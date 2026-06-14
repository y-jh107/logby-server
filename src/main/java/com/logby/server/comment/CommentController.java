package com.logby.server.comment;

import com.logby.server.comment.dto.CommentRequest;
import com.logby.server.comment.dto.CommentResponse;
import com.logby.server.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/logs/{logId}/comments")
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CommentResponse> create(
        @AuthenticationPrincipal Long userId,
        @PathVariable Long logId,
        @Valid @RequestBody CommentRequest request
    ) {
        return ApiResponse.ok(commentService.create(logId, userId, request));
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
        @AuthenticationPrincipal Long userId,
        @PathVariable Long logId,
        @PathVariable Long commentId
    ) {
        commentService.delete(logId, commentId, userId);
    }
}
