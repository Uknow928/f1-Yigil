package kr.co.yigil.travel.domain.course;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import kr.co.yigil.travel.domain.Course;
import kr.co.yigil.travel.domain.Spot;
import lombok.Getter;
import lombok.ToString;

public class CourseInfo {

    @Getter
    @ToString
    public static class Main {
        private final String title;
        private final double rate;
        private final String mapStaticImageUrl;
        private final String description;
        private final List<CourseSpotInfo> courseSpotList;

        public Main(Course course) {
            this.title = course.getTitle();
            this.rate = course.getRate();
            this.mapStaticImageUrl = course.getMapStaticImageFileUrl();
            this.description = course.getDescription();
            AtomicInteger index = new AtomicInteger(1);
            this.courseSpotList = course.getSpots().stream()
                    .map(spot -> new CourseSpotInfo(spot, index.getAndIncrement()))
                    .collect(Collectors.toList());
        }
    }

    @Getter
    @ToString
    public static class CourseSpotInfo {
        private final int order;
        private final String placeName;
        private final List<String> imageUrlList;
        private final double rate;
        private final String description;
        private final LocalDateTime createDate;

        public CourseSpotInfo(Spot spot, int index) {
            this.order = index;
            this.placeName = spot.getPlace().getName();
            this.imageUrlList = spot.getAttachFiles().getUrls();
            this.rate = spot.getRate();
            this.description = spot.getDescription();
            this.createDate = spot.getCreatedAt();
        }
    }

    @Getter
    @ToString
    public static class MyCoursesResponse {

        private final List<CourseListInfo> content;
        private final int totalPages;
        public MyCoursesResponse(List<CourseListInfo> courseList, int totalPages) {
            this.content = courseList;
            this.totalPages = totalPages;
        }
    }

    @Getter
    @ToString
    public static class CourseListInfo {

        private final Long courseId;
        private final String title;
        private final Double rate;
        private final Integer spotCount;
        private final LocalDateTime createdDate;
        private final String mapStaticImageUrl;
        private final Boolean isPrivate;

        public CourseListInfo(Course course) {
            this.courseId = course.getId();
            this.title = course.getTitle();
            this.rate = course.getRate();
            this.spotCount = course.getSpots().size();
            this.createdDate = course.getCreatedAt();
            this.isPrivate = course.isPrivate();
            this.mapStaticImageUrl = course.getMapStaticImageFileUrl();
        }
    }


}
