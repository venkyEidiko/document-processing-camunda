package com.documentprocessing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WebClientService {

    private final WebClient webClient;

    public <T> T[] postCallForArray(String url, Object data, Class<T[]> arrayType) {
        T[] responseArray = this.webClient.post()
                .uri(url)
                .body(Mono.just(data), data.getClass())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> {
            return Mono.error(new Exception("Failed to call API: " + response.statusCode()));
        })
                .bodyToMono(arrayType).block();

        return responseArray;
    }

    public <T> T postCall(String url, Object data, Class<T> responseType) {
        Mono<T> response = this.webClient.post()
                .uri(url)
                .body(Mono.just(data), data.getClass())
                .retrieve()
                .onStatus(HttpStatusCode::isError, result -> {
            return Mono.error(new Exception("Failed to call an API"));
        })
                .bodyToMono(responseType);

        return response.block();
    }

    public void postCall(String url) {
        this.webClient.post()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::isError, result -> {
            return Mono.error(new Exception("Failed to call an API"));
        })
                .toBodilessEntity().block();
    }
}
