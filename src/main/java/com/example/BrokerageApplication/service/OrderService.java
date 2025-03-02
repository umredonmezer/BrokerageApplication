package com.example.BrokerageApplication.service;

import com.example.BrokerageApplication.model.Asset;
import com.example.BrokerageApplication.model.Order;
import com.example.BrokerageApplication.model.OrderSide;
import com.example.BrokerageApplication.model.OrderStatus;
import com.example.BrokerageApplication.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final AssetService assetService;

    public OrderService(OrderRepository orderRepository, AssetService assetService) {
        this.orderRepository = orderRepository;
        this.assetService = assetService;
    }

    @Transactional
    public Order createOrder(Long customerId, String assetName, OrderSide side, int size, double price) {
        if (side == OrderSide.BUY) {
            Asset tryAsset = assetService.getCustomerAsset(customerId, "TRY")
                    .orElseThrow(() -> new RuntimeException("Customer does not have TRY balance"));

            double totalCost = size * price;
            if (tryAsset.getUsableSize() < totalCost) {
                throw new RuntimeException("Insufficient TRY balance to place this order");
            }


            assetService.updateAssetSize(customerId, "TRY", (int) -totalCost);
        } else {
            Asset asset = assetService.getCustomerAsset(customerId, assetName)
                    .orElseThrow(() -> new RuntimeException("Customer does not own this asset"));

            if (asset.getUsableSize() < size) {
                throw new RuntimeException("Not enough asset size to sell");
            }

            assetService.updateAssetSize(customerId, assetName, -size);
        }

        Order order = new Order();
        order.setCustomerId(customerId);
        order.setAssetName(assetName);
        order.setOrderSide(side);
        order.setSize(size);
        order.setPrice(price);
        order.setStatus(OrderStatus.PENDING);
        order.setCreateDate(LocalDateTime.now());

        return orderRepository.save(order);
    }

    public List<Order> getOrders(Long customerId, LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByCustomerIdAndCreateDateBetween(customerId, startDate, endDate);
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Only PENDING orders can be canceled");
        }

        if (order.getOrderSide() == OrderSide.BUY) {
            assetService.updateAssetSize(order.getCustomerId(), "TRY", (int) (order.getSize() * order.getPrice()));
        } else {
            assetService.updateAssetSize(order.getCustomerId(), order.getAssetName(), order.getSize());
        }

        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }
}
