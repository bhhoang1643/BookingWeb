package com.example.finalproject.controller;

import com.example.finalproject.config.JwtUtils;
import com.example.finalproject.dto.ShopDTO;
import com.example.finalproject.entity.Agent;
import com.example.finalproject.repository.AgentRepository;
import com.example.finalproject.service.ShopService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/shops")
@CrossOrigin(origins = "http://localhost:4200")
public class ShopController {

    private final ShopService shopService;
    private final AgentRepository agentRepository;
    private final JwtUtils jwtUtils;

    public ShopController(ShopService shopService, AgentRepository agentRepository, JwtUtils jwtUtils) {
        this.shopService = shopService;
        this.agentRepository = agentRepository;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping
    public ResponseEntity<List<ShopDTO>> getAllShops() {
        return ResponseEntity.ok(shopService.getAllShops());
    }



    @GetMapping("/my")
    public ResponseEntity<List<ShopDTO>> getMyShops(@RequestHeader("Authorization") String authHeader) {
        Integer accountId = jwtUtils.extractAccountId(authHeader.replace("Bearer ", ""));
        return ResponseEntity.ok(shopService.getShopsByAccountId(accountId));
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getShopById(@PathVariable Integer id, @RequestHeader("Authorization") String authHeader) {
        Integer accountId = jwtUtils.extractAccountId(authHeader.replace("Bearer ", ""));
        Optional<ShopDTO> shop = shopService.getShopByIdAndAccount(id, accountId);
        return shop.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body("❌ Shop does not exist or is not accessible!"));
    }


    @PostMapping
    public ResponseEntity<?> createShop(@RequestBody ShopDTO shopDTO, @RequestHeader("Authorization") String authHeader) {
        Integer accountId = jwtUtils.extractAccountId(authHeader.replace("Bearer ", ""));
        Optional<Agent> agent = agentRepository.findByAccount_AccountId(accountId);
        if (agent.isEmpty()) {
            return ResponseEntity.status(400).body("❌ Agent not found from Account ID.");
        }
        ShopDTO created = shopService.createShopWithAgent(shopDTO, agent.get());
        return ResponseEntity.status(201).body(created);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateShop(@PathVariable Integer id, @RequestBody ShopDTO shopDTO,
                                        @RequestHeader("Authorization") String authHeader) {
        Integer accountId = jwtUtils.extractAccountId(authHeader.replace("Bearer ", ""));
        ShopDTO updated = shopService.updateShopByAccount(id, shopDTO, accountId);
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteShop(@PathVariable Integer id, @RequestHeader("Authorization") String authHeader) {
        Integer accountId = jwtUtils.extractAccountId(authHeader.replace("Bearer ", ""));
        shopService.deleteShopByAccount(id, accountId);
        return ResponseEntity.ok("✅ Agent not found from Account ID!");
    }
    @GetMapping("/agent/{agentId}")
    public ResponseEntity<List<ShopDTO>> getShopsByAgentId(@PathVariable Integer agentId) {
        List<ShopDTO> shops = shopService.getShopsByAgent(agentId);
        return ResponseEntity.ok(shops);
    }

}
