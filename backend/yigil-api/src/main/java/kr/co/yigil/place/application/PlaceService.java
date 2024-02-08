package kr.co.yigil.place.application;

import kr.co.yigil.file.AttachFile;
import kr.co.yigil.global.exception.BadRequestException;
import kr.co.yigil.global.exception.ExceptionCode;
import kr.co.yigil.place.Place;
import kr.co.yigil.place.dto.request.PlaceDto;
import kr.co.yigil.place.dto.response.PlaceFindDto;
import kr.co.yigil.place.dto.response.PlaceInfoResponse;
import kr.co.yigil.place.dto.response.PlaceMapStaticImageResponse;
import kr.co.yigil.place.dto.response.RateResponse;
import kr.co.yigil.place.repository.PlaceRepository;
import kr.co.yigil.travel.application.SpotRedisIntegrityService;
import kr.co.yigil.travel.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final SpotRepository spotRepository;
    private final SpotRedisIntegrityService spotRedisIntegrityService;
    private final PlaceRateRedisIntegrityService placeRateRedisIntegrityService;

    @Transactional(readOnly = true)
    public PlaceInfoResponse getPlaceInfo(Long placeId) {
        Place place = getPlaceById(placeId);
        double totalRate = placeRateRedisIntegrityService.ensurePlaceRate(placeId)
                .getTotalRate();
        int spotCount = spotRedisIntegrityService.ensureSpotCounts(placeId).getSpotCount();
        double averageRate = totalRate / spotCount;
        return PlaceInfoResponse.from(place, spotCount, averageRate);
    }

    @Transactional(readOnly = true)
    public RateResponse getMemberRate(Long placeId, Long memberId) {
        return spotRepository.findByPlaceIdAndMemberId(placeId, memberId)
                .map(spot -> new RateResponse(spot.getRate()))
                .orElse(new RateResponse(null));
    }

    @Transactional(readOnly = true)
    public PlaceMapStaticImageResponse getPlaceStaticImage(String name, String address) {
        Place place = placeRepository.findByNameAndAddress(name, address)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PLACE_ID));
        return PlaceMapStaticImageResponse.from(place);
    }

    public Place getPlaceById(Long placeId) {
        return placeRepository.findById(placeId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PLACE_ID));
    }

    public Place getOrCreatePlace(String placeName, String placeAddress, String placePointJson,
            AttachFile placeImageFile, AttachFile mapStaticImageFile) {
        return placeRepository.findByNameAndAddress(placeName, placeAddress)
                .orElseGet(
                        () -> placeRepository.save(PlaceDto.toEntity(
                                        placeName,
                                        placeAddress,
                                        placePointJson,
                                        placeImageFile,
                                        mapStaticImageFile
                                )
                        )
                );
    }

    public Slice<PlaceFindDto> getPlaceList(Pageable pageable) {
        return placeRepository.findAllPlaces(pageable)
                .map(this::getPlaceFindDto);
    }

    private PlaceFindDto getPlaceFindDto(Place place) {
        double totalRate = placeRateRedisIntegrityService.ensurePlaceRate(place.getId())
                .getTotalRate();
        int spotCount = spotRedisIntegrityService.ensureSpotCounts(place.getId()).getSpotCount();
        double averageRate = totalRate / spotCount;
        return PlaceFindDto.from(place, spotCount, averageRate);
    }
}
