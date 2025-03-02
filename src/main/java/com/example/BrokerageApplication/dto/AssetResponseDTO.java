package com.example.BrokerageApplication.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssetResponseDTO {
    private String assetName;
    private int size;
    private int usableSize;
}
