package com.example.BrokerageApplication.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.BrokerageApplication.model.Customer;
import com.example.BrokerageApplication.model.Role;
import com.example.BrokerageApplication.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer(1L, "testuser", "encodedPass", "Test User", Role.CUSTOMER);
    }


    @Test
    void testAuthenticateSuccess() {
        when(customerRepository.findByUsername("testuser")).thenReturn(Optional.of(customer));
        when(passwordEncoder.matches("rawPass", "encodedPass")).thenReturn(true);
        Optional<Customer> result = authService.authenticate("testuser", "rawPass");
        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
    }

    @Test
    void testAuthenticateWrongPassword() {
        when(customerRepository.findByUsername("testuser")).thenReturn(Optional.of(customer));
        when(passwordEncoder.matches("wrongPass", "encodedPass")).thenReturn(false);
        Optional<Customer> result = authService.authenticate("testuser", "wrongPass");
        assertFalse(result.isPresent());
    }

    @Test
    void testAuthenticateUserNotFound() {
        when(customerRepository.findByUsername("unknown"))
                .thenReturn(Optional.empty());
        Optional<Customer> result = authService.authenticate("unknown", "pass");
        assertFalse(result.isPresent());
    }
}
