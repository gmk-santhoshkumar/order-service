package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // ========================================
    //  Place Order using FEIGN CLIENT
    // ========================================

    /**
     * POST /api/orders/feign - Place order using Feign Client
     * This endpoint uses Feign Client to call Product Service.
     */
    @PostMapping("/feign")
    public ResponseEntity<OrderResponse> placeOrderWithFeign(@Valid @RequestBody OrderRequest request) {
        OrderResponse response = orderService.placeOrderWithFeign(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ========================================
    //  Place Order using WEBCLIENT
    // ========================================

    /**
     * POST /api/orders/webclient - Place order using WebClient
     * This endpoint uses WebClient to call Product Service.
     */
    @PostMapping("/webclient")
    public ResponseEntity<OrderResponse> placeOrderWithWebClient(@Valid @RequestBody OrderRequest request) {
        OrderResponse response = orderService.placeOrderWithWebClient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ========================================
    //  Standard CRUD Operations
    // ========================================

    // GET /api/orders - Get all orders
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // GET /api/orders/{id} - Get order by ID
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        OrderResponse order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    // PUT /api/orders/{id}/cancel - Cancel an order
    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long id) {
        OrderResponse order = orderService.cancelOrder(id);
        return ResponseEntity.ok(order);
    }

    // DELETE /api/orders/{id} - Delete an order
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
