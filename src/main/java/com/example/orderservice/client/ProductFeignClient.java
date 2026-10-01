package com.example.orderservice.client;

import com.example.orderservice.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign Client to communicate with Product Service.
 *
 * HOW IT WORKS:
 * - @FeignClient creates a proxy that makes HTTP calls to the Product Service
 * - "product-service" is the client name (used for configuration)
 * - "url" points to the Product Service base URL
 * - Methods map directly to Product Service REST endpoints
 * - Spring Cloud OpenFeign handles serialization/deserialization automatically
 *
 * WHEN TO USE FEIGN CLIENT:
 * - Simple synchronous REST calls between services
 * - When you want a clean, declarative HTTP client (like a JPA repository for REST)
 * - Best for straightforward request-response patterns
 */
@FeignClient(
        name = "product-service",
        url = "${product.service.url}"   // Reads URL from application.yml
)
public interface ProductFeignClient {

    // GET /api/products/{id} - Fetch product details
    @GetMapping("/api/products/{id}")
    ProductDTO getProductById(@PathVariable("id") Long id);

    // PUT /api/products/{id}/reduce-quantity?quantity=n - Reduce stock after order
    @PutMapping("/api/products/{id}/reduce-quantity")
    ProductDTO reduceQuantity(@PathVariable("id") Long id, @RequestParam("quantity") int quantity);
}
