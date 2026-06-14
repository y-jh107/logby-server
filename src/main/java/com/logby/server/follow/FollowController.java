package com.logby.server.follow;

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
@RequestMapping("/api/follows")
@RestController
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Void> follow(
        @AuthenticationPrincipal Long followerId,
        @PathVariable Long userId
    ) {
        followService.follow(followerId, userId);
        return ApiResponse.ok();
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unfollow(
        @AuthenticationPrincipal Long followerId,
        @PathVariable Long userId
    ) {
        followService.unfollow(followerId, userId);
    }
}
