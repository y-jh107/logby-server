package com.logby.server.comment.dto;

import com.logby.server.comment.Comment;
import java.time.LocalDateTime;

public record CommentResponse(
    Long id,
    Long logId,
    Long userId,
    String nickname,
    String body,
    LocalDateTime createdAt
) {
    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
            comment.getId(),
            comment.getLog().getId(),
            comment.getUser().getId(),
            comment.getUser().getNickname(),
            comment.getBody(),
            comment.getCreatedAt()
        );
    }
}
