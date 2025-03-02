package com.example.BrokerageApplication.controller;

import com.example.BrokerageApplication.model.Customer;
import com.example.BrokerageApplication.model.Role;
import com.example.BrokerageApplication.repository.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        String fullName = request.get("fullName");
        String roleString = request.get("role");

        if (username == null || password == null || fullName == null || roleString == null) {
            return ResponseEntity.badRequest().body("All fields (username, password, fullName, role) are required.");
        }

        if (customerRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        Role role;
        try {
            role = Role.valueOf(roleString.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid role! Use 'ADMIN' or 'CUSTOMER'.");
        }

        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setPassword(passwordEncoder.encode(password));
        customer.setFullName(fullName);
        customer.setRole(role);

        customerRepository.save(customer);
        return ResponseEntity.ok("User registered successfully");
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        if (username == null || password == null) {
            return ResponseEntity.badRequest().body("Username and password are required.");
        }

        Optional<Customer> customerOpt = customerRepository.findByUsername(username);
        if (customerOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }

        Customer customer = customerOpt.get();
        if (!passwordEncoder.matches(password, customer.getPassword())) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }

        return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "username", customer.getUsername(),
                "role", customer.getRole().name()
        ));
    }
}
