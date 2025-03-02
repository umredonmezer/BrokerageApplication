package com.example.BrokerageApplication.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.BrokerageApplication.model.Order;
import com.example.BrokerageApplication.model.OrderSide;
import com.example.BrokerageApplication.model.OrderStatus;
import com.example.BrokerageApplication.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AssetService assetService;

    @InjectMocks
    private AdminService adminService;

    private List<Order> pendingOrders;

    @BeforeEach
    void setUp() {
        pendingOrders = Arrays.asList(
                new Order(1L, 101L, "BTC", OrderSide.BUY, 10, 50000.0, OrderStatus.PENDING, LocalDateTime.now()),
                new Order(2L, 102L, "ETH", OrderSide.SELL, 5, 2500.0, OrderStatus.PENDING, LocalDateTime.now())
        );
    }

    @Test
    void testGetPendingOrders() {
        when(orderRepository.findByStatus(OrderStatus.PENDING)).thenReturn(pendingOrders);
        List<Order> result = adminService.getPendingOrders();
        assertEquals(2, result.size());
        verify(orderRepository, times(1)).findByStatus(OrderStatus.PENDING);
    }

    @Test
    void testMatchPendingOrders() {
        when(orderRepository.findByStatus(OrderStatus.PENDING)).thenReturn(pendingOrders);


        doNothing().when(assetService).updateAssetSize(anyLong(), anyString(), anyInt());

        adminService.matchPendingOrders();

        for (Order order : pendingOrders) {
            assertEquals(OrderStatus.MATCHED, order.getStatus());
        }

        verify(orderRepository, times(1)).findByStatus(OrderStatus.PENDING);
        verify(orderRepository, times(2)).save(any(Order.class));


        verify(assetService, times(2)).updateAssetSize(anyLong(), anyString(), anyInt());
    }
}
