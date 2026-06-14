package com.logby.server.log.dto;

import com.logby.server.content.dto.ContentResponse;
import com.logby.server.log.Log;
import com.logby.server.log.Visibility;
import java.time.LocalDateTime;
import java.util.List;

public record LogResponse(
    Long id,
    Long authorId,
    String authorNickname,
    String title,
    String body,
    Visibility visibility,
    List<ContentResponse> contents,
    long likeCount,
    long commentCount,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    // 목록 조회용 — contents 빈 리스트, 카운트 없음
    public static LogResponse from(Log log) {
        return new LogResponse(
            log.getId(),
            log.getUser().getId(),
            log.getUser().getNickname(),
            log.getTitle(),
            log.getBody(),
            log.getVisibility(),
            List.of(),
            0L,
            0L,
            log.getCreatedAt(),
            log.getUpdatedAt()
        );
    }

    // 단건 상세 조회용 — contents 포함
    public static LogResponse of(Log log, List<ContentResponse> contents) {
        return new LogResponse(
            log.getId(),
            log.getUser().getId(),
            log.getUser().getNickname(),
            log.getTitle(),
            log.getBody(),
            log.getVisibility(),
            contents,
            0L,
            0L,
            log.getCreatedAt(),
            log.getUpdatedAt()
        );
    }

    // 피드/목록 조회용 — 좋아요·댓글 수 포함
    public static LogResponse feed(Log log, long likeCount, long commentCount) {
        return new LogResponse(
            log.getId(),
            log.getUser().getId(),
            log.getUser().getNickname(),
            log.getTitle(),
            log.getBody(),
            log.getVisibility(),
            List.of(),
            likeCount,
            commentCount,
            log.getCreatedAt(),
            log.getUpdatedAt()
        );
    }
}
