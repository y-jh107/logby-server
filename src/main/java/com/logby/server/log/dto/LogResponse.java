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
    boolean likedByMe,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    // 내 로그 목록용 — 카운트/좋아요 미포함
    public static LogResponse from(Log log) {
        return new LogResponse(
            log.getId(),
            log.getUser().getId(),
            log.getUser().getNickname(),
            log.getTitle(),
            log.getBody(),
            log.getVisibility(),
            List.of(),
            0L, 0L, false,
            log.getCreatedAt(),
            log.getUpdatedAt()
        );
    }

    // 단건 상세 조회용 — contents + 좋아요 상태 포함
    public static LogResponse of(Log log, List<ContentResponse> contents,
                                 long likeCount, long commentCount, boolean likedByMe) {
        return new LogResponse(
            log.getId(),
            log.getUser().getId(),
            log.getUser().getNickname(),
            log.getTitle(),
            log.getBody(),
            log.getVisibility(),
            contents,
            likeCount, commentCount, likedByMe,
            log.getCreatedAt(),
            log.getUpdatedAt()
        );
    }

    // 피드/공개 목록용 — 좋아요·댓글 수 포함, likedByMe=false
    public static LogResponse feed(Log log, long likeCount, long commentCount) {
        return new LogResponse(
            log.getId(),
            log.getUser().getId(),
            log.getUser().getNickname(),
            log.getTitle(),
            log.getBody(),
            log.getVisibility(),
            List.of(),
            likeCount, commentCount, false,
            log.getCreatedAt(),
            log.getUpdatedAt()
        );
    }
}
