package com.example.finalproject.repository;

import com.example.finalproject.entity.AgentPackageSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgentPackageSubscriptionRepository extends JpaRepository<AgentPackageSubscription, Integer> {
    Optional<AgentPackageSubscription> findTopByAccountIdOrderByIdDesc(Integer accountId);
}
