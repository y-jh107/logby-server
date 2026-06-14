package com.logby.server.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.logby.server.common.exception.BusinessException;
import com.logby.server.common.exception.ErrorCode;
import com.logby.server.search.dto.MovieItem;
import com.logby.server.search.dto.TvItem;
import java.time.Duration;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class TmdbClient {

    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    private final WebClient webClient;
    private final String apiKey;

    public TmdbClient(@Value("${tmdb.api-key}") String apiKey) {
        this.apiKey = apiKey;
        this.webClient = WebClient.builder()
            .baseUrl("https://api.themoviedb.org/3")
            .build();
    }

    public List<MovieItem> searchMovies(String query) {
        try {
            TmdbMovieResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/search/movie")
                    .queryParam("api_key", apiKey)
                    .queryParam("query", query)
                    .queryParam("language", "ko-KR")
                    .build())
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(),
                    res -> Mono.error(new BusinessException(ErrorCode.EXTERNAL_API_ERROR)))
                .bodyToMono(TmdbMovieResponse.class)
                .timeout(Duration.ofSeconds(5))
                .block();

            if (response == null || response.results() == null) {
                return List.of();
            }

            return response.results().stream()
                .map(doc -> new MovieItem(
                    doc.id(),
                    doc.title(),
                    doc.originalTitle(),
                    doc.overview(),
                    doc.releaseDate(),
                    doc.voteAverage(),
                    doc.posterPath() != null ? IMAGE_BASE_URL + doc.posterPath() : null
                ))
                .toList();

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.EXTERNAL_API_ERROR);
        }
    }

    public List<TvItem> searchTv(String query) {
        try {
            TmdbTvResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/search/tv")
                    .queryParam("api_key", apiKey)
                    .queryParam("query", query)
                    .queryParam("language", "ko-KR")
                    .build())
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(),
                    res -> Mono.error(new BusinessException(ErrorCode.EXTERNAL_API_ERROR)))
                .bodyToMono(TmdbTvResponse.class)
                .timeout(Duration.ofSeconds(5))
                .block();

            if (response == null || response.results() == null) {
                return List.of();
            }

            return response.results().stream()
                .map(doc -> new TvItem(
                    doc.id(),
                    doc.name(),
                    doc.originalName(),
                    doc.overview(),
                    doc.firstAirDate(),
                    doc.voteAverage(),
                    doc.posterPath() != null ? IMAGE_BASE_URL + doc.posterPath() : null
                ))
                .toList();

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.EXTERNAL_API_ERROR);
        }
    }

    private record TmdbMovieResponse(List<TmdbMovieDocument> results) {}

    private record TmdbMovieDocument(
        Long id,
        String title,
        @JsonProperty("original_title") String originalTitle,
        String overview,
        @JsonProperty("release_date") String releaseDate,
        @JsonProperty("vote_average") double voteAverage,
        @JsonProperty("poster_path") String posterPath
    ) {}

    private record TmdbTvResponse(List<TmdbTvDocument> results) {}

    private record TmdbTvDocument(
        Long id,
        String name,
        @JsonProperty("original_name") String originalName,
        String overview,
        @JsonProperty("first_air_date") String firstAirDate,
        @JsonProperty("vote_average") double voteAverage,
        @JsonProperty("poster_path") String posterPath
    ) {}
}
