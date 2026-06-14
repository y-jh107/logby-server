package com.logby.server.content;

import com.logby.server.common.exception.BusinessException;
import com.logby.server.common.exception.ErrorCode;
import com.logby.server.content.dto.ContentRequest;
import com.logby.server.content.dto.ContentResponse;
import com.logby.server.log.Log;
import com.logby.server.log.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ContentService {

    private final ContentRepository contentRepository;
    private final LogRepository logRepository;

    @Transactional
    public ContentResponse addContent(Long logId, Long userId, ContentRequest request) {
        Log log = logRepository.findById(logId)
            .orElseThrow(() -> new BusinessException(ErrorCode.LOG_NOT_FOUND));

        if (!log.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.LOG_ACCESS_DENIED);
        }

        Content content = Content.builder()
            .log(log)
            .contentType(request.contentType())
            .url(request.url())
            .sortOrder(request.sortOrder())
            .build();

        return ContentResponse.from(contentRepository.save(content));
    }

    @Transactional
    public void deleteContent(Long logId, Long contentId, Long userId) {
        Content content = contentRepository.findById(contentId)
            .orElseThrow(() -> new BusinessException(ErrorCode.CONTENT_NOT_FOUND));

        if (!content.getLog().getId().equals(logId)) {
            throw new BusinessException(ErrorCode.CONTENT_NOT_FOUND);
        }
        if (!content.getLog().getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.LOG_ACCESS_DENIED);
        }

        contentRepository.delete(content);
    }
}
