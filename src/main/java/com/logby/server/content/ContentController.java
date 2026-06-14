package com.logby.server.content;

import com.logby.server.common.response.ApiResponse;
import com.logby.server.content.dto.ContentRequest;
import com.logby.server.content.dto.ContentResponse;
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
@RequestMapping("/api/logs/{logId}/contents")
@RestController
public class ContentController {

    private final ContentService contentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ContentResponse> addContent(
        @AuthenticationPrincipal Long userId,
        @PathVariable Long logId,
        @Valid @RequestBody ContentRequest request
    ) {
        return ApiResponse.ok(contentService.addContent(logId, userId, request));
    }

    @DeleteMapping("/{contentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteContent(
        @AuthenticationPrincipal Long userId,
        @PathVariable Long logId,
        @PathVariable Long contentId
    ) {
        contentService.deleteContent(logId, contentId, userId);
    }
}
