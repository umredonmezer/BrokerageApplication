package com.example.BrokerageApplication.service;

import com.example.BrokerageApplication.model.Asset;
import com.example.BrokerageApplication.repository.AssetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AssetService {

    private final AssetRepository assetRepository;

    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public List<Asset> getCustomerAssets(Long customerId) {
        return assetRepository.findByCustomerId(customerId);
    }

    public Optional<Asset> getCustomerAsset(Long customerId, String assetName) {
        return assetRepository.findByCustomerIdAndAssetName(customerId, assetName);
    }

    @Transactional
    public void updateAssetSize(Long customerId, String assetName, int sizeChange) {
        Asset asset = assetRepository.findByCustomerIdAndAssetName(customerId, assetName)
                .orElseThrow(() -> new RuntimeException("Asset not found"));

        asset.setUsableSize(asset.getUsableSize() + sizeChange);
        assetRepository.save(asset);
    }
}
