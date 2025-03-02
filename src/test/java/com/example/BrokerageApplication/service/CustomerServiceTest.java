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
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer(1L, "testuser", "encodedPass", "Test User", Role.CUSTOMER);

    }

    @Test
    void testGetCustomerByUsernameFound() {
        when(customerRepository.findByUsername("testuser")).thenReturn(Optional.of(customer));
        Optional<Customer> result = customerService.getCustomerByUsername("testuser");
        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
    }

    @Test
    void testGetCustomerByUsernameNotFound() {
        when(customerRepository.findByUsername("unknown"))
                .thenReturn(Optional.empty());
        Optional<Customer> result = customerService.getCustomerByUsername("unknown");
        assertFalse(result.isPresent());
    }

    @Test
    void testRegisterCustomerSuccess() {
        when(customerRepository.existsByUsername("newuser")).thenReturn(false);
        when(passwordEncoder.encode("rawPass")).thenReturn("hashedPass");
        customerService.registerCustomer("newuser", "rawPass", "New User", false);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testRegisterCustomerAlreadyExists() {
        when(customerRepository.existsByUsername("testuser")).thenReturn(true);
        assertThrows(RuntimeException.class, () ->
                customerService.registerCustomer("testuser", "pass", "Test User", false));
    }
}

