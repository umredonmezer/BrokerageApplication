package com.example.BrokerageApplication.dto;

import com.example.BrokerageApplication.model.OrderSide;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequestDTO {
    private Long customerId;
    private String assetName;
    private OrderSide orderSide;
    private int size;
    private double price;
}
