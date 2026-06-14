package com.logby.server.user.dto;

import com.logby.server.user.User;

public record UserProfileResponse(
    Long id,
    String nickname,
    String bio,
    String profileImage,
    long followerCount,
    long followingCount,
    long publicLogCount,
    boolean isFollowing
) {
    public static UserProfileResponse of(User user, long followerCount, long followingCount,
                                         long publicLogCount, boolean isFollowing) {
        return new UserProfileResponse(
            user.getId(),
            user.getNickname(),
            user.getBio(),
            user.getProfileImage(),
            followerCount,
            followingCount,
            publicLogCount,
            isFollowing
        );
    }
}
