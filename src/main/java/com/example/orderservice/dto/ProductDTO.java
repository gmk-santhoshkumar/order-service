package com.example.orderservice.dto;

import lombok.*;

/**
 * DTO to hold product data received from Product Service.
 * We use a DTO instead of depending on the Product entity directly
 * to keep the services loosely coupled.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductDTO {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private String category;
}
