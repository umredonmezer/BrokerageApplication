package com.example.BrokerageApplication.repository;

import com.example.BrokerageApplication.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findByCustomerId(Long customerId);
    Optional<Asset> findByCustomerIdAndAssetName(Long customerId, String assetName);
}
