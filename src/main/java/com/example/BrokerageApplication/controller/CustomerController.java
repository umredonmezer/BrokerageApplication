package com.example.BrokerageApplication.controller;

import com.example.BrokerageApplication.model.Customer;
import com.example.BrokerageApplication.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        String fullName = request.get("fullName");
        boolean isAdmin = Boolean.parseBoolean(request.getOrDefault("isAdmin", "false"));

        try {
            Customer customer = customerService.registerCustomer(username, password, fullName, isAdmin);
            return ResponseEntity.ok(Map.of("message", "User registered successfully!", "customerId", customer.getId()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getCustomer(@PathVariable String username) {
        Optional<Customer> customer = customerService.getCustomerByUsername(username);
        if (customer.isPresent()) {
            return ResponseEntity.ok(Map.of(
                    "username", customer.get().getUsername(),
                    "fullName", customer.get().getFullName(),
                    "role", customer.get().getRole().name()
            ));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{username}/role")
    public ResponseEntity<String> getUserRole(@PathVariable String username) {
        Optional<Customer> customer = customerService.getCustomerByUsername(username);
        return customer.map(c -> ResponseEntity.ok(c.getRole().name()))
                .orElse(ResponseEntity.notFound().build());
    }
}
