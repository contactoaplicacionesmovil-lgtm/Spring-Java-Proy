package org.example.finalspringproject.repository;

import org.example.finalspringproject.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProductIdAndStoreId(Long productId, Long storeId);
    List<Inventory> findByStoreId(Long storeId);
    void deleteByProductId(Long productId);
}
