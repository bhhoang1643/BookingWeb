package com.example.finalproject.service;

import com.example.finalproject.dto.OrderDTO;
import com.example.finalproject.dto.OrderDetailDTO;
import com.example.finalproject.entity.*;
import com.example.finalproject.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final AgentRepository agentRepository;

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository, ProductRepository productRepository, AgentRepository agentRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.agentRepository = agentRepository;
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO getOrderById(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Order does not exist"));
        return convertToDTO(order);
    }

    public List<OrderDTO> getOrdersByCustomerId(Integer customerId) {
        return orderRepository.findByCustomerIdWithDetails(customerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getOrdersByAgentId(Integer agentId) {
        return orderRepository.findByAgentIdWithDetails(agentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Customer customer = customerRepository.findById(orderDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("❌ Customer does not exist!"));

        if (orderDTO.getOrderDetails() == null || orderDTO.getOrderDetails().isEmpty()) {
            throw new RuntimeException("❌ Dont have OrderDetail!");
        }

        Order order = new Order(customer);


        List<OrderDetail> orderDetails = orderDTO.getOrderDetails().stream()
                .map(detailDTO -> {
                    Product product = productRepository.findById(detailDTO.getProductId())
                            .orElseThrow(() -> new RuntimeException("❌ Product does not exist!"));
                    return new OrderDetail(order, product, detailDTO.getQuantity());
                })
                .collect(Collectors.toList());

        order.setOrderDetails(orderDetails);


        Integer firstProductId = orderDTO.getOrderDetails().get(0).getProductId();
        Product firstProduct = productRepository.findById(firstProductId)
                .orElseThrow(() -> new RuntimeException("❌ Product does not exist!"));

        order.setAgentId(firstProduct.getCategory().getAgent().getId());
        order.setPaymentStatus("unpaid");

        return convertToDTO(orderRepository.save(order));
    }

    @Transactional
    public void confirmPayment(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Order does not exist!"));
        order.setPaymentStatus("paid");
        orderRepository.save(order);
    }

    public OrderDTO updateOrder(Integer id, OrderDTO orderDTO) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Order does not exist!"));

        if (orderDTO.getTotalPrice() != null) order.setTotalPrice(orderDTO.getTotalPrice());
        if (orderDTO.getAgentId() != null) order.setAgentId(orderDTO.getAgentId());

        return convertToDTO(orderRepository.save(order));
    }

    public void deleteOrder(Integer id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("❌ Orderdoes not exist!");
        }
        orderRepository.deleteById(id);
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO(
                order.getId(),
                order.getCustomer().getId(),
                order.getAgentId(),
                order.getTotalPrice(),
                order.getCreatedAt(),
                order.getPaymentStatus()
        );

        if (order.getCustomer() != null && order.getCustomer().getAccount() != null) {
            dto.setCustomerName(order.getCustomer().getAccount().getUsername());
            dto.setCustomerPhone(order.getCustomer().getAccount().getPhoneNumber());
            dto.setCustomerAddress(order.getCustomer().getAddress());
        }


        List<OrderDetailDTO> detailDTOs = order.getOrderDetails().stream()
                .map(detail -> {
                    OrderDetailDTO detailDTO = new OrderDetailDTO(
                            detail.getId(),
                            order.getId(),
                            detail.getProduct().getId(),
                            detail.getQuantity(),
                            detail.getProduct().getPrice().multiply(BigDecimal.valueOf(detail.getQuantity()))
                    );
                    detailDTO.setProductName(detail.getProduct().getName());
                    detailDTO.setCategoryName(detail.getProduct().getCategory().getName());
                    detailDTO.setPrice(detail.getProduct().getPrice());
                    return detailDTO;
                })
                .collect(Collectors.toList());

        dto.setOrderDetails(detailDTOs);

        return dto;
    }

}
