package com.example.BrokerageApplication.controller;

import com.example.BrokerageApplication.dto.OrderRequestDTO;
import com.example.BrokerageApplication.model.Order;
import com.example.BrokerageApplication.model.OrderSide;
import com.example.BrokerageApplication.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/customer/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequestDTO orderRequest) {
        Long customerId = getAuthenticatedCustomerId();
        Order order = orderService.createOrder(customerId, orderRequest.getAssetName(), orderRequest.getOrderSide(),
                orderRequest.getSize(), orderRequest.getPrice());
        return ResponseEntity.ok(order);
    }


    @GetMapping
    public ResponseEntity<List<Order>> getOrders(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        Long customerId = getAuthenticatedCustomerId();

        startDate = startDate.trim();
        endDate = endDate.trim();

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);

        List<Order> orders = orderService.getOrders(customerId, start, end);
        return ResponseEntity.ok(orders);
    }


    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long customerId;

        if (principal instanceof UserDetails) {
            customerId = Long.parseLong(((UserDetails) principal).getUsername());
        } else {
            throw new IllegalArgumentException("Invalid user session");
        }

        orderService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    private Long getAuthenticatedCustomerId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            try {
                return Long.parseLong(((UserDetails) principal).getUsername());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid user ID format.");
            }
        } else {
            throw new IllegalArgumentException("Invalid user session");
        }
    }

}

