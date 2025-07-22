package com.example.finalproject.controller;

import com.example.finalproject.dto.CustomerLoyaltyDTO;
import com.example.finalproject.service.CustomerLoyaltyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loyalty")
@CrossOrigin(origins = "*")
public class CustomerLoyaltyController {

    private final CustomerLoyaltyService loyaltyService;

    public CustomerLoyaltyController(CustomerLoyaltyService loyaltyService) {
        this.loyaltyService = loyaltyService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerLoyaltyDTO>> getAll() {
        return ResponseEntity.ok(loyaltyService.getAll());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CustomerLoyaltyDTO>> getByCustomer(@PathVariable Integer customerId) {
        return ResponseEntity.ok(loyaltyService.getByCustomerId(customerId));
    }

    @PostMapping
    public ResponseEntity<CustomerLoyaltyDTO> create(@RequestBody CustomerLoyaltyDTO dto) {
        return ResponseEntity.ok(loyaltyService.create(dto));
    }
}
