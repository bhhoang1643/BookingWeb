package com.example.finalproject.service;

import com.example.finalproject.dto.OrderDetailDTO;
import com.example.finalproject.entity.Order;
import com.example.finalproject.entity.OrderDetail;
import com.example.finalproject.entity.Product;
import com.example.finalproject.repository.OrderDetailRepository;
import com.example.finalproject.repository.OrderRepository;
import com.example.finalproject.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderDetailService(OrderDetailRepository orderDetailRepository, OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderDetailRepository = orderDetailRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public OrderDetailDTO addOrderDetail(Integer orderId, Integer productId, Integer quantity) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("❌ Order does not exist!"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("❌ Product does not exist!"));

        OrderDetail orderDetail = new OrderDetail(order, product, quantity);
        order.getOrderDetails().add(orderDetail);
        orderDetail = orderDetailRepository.save(orderDetail);
        updateOrderTotalPrice(order);

        return convertToDTO(orderDetail);
    }

    @Transactional
    public OrderDetailDTO updateOrderDetail(Integer id, Integer quantity) {
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ OrderDetail does not exist!"));

        orderDetail.setQuantity(quantity);
        orderDetail = orderDetailRepository.save(orderDetail);
        updateOrderTotalPrice(orderDetail.getOrder());

        return convertToDTO(orderDetail);
    }

    public List<OrderDetailDTO> getAllOrderDetails() {
        return orderDetailRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<OrderDetailDTO> getOrderDetailsByOrderId(Integer orderId) {
        return orderDetailRepository.findByOrder_Id(orderId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteOrderDetail(Integer id) {
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ OrderDetail does not exist!"));

        Order order = orderDetail.getOrder();
        order.getOrderDetails().removeIf(od -> od.getId().equals(id));
        orderDetailRepository.delete(orderDetail);
        updateOrderTotalPrice(order);
    }

    private void updateOrderTotalPrice(Order order) {
        if (order == null || order.getOrderDetails() == null) return;

        BigDecimal newTotalPrice = order.getOrderDetails().stream()
                .map(detail -> detail.getProduct().getPrice().multiply(BigDecimal.valueOf(detail.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (!order.getTotalPrice().equals(newTotalPrice)) {
            order.setTotalPrice(newTotalPrice);
            orderRepository.save(order);
        }
    }


    private OrderDetailDTO convertToDTO(OrderDetail orderDetail) {
        OrderDetailDTO dto = new OrderDetailDTO(
                orderDetail.getId(),
                orderDetail.getOrder().getId(),
                orderDetail.getProduct().getId(),
                orderDetail.getQuantity(),
                orderDetail.getProduct().getPrice().multiply(BigDecimal.valueOf(orderDetail.getQuantity()))
        );


        dto.setProductName(orderDetail.getProduct().getName());
        dto.setCategoryName(orderDetail.getProduct().getCategory().getName());
        dto.setPrice(orderDetail.getProduct().getPrice());

        return dto;
    }
}
