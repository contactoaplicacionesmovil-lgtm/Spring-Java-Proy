package org.example.finalspringproject.repository;

import org.example.finalspringproject.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
