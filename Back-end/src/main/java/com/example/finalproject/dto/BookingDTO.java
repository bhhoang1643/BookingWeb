package com.example.finalproject.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class BookingDTO {
    private Integer bookingId;
    private Integer customerId;
    private Integer shopId;
    private Integer agentId;
    private LocalDateTime datetime;
    private String paymentStatus;
    private BigDecimal totalPrice;

    private String customerPhone;
    private String stylistName;
    private String shopLocation;

    private List<ServiceDTO> services;

    public BookingDTO() {}

    public BookingDTO(Integer bookingId, Integer customerId, Integer shopId, Integer agentId, LocalDateTime datetime, String paymentStatus, BigDecimal totalPrice) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.shopId = shopId;
        this.agentId = agentId;
        this.datetime = datetime;
        this.paymentStatus = paymentStatus;
        this.totalPrice = totalPrice;
    }

    public Integer getBookingId() { return bookingId; }
    public void setBookingId(Integer bookingId) { this.bookingId = bookingId; }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public Integer getShopId() { return shopId; }
    public void setShopId(Integer shopId) { this.shopId = shopId; }

    public Integer getAgentId() { return agentId; }
    public void setAgentId(Integer agentId) { this.agentId = agentId; }

    public LocalDateTime getDatetime() { return datetime; }
    public void setDatetime(LocalDateTime datetime) { this.datetime = datetime; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getStylistName() { return stylistName; }
    public void setStylistName(String stylistName) { this.stylistName = stylistName; }

    public String getShopLocation() { return shopLocation; }
    public void setShopLocation(String shopLocation) { this.shopLocation = shopLocation; }

    public List<ServiceDTO> getServices() { return services; }
    public void setServices(List<ServiceDTO> services) { this.services = services; }
}