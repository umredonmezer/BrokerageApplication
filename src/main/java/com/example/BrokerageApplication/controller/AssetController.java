package com.example.BrokerageApplication.controller;

import com.example.BrokerageApplication.model.Asset;
import com.example.BrokerageApplication.model.Customer;
import com.example.BrokerageApplication.repository.AssetRepository;
import com.example.BrokerageApplication.repository.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customer/assets")
public class AssetController {
    private final AssetRepository assetRepository;
    private final CustomerRepository customerRepository;

    public AssetController(AssetRepository assetRepository, CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.assetRepository = assetRepository;
    }





    @GetMapping("/{username}")
    public ResponseEntity<List<Asset>> getCustomerAssets(@PathVariable String username) {
        Optional<Customer> customerOpt = customerRepository.findByUsername(username);

        if (customerOpt.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        Customer customer = customerOpt.get();
        Long customerId = customer.getId();

        if (customerId == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        List<Asset> assets = assetRepository.findByCustomerId(customerId);

        boolean hasTryAsset = assets.stream().anyMatch(a -> a.getAssetName().equals("TRY"));
        if (!hasTryAsset) {
            Asset tryAsset = new Asset(customerId, "TRY", 10000.0, 10000.0);
            assetRepository.save(tryAsset);
            assets.add(tryAsset);
        }

        return ResponseEntity.ok(assets);
    }


    @PostMapping
    public ResponseEntity<?> createAsset(@RequestBody Asset asset) {
        Optional<Customer> customerOpt = customerRepository.findById(asset.getCustomerId());

        if (customerOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Customer does not exist.");
        }

        Optional<Asset> existingAsset = assetRepository.findByCustomerIdAndAssetName(asset.getCustomerId(), asset.getAssetName());
        if (existingAsset.isPresent()) {
            return ResponseEntity.badRequest().body("Asset already exists for this customer.");
        }

        assetRepository.save(asset);
        return ResponseEntity.ok("Asset created successfully.");
    }
}
