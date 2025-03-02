package com.example.BrokerageApplication.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.BrokerageApplication.model.Asset;
import com.example.BrokerageApplication.repository.AssetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AssetServiceTest {

    @Mock
    private AssetRepository assetRepository;

    @InjectMocks
    private AssetService assetService;

    private Asset asset;

    @BeforeEach
    void setUp() {
        asset = new Asset(1L, "BTC", 50, 50);
    }

    @Test
    void testGetCustomerAssetFound() {
        when(assetRepository.findByCustomerIdAndAssetName(1L, "BTC")).thenReturn(Optional.of(asset));
        Optional<Asset> result = assetService.getCustomerAsset(1L, "BTC");
        assertTrue(result.isPresent());
        assertEquals("BTC", result.get().getAssetName());
    }

    @Test
    void testGetCustomerAssetNotFound() {
        when(assetRepository.findByCustomerIdAndAssetName(1L, "ETH")).thenReturn(Optional.empty());
        Optional<Asset> result = assetService.getCustomerAsset(1L, "ETH");
        assertFalse(result.isPresent());
    }

    @Test
    void testUpdateAssetSizeSuccess() {
        when(assetRepository.findByCustomerIdAndAssetName(1L, "BTC")).thenReturn(Optional.of(asset));
        assetService.updateAssetSize(1L, "BTC", 10);
        assertEquals(60, asset.getUsableSize());
        verify(assetRepository, times(1)).save(asset);
    }

    @Test
    void testUpdateAssetSizeAssetNotFound() {
        when(assetRepository.findByCustomerIdAndAssetName(1L, "ETH")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> assetService.updateAssetSize(1L, "ETH", 10));
    }
}
