package com.example.finalproject.service;

import com.example.finalproject.dto.ServiceDTO;
import com.example.finalproject.entity.Agent;
import com.example.finalproject.entity.ServiceEntity;
import com.example.finalproject.repository.AgentRepository;
import com.example.finalproject.repository.ServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final AgentRepository agentRepository;
    private final ImgurService imgurService;

    public ServiceService(ServiceRepository serviceRepository, AgentRepository agentRepository, ImgurService imgurService) {
        this.serviceRepository = serviceRepository;
        this.agentRepository = agentRepository;
        this.imgurService = imgurService;
    }

    private Agent getAgentByAccount(Integer accountId) {
        return agentRepository.findByAccount_AccountId(accountId)
                .orElseThrow(() -> new RuntimeException("❌ Agent not found from Account ID!"));
    }

    public List<ServiceDTO> getServicesByAccountId(Integer accountId) {
        Agent agent = getAgentByAccount(accountId);
        return serviceRepository.findByAgentId(agent.getId())
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ServiceDTO> getServicesByAgentId(Integer agentId) {
        return serviceRepository.findByAgentId(agentId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<ServiceDTO> getServiceByIdAndAccount(Integer id, Integer accountId) {
        Agent agent = getAgentByAccount(accountId);
        return serviceRepository.findByServiceIdAndAgentId(id, agent.getId())
                .map(this::toDTO);
    }

    public ServiceDTO createServiceWithAgent(ServiceDTO dto, Agent agent) {
        ServiceEntity entity = new ServiceEntity(agent, dto.getName(), dto.getPrice(), dto.getStatus(), dto.getImage());
        return toDTO(serviceRepository.save(entity));
    }

    public ServiceDTO updateService(Integer id, ServiceDTO dto, MultipartFile file, Integer accountId) throws IOException {
        Agent agent = getAgentByAccount(accountId);
        ServiceEntity entity = serviceRepository.findByServiceIdAndAgentId(id, agent.getId())
                .orElseThrow(() -> new RuntimeException("❌ No managed service found!"));

        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setStatus(dto.getStatus());

        if (file != null && !file.isEmpty()) {
            String imageUrl = imgurService.uploadToImgur(file);
            entity.setImage(imageUrl);
        }

        return toDTO(serviceRepository.save(entity));
    }

    public void deleteServiceByAccount(Integer id, Integer accountId) {
        Agent agent = getAgentByAccount(accountId);
        boolean exists = serviceRepository.existsByServiceIdAndAgentId(id, agent.getId());
        if (!exists) {
            throw new RuntimeException("❌ No permission to delete this Service!");
        }
        serviceRepository.deleteById(id);
    }

    private ServiceDTO toDTO(ServiceEntity entity) {
        return new ServiceDTO(
                entity.getServiceId(),
                entity.getAgent().getId(),
                entity.getName(),
                entity.getPrice(),
                entity.getStatus(),
                entity.getImage()
        );
    }
}
