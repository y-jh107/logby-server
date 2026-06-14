package com.logby.server.log.dto;

import com.logby.server.content.dto.ContentResponse;
import com.logby.server.log.Log;
import com.logby.server.log.Visibility;
import java.time.LocalDateTime;
import java.util.List;

public record LogResponse(
    Long id,
    String title,
    String body,
    Visibility visibility,
    List<ContentResponse> contents,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    // 목록 조회용 — contents 빈 리스트
    public static LogResponse from(Log log) {
        return new LogResponse(
            log.getId(),
            log.getTitle(),
            log.getBody(),
            log.getVisibility(),
            List.of(),
            log.getCreatedAt(),
            log.getUpdatedAt()
        );
    }

    // 단건 상세 조회용 — contents 포함
    public static LogResponse of(Log log, List<ContentResponse> contents) {
        return new LogResponse(
            log.getId(),
            log.getTitle(),
            log.getBody(),
            log.getVisibility(),
            contents,
            log.getCreatedAt(),
            log.getUpdatedAt()
        );
    }
}
