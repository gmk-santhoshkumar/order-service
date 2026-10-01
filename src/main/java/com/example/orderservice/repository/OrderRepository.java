package com.example.orderservice.repository;

import com.example.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Find orders by product ID
    List<Order> findByProductId(Long productId);

    // Find orders by status
    List<Order> findByOrderStatus(String orderStatus);
}
