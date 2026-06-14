package com.logby.server.user;

import com.logby.server.common.response.ApiResponse;
import com.logby.server.log.LogService;
import com.logby.server.log.dto.LogResponse;
import com.logby.server.user.dto.MyProfileResponse;
import com.logby.server.user.dto.UpdateProfileRequest;
import com.logby.server.user.dto.UserProfileResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class ProfileController {

    private final UserService userService;
    private final LogService logService;

    @GetMapping("/me")
    public ApiResponse<MyProfileResponse> getMyProfile(@AuthenticationPrincipal Long userId) {
        return ApiResponse.ok(userService.getMyProfile(userId));
    }

    @PatchMapping("/me")
    public ApiResponse<MyProfileResponse> updateProfile(
        @AuthenticationPrincipal Long userId,
        @Valid @RequestBody UpdateProfileRequest request
    ) {
        return ApiResponse.ok(userService.updateProfile(userId, request));
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserProfileResponse> getUserProfile(
        @AuthenticationPrincipal Long currentUserId,
        @PathVariable Long userId
    ) {
        return ApiResponse.ok(userService.getUserProfile(userId, currentUserId));
    }

    @GetMapping("/{userId}/logs")
    public ApiResponse<Page<LogResponse>> getUserLogs(
        @PathVariable Long userId,
        @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ApiResponse.ok(logService.getUserPublicLogs(userId, pageable));
    }
}
