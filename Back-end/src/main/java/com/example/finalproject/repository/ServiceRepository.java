package com.example.finalproject.repository;

import com.example.finalproject.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Integer> {
    List<ServiceEntity> findByAgentId(Integer agentId);
    Optional<ServiceEntity> findByServiceIdAndAgentId(Integer serviceId, Integer agentId);
    boolean existsByServiceIdAndAgentId(Integer serviceId, Integer agentId);
}
