package kr.co.yigil.travel.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import kr.co.yigil.file.AttachFile;
import kr.co.yigil.file.FileType;
import kr.co.yigil.file.FileUploader;
import kr.co.yigil.member.Ages;
import kr.co.yigil.member.Gender;
import kr.co.yigil.member.Member;
import kr.co.yigil.member.SocialLoginType;
import kr.co.yigil.travel.domain.Course;
import kr.co.yigil.travel.domain.Spot;
import kr.co.yigil.travel.domain.course.CourseCommand.ModifyCourseRequest;
import kr.co.yigil.travel.domain.course.CourseCommand.RegisterCourseRequest;
import kr.co.yigil.travel.domain.course.CourseCommand.RegisterCourseRequestWithSpotInfo;
import kr.co.yigil.travel.domain.course.CourseInfo;
import kr.co.yigil.travel.domain.course.CourseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.LineString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class CourseFacadeTest {

    @InjectMocks
    private CourseFacade courseFacade;

    @Mock
    private CourseService courseService;

    @Mock
    private FileUploader fileUploader;


    @DisplayName("getCourseSliceInPlace 메서드가 유효한 요청이 들어왔을 때 Course의 Slice객체를 잘 반환하는지")
    @Test
    void whenGetCoursesSliceInPlace_WithValidRequest() {
        Long id = 1L;
        String email = "test@test.com";
        String socialLoginId = "12345";
        String nickname = "tester";
        String profileImageUrl = "test.jpg";
        Member member = new Member(id, email, socialLoginId, nickname, profileImageUrl,
                SocialLoginType.KAKAO, Ages.NONE, Gender.NONE);

        String title = "Test Course Title";
        String description = "Test Course Description";
        double rate = 5.0;
        LineString path = null;
        boolean isPrivate = false;
        List<Spot> spots = Collections.emptyList();
        int representativeSpotOrder = 0;
        AttachFile mapStaticImageFile = null;

        Course course = new Course(id, member, title, description, rate, path, isPrivate, spots, representativeSpotOrder, mapStaticImageFile);

        Long placeId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Slice<Course> expectedSlice = new PageImpl<>(Collections.singletonList(course), pageable,
                1);
        when(courseService.getCoursesSliceInPlace(eq(placeId), any(Pageable.class))).thenReturn(
                expectedSlice);

        Slice<Course> result = courseFacade.getCourseSliceInPlace(placeId, pageable);

        assertNotNull(result);
        assertEquals(expectedSlice, result);
        verify(courseService, times(1)).getCoursesSliceInPlace(eq(placeId), any(Pageable.class));
    }

    @DisplayName("registerCourse 메서드가 CourseServicer를 잘 호출하는지")
    @Test
    void registerCourse_ShouldCallService() {
        RegisterCourseRequest command = mock(RegisterCourseRequest.class);
        Long memberId = 1L;

        courseFacade.registerCourse(command, memberId);

        verify(courseService).registerCourse(command, memberId);
    }

    @DisplayName("registerCourseWithoutSeries 메서드가 CourseService를 잘 호출하는지")
    @Test
    void registerCourseWithoutSeries_ShouldCallServiceAndUploader() {
        RegisterCourseRequestWithSpotInfo command = mock(RegisterCourseRequestWithSpotInfo.class);
        Long memberId = 1L;

        courseFacade.registerCourseWithoutSeries(command, memberId);

        verify(courseService).registerCourseWithoutSeries(command, memberId);
    }

    @DisplayName("retrieveCourseInfo 메서드가 CourseInfo를 잘 반환하는지")
    @Test
    void retrieveCourseInfo_ShoudReturnCourseInfo() {
        Long courseId = 1L;
        CourseInfo.Main expectedCourseInfo = mock(CourseInfo.Main.class);

        when(courseService.retrieveCourseInfo(courseId)).thenReturn(expectedCourseInfo);

        CourseInfo.Main result = courseFacade.retrieveCourseInfo(courseId);

        assertEquals(expectedCourseInfo, result);
        verify(courseService).retrieveCourseInfo(courseId);
    }

    @DisplayName("modifyCourse 메서드가 CourseService를 잘 호출하는지")
    @Test
    void modifyCourse_ShouldCallService() {
        ModifyCourseRequest command = mock(ModifyCourseRequest.class);
        Long courseId = 1L;
        Long memberId = 1L;
        Course mockCourse = mock(Course.class);
        when(courseService.modifyCourse(command, courseId, memberId)).thenReturn(mockCourse);

        courseFacade.modifyCourse(command, courseId, memberId);

        verify(courseService).modifyCourse(command, courseId, memberId);
    }

    @DisplayName("deleteCourse 메서드가 CourseSerivce를 잘 호출하는지")
    @Test
    void deleteCourse_ShouldCallService() {
        Long courseId = 1L;
        Long memberId = 1L;

        doNothing().when(courseService).deleteCourse(courseId, memberId);

        courseFacade.deleteCourse(courseId, memberId);

        verify(courseService).deleteCourse(courseId, memberId);
    }


    @DisplayName("getMemberCourseInfo 메서드가 유효한 요청이 들어왔을 때 CourseInfo의 MyCoursesResponse 객체를 잘 반환하는지")
    @Test
    void WhenGetMemberCourseInfo_ThenShouldReturnValidMyCoursesResponse() {
        // Given
        Long memberId = 1L;
        int totalPages = 1;
        PageRequest pageable = PageRequest.of(0, 5);

        String email = "test@test.com";
        String socialLoginId = "12345";
        String nickname = "tester";
        String profileImageUrl = "test.jpg";
        Member member = new Member(memberId, email, socialLoginId, nickname, profileImageUrl,
            SocialLoginType.KAKAO, Ages.NONE, Gender.NONE);

        Long courseId = 1L;
        String title = "Test Course Title";
        double rate = 5.0;
        LineString path = null;
        boolean isPrivate = false;
        List<Spot> spots = Collections.emptyList();
        int representativeSpotOrder = 0;
        AttachFile mapStaticImageFile = new AttachFile(FileType.IMAGE, "test.jpg", "test.jpg", 10L);

        Course mockCourse = new Course(courseId, member, title, null, rate, path, isPrivate,
            spots, representativeSpotOrder, mapStaticImageFile);

        CourseInfo.CourseListInfo courseInfo = new CourseInfo.CourseListInfo(mockCourse);
        List<CourseInfo.CourseListInfo> courseList = Collections.singletonList(courseInfo);

        CourseInfo.MyCoursesResponse mockCourseListResponse = new CourseInfo.MyCoursesResponse(
            courseList,
            totalPages
        );

        when(courseService.retrieveCourseList(anyLong(), any(Pageable.class), anyString())).thenReturn(
            mockCourseListResponse);

        // When
        var result = courseFacade.getMemberCoursesInfo(memberId, pageable, "private");

        // Then
        assertThat(result).isNotNull()
            .isInstanceOf(CourseInfo.MyCoursesResponse.class)
            .usingRecursiveComparison().isEqualTo(mockCourseListResponse);
        assertThat(result.getContent().size()).isEqualTo(1);
    }
}
