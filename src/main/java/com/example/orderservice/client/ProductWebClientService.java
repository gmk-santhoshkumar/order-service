package com.example.orderservice.client;

import com.example.orderservice.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * WebClient-based service to communicate with Product Service.
 *
 * HOW IT WORKS:
 * - WebClient is a non-blocking, reactive HTTP client from Spring WebFlux
 * - We use .block() here to make it synchronous (since our app is Spring MVC)
 * - In a fully reactive app, you would return Mono/Flux instead of blocking
 *
 * WHEN TO USE WEBCLIENT:
 * - When you need more control over HTTP requests (headers, timeouts, retries)
 * - When you want non-blocking/reactive communication
 * - When you need to compose multiple API calls
 * - Recommended by Spring team as replacement for RestTemplate
 *
 * FEIGN vs WEBCLIENT:
 * ┌───────────────┬──────────────────────┬──────────────────────┐
 * │ Feature       │ Feign Client         │ WebClient            │
 * ├───────────────┼──────────────────────┼──────────────────────┤
 * │ Style         │ Declarative (interface)│ Programmatic (builder)│
 * │ Blocking      │ Yes (synchronous)     │ Optional (can be async)│
 * │ Ease of Use   │ Very Easy            │ Moderate             │
 * │ Flexibility   │ Limited              │ Highly Flexible      │
 * │ Error Handling│ Basic                │ Advanced             │
 * │ Best For      │ Simple REST calls    │ Complex scenarios    │
 * └───────────────┴──────────────────────┴──────────────────────┘
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductWebClientService {

    private final WebClient webClient;

    /**
     * Fetch product details by ID using WebClient.
     * Uses .block() to wait for the response synchronously.
     */
    public ProductDTO getProductById(Long productId) {
        log.info("Calling Product Service via WebClient for product ID: {}", productId);

        return webClient.get()
                .uri("/api/products/{id}", productId)
                .retrieve()
                .bodyToMono(ProductDTO.class)
                .block();   // Block and wait for response (synchronous)
    }

    /**
     * Reduce product quantity after order is placed.
     */
    public ProductDTO reduceQuantity(Long productId, int quantity) {
        log.info("Reducing quantity for product ID: {} by {} via WebClient", productId, quantity);

        return webClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/products/{id}/reduce-quantity")
                        .queryParam("quantity", quantity)
                        .build(productId))
                .retrieve()
                .bodyToMono(ProductDTO.class)
                .block();   // Block and wait for response (synchronous)
    }
}
