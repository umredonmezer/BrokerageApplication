package com.example.BrokerageApplication.dto;


import com.example.BrokerageApplication.model.OrderSide;
import com.example.BrokerageApplication.model.OrderStatus;
import java.time.LocalDateTime;

public class OrderResponseDTO {
    private Long id;
    private Long customerId;
    private String assetName;
    private OrderSide orderSide;
    private double size;
    private double price;
    private OrderStatus status;
    private LocalDateTime createDate;

    public OrderResponseDTO(Long id, Long customerId, String assetName, OrderSide orderSide, double size, double price, OrderStatus status, LocalDateTime createDate) {
        this.id = id;
        this.customerId = customerId;
        this.assetName = assetName;
        this.orderSide = orderSide;
        this.size = size;
        this.price = price;
        this.status = status;
        this.createDate = createDate;
    }

    public OrderResponseDTO() {}


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public OrderSide getOrderSide() {
        return orderSide;
    }

    public void setOrderSide(OrderSide orderSide) {
        this.orderSide = orderSide;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
}
