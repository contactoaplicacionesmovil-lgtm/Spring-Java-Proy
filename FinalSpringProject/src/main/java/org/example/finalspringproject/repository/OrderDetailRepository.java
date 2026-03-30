package org.example.finalspringproject.repository;

import org.example.finalspringproject.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    void deleteByOrderId(Long orderId);
}
