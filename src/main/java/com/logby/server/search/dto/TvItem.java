package com.logby.server.search.dto;

public record TvItem(
    Long id,
    String name,
    String originalName,
    String overview,
    String firstAirDate,
    double voteAverage,
    String posterUrl
) {}
