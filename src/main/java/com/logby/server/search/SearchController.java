package com.logby.server.search;

import com.logby.server.common.response.ApiResponse;
import com.logby.server.search.dto.BookItem;
import com.logby.server.search.dto.MovieItem;
import com.logby.server.search.dto.TvItem;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RequestMapping("/api/search")
@RestController
public class SearchController {

    private final KakaoBookClient kakaoBookClient;
    private final TmdbClient tmdbClient;

    @GetMapping("/books")
    public ApiResponse<List<BookItem>> searchBooks(
        @RequestParam @NotBlank(message = "검색어를 입력해주세요.") String query
    ) {
        return ApiResponse.ok(kakaoBookClient.searchBooks(query));
    }

    @GetMapping("/movies")
    public ApiResponse<List<MovieItem>> searchMovies(
        @RequestParam @NotBlank(message = "검색어를 입력해주세요.") String query
    ) {
        return ApiResponse.ok(tmdbClient.searchMovies(query));
    }

    @GetMapping("/tv")
    public ApiResponse<List<TvItem>> searchTv(
        @RequestParam @NotBlank(message = "검색어를 입력해주세요.") String query
    ) {
        return ApiResponse.ok(tmdbClient.searchTv(query));
    }
}
