package com.example.BrokerageApplication.service;

import com.example.BrokerageApplication.model.Customer;
import com.example.BrokerageApplication.model.Role;
import com.example.BrokerageApplication.repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Customer> getCustomerByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    public boolean isAdmin(String username) {
        return customerRepository.findByUsername(username)
                .map(customer -> customer.getRole() == Role.ADMIN)
                .orElse(false);
    }

    public Customer registerCustomer(String username, String password, String fullName, boolean isAdmin) {
        if (customerRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists!");
        }

        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setPassword(passwordEncoder.encode(password));
        customer.setFullName(fullName);
        customer.setRole(isAdmin ? Role.ADMIN : Role.CUSTOMER);

        return customerRepository.save(customer);
    }
}
