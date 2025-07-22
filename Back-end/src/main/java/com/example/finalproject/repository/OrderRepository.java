package com.example.finalproject.repository;

import com.example.finalproject.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("SELECT DISTINCT o FROM Order o " +
            "LEFT JOIN FETCH o.orderDetails od " +
            "LEFT JOIN FETCH od.product p " +
            "LEFT JOIN FETCH p.category " +
            "WHERE o.agentId = :agentId")
    List<Order> findByAgentIdWithDetails(@Param("agentId") Integer agentId);

    @Query("SELECT DISTINCT o FROM Order o " +
            "LEFT JOIN FETCH o.orderDetails od " +
            "LEFT JOIN FETCH od.product p " +
            "LEFT JOIN FETCH p.category " +
            "WHERE o.customer.id = :customerId")
    List<Order> findByCustomerIdWithDetails(@Param("customerId") Integer customerId);

    List<Order> findByCustomerId(Integer customerId);
    List<Order> findByAgentId(Integer agentId);
}
