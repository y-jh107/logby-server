package com.logby.server.search.dto;

public record MovieItem(
    Long id,
    String title,
    String originalTitle,
    String overview,
    String releaseDate,
    double voteAverage,
    String posterUrl
) {}
