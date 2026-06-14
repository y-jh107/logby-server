package com.logby.server.user.dto;

import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
    @Size(min = 2, max = 20, message = "닉네임은 2~20자 사이여야 합니다.")
    String nickname,

    @Size(max = 200, message = "소개는 200자 이하여야 합니다.")
    String bio,

    String profileImage
) {}
