package com.logby.server.content.dto;

import com.logby.server.content.Content;
import com.logby.server.content.ContentType;

public record ContentResponse(
    Long id,
    ContentType contentType,
    String url,
    int sortOrder
) {
    public static ContentResponse from(Content content) {
        return new ContentResponse(
            content.getId(),
            content.getContentType(),
            content.getUrl(),
            content.getSortOrder()
        );
    }
}
