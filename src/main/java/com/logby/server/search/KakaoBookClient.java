package com.logby.server.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.logby.server.common.exception.BusinessException;
import com.logby.server.common.exception.ErrorCode;
import com.logby.server.search.dto.BookItem;
import java.time.Duration;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class KakaoBookClient {

    private final WebClient webClient;

    public KakaoBookClient(@Value("${kakao.api-key}") String apiKey) {
        this.webClient = WebClient.builder()
            .baseUrl("https://dapi.kakao.com")
            .defaultHeader("Authorization", "KakaoAK " + apiKey)
            .build();
    }

    public List<BookItem> searchBooks(String query) {
        try {
            KakaoApiResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/v3/search/book")
                    .queryParam("query", query)
                    .queryParam("size", 20)
                    .build())
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(),
                    res -> Mono.error(new BusinessException(ErrorCode.EXTERNAL_API_ERROR)))
                .bodyToMono(KakaoApiResponse.class)
                .timeout(Duration.ofSeconds(5))
                .block();

            if (response == null || response.documents() == null) {
                return List.of();
            }

            return response.documents().stream()
                .map(doc -> new BookItem(
                    doc.title(),
                    doc.authors(),
                    doc.publisher(),
                    doc.isbn(),
                    doc.thumbnail(),
                    doc.price(),
                    doc.salePrice(),
                    doc.url()
                ))
                .toList();

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.EXTERNAL_API_ERROR);
        }
    }

    private record KakaoApiResponse(List<KakaoDocument> documents) {}

    private record KakaoDocument(
        String title,
        List<String> authors,
        String publisher,
        String isbn,
        String thumbnail,
        int price,
        @JsonProperty("sale_price") int salePrice,
        String url
    ) {}
}
