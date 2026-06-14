package com.logby.server.log;

import com.logby.server.common.response.ApiResponse;
import com.logby.server.log.dto.LogCreateRequest;
import com.logby.server.log.dto.LogResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/logs")
@RestController
public class LogController {

    private final LogService logService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<LogResponse> create(
        @AuthenticationPrincipal Long userId,
        @Valid @RequestBody LogCreateRequest request
    ) {
        return ApiResponse.ok(logService.create(userId, request));
    }

    @GetMapping
    public ApiResponse<List<LogResponse>> getMyLogs(@AuthenticationPrincipal Long userId) {
        return ApiResponse.ok(logService.getMyLogs(userId));
    }

    @GetMapping("/{logId}")
    public ApiResponse<LogResponse> getLog(
        @AuthenticationPrincipal Long userId,
        @PathVariable Long logId
    ) {
        return ApiResponse.ok(logService.getLog(logId, userId));
    }

    @PutMapping("/{logId}")
    public ApiResponse<LogResponse> update(
        @AuthenticationPrincipal Long userId,
        @PathVariable Long logId,
        @Valid @RequestBody LogCreateRequest request
    ) {
        return ApiResponse.ok(logService.update(logId, userId, request));
    }

    @DeleteMapping("/{logId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
        @AuthenticationPrincipal Long userId,
        @PathVariable Long logId
    ) {
        logService.delete(logId, userId);
    }
}
