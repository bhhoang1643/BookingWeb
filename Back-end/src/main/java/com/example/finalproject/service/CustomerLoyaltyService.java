package com.example.finalproject.service;

import com.example.finalproject.dto.CustomerLoyaltyDTO;
import com.example.finalproject.entity.Customer;
import com.example.finalproject.entity.CustomerLoyalty;
import com.example.finalproject.repository.CustomerLoyaltyRepository;
import com.example.finalproject.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerLoyaltyService {

    private final CustomerLoyaltyRepository loyaltyRepo;
    private final CustomerRepository customerRepo;

    public CustomerLoyaltyService(CustomerLoyaltyRepository loyaltyRepo, CustomerRepository customerRepo) {
        this.loyaltyRepo = loyaltyRepo;
        this.customerRepo = customerRepo;
    }

    public List<CustomerLoyaltyDTO> getAll() {
        return loyaltyRepo.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CustomerLoyaltyDTO> getByCustomerId(Integer customerId) {
        return loyaltyRepo.findByCustomer_Id(customerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CustomerLoyaltyDTO create(CustomerLoyaltyDTO dto) {
        Customer customer = customerRepo.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("❌ Customer not found"));

        CustomerLoyalty loyalty = new CustomerLoyalty(customer, dto.getConvertPoints(), LocalDateTime.now());
        return convertToDTO(loyaltyRepo.save(loyalty));
    }

    public void recordPointUsage(Integer customerId, Integer points) {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("❌ Customer does not exist!"));

        CustomerLoyalty log = new CustomerLoyalty();
        log.setCustomer(customer);
        log.setConvertPoints(points);
        log.setDatetime(LocalDateTime.now());
        loyaltyRepo.save(log);
    }

    private CustomerLoyaltyDTO convertToDTO(CustomerLoyalty loyalty) {
        return new CustomerLoyaltyDTO(
                loyalty.getId(),
                loyalty.getCustomer().getId(),
                loyalty.getConvertPoints(),
                loyalty.getDatetime()
        );
    }
}
