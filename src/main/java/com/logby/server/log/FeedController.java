package com.logby.server.log;

import com.logby.server.common.response.ApiResponse;
import com.logby.server.log.dto.LogResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/feeds")
@RestController
public class FeedController {

    private final LogService logService;

    @GetMapping
    public ApiResponse<Page<LogResponse>> getFeed(
        @AuthenticationPrincipal Long userId,
        @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ApiResponse.ok(logService.getFeed(userId, pageable));
    }
}
