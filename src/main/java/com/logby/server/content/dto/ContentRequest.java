package com.logby.server.content.dto;

import com.logby.server.content.ContentType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ContentRequest(
    @NotBlank(message = "URL은 필수입니다.")
    String url,

    @NotNull(message = "콘텐츠 타입은 필수입니다.")
    ContentType contentType,

    @Min(value = 0, message = "순서는 0 이상이어야 합니다.")
    int sortOrder
) {}
