package com.logby.server.user.dto;

import com.logby.server.user.User;

public record MyProfileResponse(
    Long id,
    String email,
    String nickname,
    String bio,
    String profileImage
) {
    public static MyProfileResponse from(User user) {
        return new MyProfileResponse(
            user.getId(),
            user.getEmail(),
            user.getNickname(),
            user.getBio(),
            user.getProfileImage()
        );
    }
}
