package org.example.finalspringproject.service;

import org.example.finalspringproject.model.Inventory;
import org.example.finalspringproject.model.Product;
import org.example.finalspringproject.repository.InventoryRepository;
import org.example.finalspringproject.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ServiceClass {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    public ServiceClass(InventoryRepository inventoryRepository, ProductRepository productRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }

    public void validateInventory(Long productId, Long storeId) {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("Product id is required");
        }
        if (storeId == null || storeId <= 0) {
            throw new IllegalArgumentException("Store id is required");
        }

        boolean exists = inventoryRepository.findByProductIdAndStoreId(productId, storeId).isPresent();
        if (exists) {
            throw new IllegalArgumentException(
                    "Duplicate inventory for product-store pair. productId=" + productId + ", storeId=" + storeId);
        }
    }

    public void validateProduct(String productName) {
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Product name is required");
        }

        if (!productRepository.findByName(productName.trim()).isEmpty()) {
            throw new IllegalArgumentException("Product already exists with name: " + productName);
        }
    }

    public Product validateProductId(Long productId) {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("Product id is required");
        }

        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));
    }

    public Inventory getInventoryID(Long productId, Long storeId) {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("Product id is required");
        }
        if (storeId == null || storeId <= 0) {
            throw new IllegalArgumentException("Store id is required");
        }

        return inventoryRepository.findByProductIdAndStoreId(productId, storeId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Inventory not found for productId=" + productId + " and storeId=" + storeId));
    }
}
