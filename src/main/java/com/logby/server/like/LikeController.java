package com.logby.server.like;

import com.logby.server.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/logs/{logId}/likes")
@RestController
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Void> like(
        @AuthenticationPrincipal Long userId,
        @PathVariable Long logId
    ) {
        likeService.like(logId, userId);
        return ApiResponse.ok();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlike(
        @AuthenticationPrincipal Long userId,
        @PathVariable Long logId
    ) {
        likeService.unlike(logId, userId);
    }
}
