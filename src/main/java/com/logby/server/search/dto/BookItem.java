package com.logby.server.search.dto;

import java.util.List;

public record BookItem(
    String title,
    List<String> authors,
    String publisher,
    String isbn,
    String thumbnail,
    int price,
    int salePrice,
    String url
) {}
