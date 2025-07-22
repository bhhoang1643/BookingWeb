package com.example.finalproject.repository;

import com.example.finalproject.entity.CustomerLoyalty;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CustomerLoyaltyRepository extends JpaRepository<CustomerLoyalty, Integer> {
    List<CustomerLoyalty> findByCustomer_Id(Integer customerId);
}
