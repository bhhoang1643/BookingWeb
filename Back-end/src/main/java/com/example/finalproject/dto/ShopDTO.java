package com.example.finalproject.dto;

public class ShopDTO {

    private Integer id;
    private Integer agentId;
    private String location;
    private String phoneNumber;

    // Constructors
    public ShopDTO(Integer id, Integer agentId, String location, String phoneNumber) {
        this.id = id;
        this.agentId = agentId;
        this.location = location;
        this.phoneNumber = phoneNumber;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
