package kr.co.yigil.travel.domain.spot;

import kr.co.yigil.travel.domain.Spot;
import kr.co.yigil.travel.domain.spot.SpotCommand.ModifySpotRequest;
import kr.co.yigil.travel.domain.spot.SpotCommand.RegisterSpotRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface SpotService {
    Slice<Spot> getSpotSliceInPlace(Long placeId, Pageable pageable);

    void registerSpot(RegisterSpotRequest command, Long memberId);

    public SpotInfo.Main retrieveSpotInfo(Long spotId);

    void modifySpot(ModifySpotRequest command, Long spotId, Long memberId);

    void deleteSpot(Long spotId, Long memberId);

    Page<Spot> getSpotListByMemberId(Long memberId, Pageable pageable);
}
