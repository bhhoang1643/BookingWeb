package com.example.finalproject.controller;

import com.example.finalproject.dto.AgentPackageDTO;
import com.example.finalproject.service.AgentPackageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/agent-package")
@CrossOrigin(origins = "http://localhost:4200")
public class AgentPackageController {

    private final AgentPackageService agentPackageService;

    public AgentPackageController(AgentPackageService agentPackageService) {
        this.agentPackageService = agentPackageService;
    }


    @PostMapping("/subscribe")
    public ResponseEntity<AgentPackageDTO> subscribe(@RequestBody Map<String, Object> request) {
        try {
            Integer accountId = (Integer) request.get("accountId");
            String name = (String) request.get("name");
            BigDecimal price = new BigDecimal(request.get("price").toString());
            Integer duration = (Integer) request.get("duration");

            AgentPackageDTO dto = agentPackageService.createSubscription(accountId, name, price, duration);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    @GetMapping("/remaining/{accountId}")
    public ResponseEntity<?> getRemainingTime(@PathVariable Integer accountId) {
        try {
            Map<String, Object> result = agentPackageService.getRemainingTime(accountId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("\u274c " + e.getMessage());
        }
    }


    @PostMapping("/confirm-payment")
    public ResponseEntity<String> confirmPayment(@RequestParam Integer subscriptionId) {
        try {
            agentPackageService.activateAgentPackage(subscriptionId);
            return ResponseEntity.ok("\u2705 Agent successfully paid and activated!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("\u274c " + e.getMessage());
        }
    }


    @GetMapping("/list")
    public ResponseEntity<List<AgentPackageDTO>> listAll() {
        return ResponseEntity.ok(agentPackageService.getAllSubscriptions());
    }
}
