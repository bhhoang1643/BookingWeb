package com.example.finalproject.dto;

import java.math.BigDecimal;

public class ServiceDTO {
    private Integer serviceId;
    private Integer agentId;
    private String name;
    private BigDecimal price;
    private String status;
    private String image;

    public ServiceDTO() {}

    public ServiceDTO(Integer serviceId, Integer agentId, String name, BigDecimal price, String status, String image) {
        this.serviceId = serviceId;
        this.agentId = agentId;
        this.name = name;
        this.price = price;
        this.status = status;
        this.image = image;
    }


    public Integer getServiceId() { return serviceId; }
    public void setServiceId(Integer serviceId) { this.serviceId = serviceId; }

    public Integer getAgentId() { return agentId; }
    public void setAgentId(Integer agentId) { this.agentId = agentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}
