package com.example.finalproject.controller;

import com.example.finalproject.dto.OrderDetailDTO;
import com.example.finalproject.service.OrderDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-details")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    public OrderDetailController(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }


    @PostMapping("/{orderId}/product/{productId}/{quantity}")
    public ResponseEntity<OrderDetailDTO> addOrderDetail(
            @PathVariable Integer orderId,
            @PathVariable Integer productId,
            @PathVariable Integer quantity) {
        return ResponseEntity.ok(orderDetailService.addOrderDetail(orderId, productId, quantity));
    }



    @PutMapping("/{id}/{quantity}")
    public ResponseEntity<OrderDetailDTO> updateOrderDetail(
            @PathVariable Integer id,
            @PathVariable Integer quantity) {
        return ResponseEntity.ok(orderDetailService.updateOrderDetail(id, quantity));
    }


    @GetMapping
    public ResponseEntity<List<OrderDetailDTO>> getAllOrderDetails() {
        return ResponseEntity.ok(orderDetailService.getAllOrderDetails());
    }


    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderDetailDTO>> getOrderDetailsByOrderId(@PathVariable Integer orderId) {
        return ResponseEntity.ok(orderDetailService.getOrderDetailsByOrderId(orderId));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderDetail(@PathVariable Integer id) {
        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.ok("✅ OrderDetail has been deleted!");
    }
}
