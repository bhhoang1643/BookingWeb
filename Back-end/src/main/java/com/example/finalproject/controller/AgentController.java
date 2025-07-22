package com.example.finalproject.controller;

import com.example.finalproject.config.JwtUtils;
import com.example.finalproject.dto.AgentDTO;
import com.example.finalproject.entity.Agent;
import com.example.finalproject.service.AgentService;
import com.example.finalproject.repository.AgentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/agents")
public class AgentController {

    private final AgentService agentService;
    private final JwtUtils jwtUtils;
    private final AgentRepository agentRepository;

    public AgentController(AgentService agentService, JwtUtils jwtUtils, AgentRepository agentRepository) {
        this.agentService = agentService;
        this.jwtUtils = jwtUtils;
        this.agentRepository = agentRepository;
    }

    @GetMapping
    public ResponseEntity<List<AgentDTO>> getAllAgents() {
        return ResponseEntity.ok(agentService.getAllAgents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgentDTO> getAgentById(@PathVariable Integer id) {
        return ResponseEntity.ok(agentService.getAgentById(id));
    }

    @PostMapping
    public ResponseEntity<AgentDTO> createAgent(@RequestBody AgentDTO agentDTO) {
        return ResponseEntity.status(201).body(agentService.createAgent(agentDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgentDTO> updateAgent(@PathVariable Integer id, @RequestBody AgentDTO agentDTO) {
        return ResponseEntity.ok(agentService.updateAgent(id, agentDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgent(@PathVariable Integer id) {
        agentService.deleteAgent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyAgent(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Integer accountId = jwtUtils.extractAccountId(token);
        Optional<Agent> optionalAgent = agentRepository.findByAccount_AccountId(accountId);

        if (optionalAgent.isPresent()) {
            AgentDTO agentDTO = agentService.getAgentById(optionalAgent.get().getId());
            return ResponseEntity.ok(agentDTO);
        } else {
            return ResponseEntity.status(404).body("❌ Agent not found from Account ID.");
        }
    }
}
