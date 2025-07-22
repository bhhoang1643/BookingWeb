package com.example.finalproject.service;

import com.example.finalproject.dto.AccountDTO;
import com.example.finalproject.dto.AgentDTO;
import com.example.finalproject.entity.Account;
import com.example.finalproject.entity.Agent;
import com.example.finalproject.repository.AccountRepository;
import com.example.finalproject.repository.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgentService {

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private AccountRepository accountRepository;

    public List<AgentDTO> getAllAgents() {
        return agentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public AgentDTO getAgentById(Integer id) {
        return agentRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new RuntimeException("❌ Agent not found"));
    }

    public AgentDTO createAgent(AgentDTO agentDTO) {
        Account account = accountRepository.findById(agentDTO.getAccountId())
                .orElseThrow(() -> new RuntimeException("❌ Account not found"));

        Agent agent = new Agent(
                agentDTO.getSpecialization(),
                agentDTO.getLocation(),
                agentDTO.getEstablishment(),
                agentDTO.getOpeningHours(),
                agentDTO.getProfessionalSkills(),
                agentDTO.getOwnerName(),
                agentDTO.getAgentName(),
                account
        );

        return convertToDto(agentRepository.save(agent));
    }

    public AgentDTO updateAgent(Integer id, AgentDTO agentDTO) {
        Agent agent = agentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Agent not found with ID: " + id));

        agent.setSpecialization(agentDTO.getSpecialization());
        agent.setLocation(agentDTO.getLocation());
        agent.setEstablishment(agentDTO.getEstablishment());
        agent.setOpeningHours(agentDTO.getOpeningHours());
        agent.setProfessionalSkills(agentDTO.getProfessionalSkills());
        agent.setOwnerName(agentDTO.getOwnerName());
        agent.setAgentName(agentDTO.getAgentName());

        return convertToDto(agentRepository.save(agent));
    }

    public void deleteAgent(Integer id) {
        if (!agentRepository.existsById(id)) {
            throw new RuntimeException("❌ Agent not found with ID: " + id);
        }
        agentRepository.deleteById(id);
    }

    private AgentDTO convertToDto(Agent agent) {
        Account account = agent.getAccount();

        AccountDTO accountDTO = new AccountDTO(
                account.getAccountId(),
                account.getUsername(),
                account.getEmail(),
                account.getPhoneNumber()
        );

        return new AgentDTO(
                agent.getId(),
                agent.getSpecialization(),
                agent.getLocation(),
                agent.getEstablishment(),
                agent.getOpeningHours(),
                agent.getProfessionalSkills(),
                agent.getOwnerName(),
                agent.getAgentName(),
                account.getAccountId(),
                accountDTO
        );
    }
}
