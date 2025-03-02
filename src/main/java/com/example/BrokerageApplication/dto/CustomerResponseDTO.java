package com.example.BrokerageApplication.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerResponseDTO {
    private Long id;
    private String username;
    private boolean isAdmin;
}
