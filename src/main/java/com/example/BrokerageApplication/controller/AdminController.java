package com.example.BrokerageApplication.controller;

import com.example.BrokerageApplication.model.Order;
import com.example.BrokerageApplication.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    @GetMapping("/pending-orders")
    public ResponseEntity<List<Order>> getPendingOrders() {
        List<Order> pendingOrders = adminService.getPendingOrders();
        return ResponseEntity.ok(pendingOrders);
    }

    @PostMapping("/match-orders")
    public ResponseEntity<Void> matchPendingOrders() {
        adminService.matchPendingOrders();
        return ResponseEntity.ok().build();
    }
}
