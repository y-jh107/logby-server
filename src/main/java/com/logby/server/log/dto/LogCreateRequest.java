package com.logby.server.log.dto;

import com.logby.server.log.Visibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LogCreateRequest(
    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 100, message = "제목은 100자 이하여야 합니다.")
    String title,

    @NotBlank(message = "내용은 필수입니다.")
    String body,

    @NotNull(message = "공개 범위는 필수입니다.")
    Visibility visibility
) {}
