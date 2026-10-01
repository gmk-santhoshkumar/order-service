package com.example.orderservice.service;

import com.example.orderservice.client.ProductFeignClient;
import com.example.orderservice.client.ProductWebClientService;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.dto.ProductDTO;
import com.example.orderservice.exception.OrderNotFoundException;
import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductFeignClient productFeignClient;         // Feign Client
    private final ProductWebClientService productWebClientService; // WebClient

    /**
     * Place a new order using FEIGN CLIENT to call Product Service.
     *
     * Flow:
     * 1. Fetch product details from Product Service (via Feign)
     * 2. Calculate total price
     * 3. Reduce product stock in Product Service (via Feign)
     * 4. Save the order in Order DB
     */
    public OrderResponse placeOrderWithFeign(OrderRequest request) {
        log.info("Placing order using FEIGN CLIENT for product ID: {}", request.getProductId());

        // Step 1: Get product details from Product Service using Feign Client
        ProductDTO product = productFeignClient.getProductById(request.getProductId());
        log.info("Product fetched via Feign: {}", product.getName());

        // Step 2: Calculate total price
        double totalPrice = product.getPrice() * request.getQuantity();

        // Step 3: Reduce product stock in Product Service using Feign Client
        productFeignClient.reduceQuantity(request.getProductId(), request.getQuantity());
        log.info("Stock reduced via Feign for product: {}", product.getName());

        // Step 4: Save order in database
        Order order = Order.builder()
                .productId(request.getProductId())
                .productName(product.getName())
                .quantity(request.getQuantity())
                .totalPrice(totalPrice)
                .orderStatus("PLACED")
                .orderDate(LocalDateTime.now())
                .build();

        Order savedOrder = orderRepository.save(order);
        log.info("Order placed successfully with ID: {}", savedOrder.getId());

        return mapToResponse(savedOrder);
    }

    /**
     * Place a new order using WEBCLIENT to call Product Service.
     *
     * Flow is the same as Feign, but uses WebClient instead.
     * This demonstrates the alternative approach.
     */
    public OrderResponse placeOrderWithWebClient(OrderRequest request) {
        log.info("Placing order using WEBCLIENT for product ID: {}", request.getProductId());

        // Step 1: Get product details from Product Service using WebClient
        ProductDTO product = productWebClientService.getProductById(request.getProductId());
        log.info("Product fetched via WebClient: {}", product.getName());

        // Step 2: Calculate total price
        double totalPrice = product.getPrice() * request.getQuantity();

        // Step 3: Reduce product stock in Product Service using WebClient
        productWebClientService.reduceQuantity(request.getProductId(), request.getQuantity());
        log.info("Stock reduced via WebClient for product: {}", product.getName());

        // Step 4: Save order in database
        Order order = Order.builder()
                .productId(request.getProductId())
                .productName(product.getName())
                .quantity(request.getQuantity())
                .totalPrice(totalPrice)
                .orderStatus("PLACED")
                .orderDate(LocalDateTime.now())
                .build();

        Order savedOrder = orderRepository.save(order);
        log.info("Order placed successfully with ID: {}", savedOrder.getId());

        return mapToResponse(savedOrder);
    }

    // Get all orders
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Get order by ID
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        return mapToResponse(order);
    }

    // Cancel an order
    public OrderResponse cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        order.setOrderStatus("CANCELLED");
        Order updated = orderRepository.save(order);
        return mapToResponse(updated);
    }

    // Delete an order
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        orderRepository.delete(order);
    }

    // Helper: Map Order entity to OrderResponse DTO
    private OrderResponse mapToResponse(Order order) {
        return OrderResponse.builder()
                .orderId(order.getId())
                .productId(order.getProductId())
                .productName(order.getProductName())
                .quantity(order.getQuantity())
                .totalPrice(order.getTotalPrice())
                .orderStatus(order.getOrderStatus())
                .orderDate(order.getOrderDate())
                .build();
    }
}
