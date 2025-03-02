package com.example.BrokerageApplication.service;

import com.example.BrokerageApplication.model.Order;
import com.example.BrokerageApplication.model.OrderStatus;
import com.example.BrokerageApplication.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final OrderRepository orderRepository;
    private final AssetService assetService;

    public AdminService(OrderRepository orderRepository, AssetService assetService) {
        this.orderRepository = orderRepository;
        this.assetService = assetService;
    }
    public List<Order> getPendingOrders() {
        return orderRepository.findByStatus(OrderStatus.PENDING);
    }

    @Transactional
    public void matchPendingOrders() {
        List<Order> pendingOrders = orderRepository.findByStatus(OrderStatus.PENDING);

        for (Order order : pendingOrders) {
            assetService.updateAssetSize(order.getCustomerId(), order.getAssetName(), order.getSize());
            order.setStatus(OrderStatus.MATCHED);
            orderRepository.save(order);
        }
    }
}
