package com.example.finalproject.controller;

import com.example.finalproject.config.JwtUtils;
import com.example.finalproject.dto.OrderDTO;
import com.example.finalproject.entity.Agent;
import com.example.finalproject.service.OrderService;
import com.example.finalproject.repository.AgentRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {

    private final OrderService orderService;
    private final JwtUtils jwtUtils;
    private final AgentRepository agentRepository;

    public OrderController(OrderService orderService, JwtUtils jwtUtils, AgentRepository agentRepository) {
        this.orderService = orderService;
        this.jwtUtils = jwtUtils;
        this.agentRepository = agentRepository;
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Integer id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByCustomerId(@PathVariable Integer customerId) {
        return ResponseEntity.ok(orderService.getOrdersByCustomerId(customerId));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<List<OrderDTO>> getOrdersByCurrentAgent(HttpServletRequest request) {
        Integer accountId = jwtUtils.extractAccountIdFromRequest(request);
        Agent agent = agentRepository.findByAccount_AccountId(accountId)
                .orElseThrow(() -> new RuntimeException("❌Agent does not exist!"));
        return ResponseEntity.ok(orderService.getOrdersByAgentId(agent.getId()));
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        return ResponseEntity.status(201).body(orderService.createOrder(orderDTO));
    }
    @PutMapping("/order-payments/{id}/confirm")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<?> confirmOrderPayment(@PathVariable Integer id) {
        orderService.confirmPayment(id);
        return ResponseEntity.ok("✅ Order has been confirmed payment!");
    }


    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Integer id, @RequestBody OrderDTO orderDTO) {
        return ResponseEntity.ok(orderService.updateOrder(id, orderDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Integer id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("✅ Order has been deleted successfully!");
    }
}
