package kr.co.yigil.place.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import kr.co.yigil.auth.domain.Accessor;
import kr.co.yigil.place.domain.Place;
import kr.co.yigil.place.domain.PlaceCommand;
import kr.co.yigil.place.domain.PlaceInfo.Detail;
import kr.co.yigil.place.domain.PlaceInfo.Main;
import kr.co.yigil.place.domain.PlaceInfo.MapStaticImageInfo;
import kr.co.yigil.place.domain.PlaceService;
import kr.co.yigil.place.interfaces.dto.request.NearPlaceRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

@ExtendWith(MockitoExtension.class)
public class PlaceFacadeTest {

    @Mock
    private PlaceService placeService;

    @InjectMocks
    private PlaceFacade placeFacade;

    @DisplayName("findPlaceStaticImage 메서드가 MapStaticImageInfo를 잘 반환하는지")
    @Test
    void findPlaceStaticImage_ShouldReturnResponse() {
        MapStaticImageInfo mockResponse = mock(MapStaticImageInfo.class);
        String placeName = "장소";
        String address = "장소구 장소면 장소리";

        when(placeService.findPlaceStaticImage(placeName, address)).thenReturn(mockResponse);

        var result = placeFacade.findPlaceStaticImage(placeName, address);

        assertEquals(result, mockResponse);
        verify(placeService).findPlaceStaticImage(placeName, address);
    }

    @DisplayName("getPopularPlace 메서드가 Response를 잘 반환하는지")
    @Test
    void getPopularPlace_ShouldReturnResponse() {
        Accessor mockAccessor = mock(Accessor.class);
        Main mockResponse = mock(Main.class);

        when(placeService.getPopularPlace(mockAccessor)).thenReturn(List.of(mockResponse));

        var result = placeFacade.getPopularPlace(mockAccessor);

        assertEquals(result, List.of(mockResponse));
        verify(placeService).getPopularPlace(any(Accessor.class));
    }

    @DisplayName("retrievePlaceInfo 메서드가 Response를 잘 반환하는지")
    @Test
    void retrievePlaceInfo_ShouldReturnResponse() {
        Accessor mockAccessor = mock(Accessor.class);
        Detail mockResponse = mock(Detail.class);
        Long placeId = 1L;

        when(placeService.retrievePlace(placeId, mockAccessor)).thenReturn(mockResponse);

        var result = placeFacade.retrievePlaceInfo(placeId, mockAccessor);

        assertEquals(result, mockResponse);
        verify(placeService).retrievePlace(anyLong(), any(Accessor.class));
    }

    @DisplayName("getPlaceRegion 메서드가 Response를 잘 반환하는지")
    @Test
    void getPlaceRegion_ShouldReturnResponse() {
        Accessor mockAccessor = mock(Accessor.class);
        Main mockResponse = mock(Main.class);
        Long regionId = 1L;

        when(placeService.getPlaceInRegion(regionId, mockAccessor)).thenReturn(List.of(mockResponse));

        var result = placeFacade.getPlaceInRegion(regionId, mockAccessor);

        assertEquals(result, List.of(mockResponse));
        verify(placeService).getPlaceInRegion(anyLong(), any(Accessor.class));
    }

    @DisplayName("getNearPlace 메서드가 Response를 잘 반환하는지")
    @Test
    void getNearPlace_ShouldReturnResponse() {
        PlaceCommand.NearPlaceRequest mockCommand = mock(PlaceCommand.NearPlaceRequest.class);
        Page<Place> mockResponse = mock(Page.class);

        when(placeService.getNearPlace(mockCommand)).thenReturn(mockResponse);

        var result = placeFacade.getNearPlace(mockCommand);

        assertEquals(result, mockResponse);
        verify(placeService).getNearPlace(any(
                kr.co.yigil.place.domain.PlaceCommand.NearPlaceRequest.class));
    }
}
