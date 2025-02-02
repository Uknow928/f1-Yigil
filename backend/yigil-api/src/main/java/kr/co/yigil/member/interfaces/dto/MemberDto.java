package kr.co.yigil.member.interfaces.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

public class MemberDto {

    @Getter
    @Setter
    @ToString
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberUpdateRequest {

        private String nickname;
        private String ages;
        private String gender;
        private MultipartFile profileImageFile;
        private List<Long> favoriteRegionIds;
    }

    @Getter
    @Builder
    @ToString
    public static class Main {

        private final Long memberId;
        private final String email;
        private final String nickname;
        private final String profileImageUrl;
        private final List<Long> favoriteRegionIds;
        private final int followingCount;
        private final int followerCount;
    }

    @Getter
    @Builder
    @ToString
    public static class MemberUpdateResponse {

        private final String message;
    }

    @Getter
    @Builder
    @ToString
    public static class MemberDeleteResponse {
        private final String message;
    }
}