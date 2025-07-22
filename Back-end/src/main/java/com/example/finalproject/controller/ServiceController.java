package com.example.finalproject.controller;

import com.example.finalproject.config.JwtUtils;
import com.example.finalproject.dto.ServiceDTO;
import com.example.finalproject.entity.Agent;
import com.example.finalproject.repository.AgentRepository;
import com.example.finalproject.service.ImgurService;
import com.example.finalproject.service.ServiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/services")
@CrossOrigin(origins = "http://localhost:4200")
public class ServiceController {

    private final ServiceService serviceService;
    private final AgentRepository agentRepository;
    private final JwtUtils jwtUtils;
    private final ImgurService imgurService;

    public ServiceController(ServiceService serviceService, AgentRepository agentRepository, JwtUtils jwtUtils, ImgurService imgurService) {
        this.serviceService = serviceService;
        this.agentRepository = agentRepository;
        this.jwtUtils = jwtUtils;
        this.imgurService = imgurService;
    }

    @GetMapping("/my")
    public ResponseEntity<List<ServiceDTO>> getMyServices(@RequestHeader("Authorization") String authHeader) {
        Integer accountId = jwtUtils.extractAccountId(authHeader.replace("Bearer ", ""));
        return ResponseEntity.ok(serviceService.getServicesByAccountId(accountId));
    }

    @GetMapping("/agent/{agentId}")
    public ResponseEntity<List<ServiceDTO>> getServicesByAgentId(@PathVariable Integer agentId) {
        return ResponseEntity.ok(serviceService.getServicesByAgentId(agentId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id, @RequestHeader("Authorization") String authHeader) {
        Integer accountId = jwtUtils.extractAccountId(authHeader.replace("Bearer ", ""));
        Optional<ServiceDTO> service = serviceService.getServiceByIdAndAccount(id, accountId);
        return service.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body("❌ Service does not exist or is not accessible!"));
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> create(@RequestParam("name") String name,
                                    @RequestParam("price") BigDecimal price,
                                    @RequestParam("status") String status,
                                    @RequestParam(value = "file", required = false) MultipartFile file,
                                    @RequestHeader("Authorization") String authHeader) throws IOException {
        Integer accountId = jwtUtils.extractAccountId(authHeader.replace("Bearer ", ""));
        Optional<Agent> agent = agentRepository.findByAccount_AccountId(accountId);
        if (agent.isEmpty()) {
            return ResponseEntity.badRequest().body("❌ Agent not found from Account ID!");
        }

        String imageUrl = (file != null && !file.isEmpty()) ? imgurService.uploadToImgur(file) : null;
        ServiceDTO dto = new ServiceDTO(null, agent.get().getId(), name, price, status, imageUrl);
        ServiceDTO created = serviceService.createServiceWithAgent(dto, agent.get());
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<?> update(@PathVariable Integer id,
                                    @RequestParam("name") String name,
                                    @RequestParam("price") BigDecimal price,
                                    @RequestParam("status") String status,
                                    @RequestParam(value = "file", required = false) MultipartFile file,
                                    @RequestHeader("Authorization") String authHeader) throws IOException {
        Integer accountId = jwtUtils.extractAccountId(authHeader.replace("Bearer ", ""));
        ServiceDTO dto = new ServiceDTO(null, null, name, price, status, null);
        return ResponseEntity.ok(serviceService.updateService(id, dto, file, accountId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id, @RequestHeader("Authorization") String authHeader) {
        Integer accountId = jwtUtils.extractAccountId(authHeader.replace("Bearer ", ""));
        serviceService.deleteServiceByAccount(id, accountId);
        return ResponseEntity.ok("✅ Service has been removed!");
    }
    @GetMapping("/agents-with-services/all")
    public ResponseEntity<List<Map<String, Object>>> getAgentsWithServices() {
        List<Agent> agents = agentRepository.findAll();
        List<Map<String, Object>> result = agents.stream().map(agent -> {
            Map<String, Object> map = new HashMap<>();
            map.put("agentId", agent.getId());
            map.put("agentName", agent.getAgentName());
            map.put("services", serviceService.getServicesByAgentId(agent.getId()));
            return map;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }


}