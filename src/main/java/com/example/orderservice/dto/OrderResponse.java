package com.example.orderservice.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Response DTO for order details.
 * Includes product information fetched from Product Service.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long orderId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private Double totalPrice;
    private String orderStatus;
    private LocalDateTime orderDate;
}
