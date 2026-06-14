package com.logby.server.log.dto;

import com.logby.server.log.Log;
import com.logby.server.log.Visibility;
import java.time.LocalDateTime;

public record LogResponse(
    Long id,
    String title,
    String body,
    Visibility visibility,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static LogResponse from(Log log) {
        return new LogResponse(
            log.getId(),
            log.getTitle(),
            log.getBody(),
            log.getVisibility(),
            log.getCreatedAt(),
            log.getUpdatedAt()
        );
    }
}
