package com.example.BrokerageApplication.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.BrokerageApplication.model.Asset;
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
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AssetService assetService;

    @InjectMocks
    private OrderService orderService;

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order(1L, 101L, "BTC", OrderSide.BUY, 10, 50000.0, OrderStatus.PENDING, LocalDateTime.now());
    }

    @Test
    void testCreateOrderSuccess() {

        double totalCost = 10 * 50000.0;

        Asset tryBalance = new Asset(101L, "TRY", 1000000, 1000000);
        when(assetService.getCustomerAsset(anyLong(), eq("TRY"))).thenReturn(Optional.of(tryBalance));

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order result = orderService.createOrder(101L, "BTC", OrderSide.BUY, 10, 50000.0);

        assertNotNull(result);
        assertEquals(OrderStatus.PENDING, result.getStatus());
        verify(orderRepository, times(1)).save(any(Order.class));
    }


    @Test
    void testGetOrders() {
        List<Order> orders = Arrays.asList(order);
        when(orderRepository.findByCustomerIdAndCreateDateBetween(eq(101L), any(), any())).thenReturn(orders);
        List<Order> result = orderService.getOrders(101L, LocalDateTime.now().minusDays(1), LocalDateTime.now());
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findByCustomerIdAndCreateDateBetween(anyLong(), any(), any());
    }

    @Test
    void testCancelOrderSuccess() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        doNothing().when(assetService).updateAssetSize(anyLong(), anyString(), anyInt());

        orderService.cancelOrder(1L);

        assertEquals(OrderStatus.CANCELED, order.getStatus());
        verify(orderRepository, times(1)).save(order);
        verify(assetService, times(1)).updateAssetSize(anyLong(), anyString(), anyInt());
    }

    @Test
    void testCancelOrderNotFound() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> orderService.cancelOrder(99L));
    }
}
