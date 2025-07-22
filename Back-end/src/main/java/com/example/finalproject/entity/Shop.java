package com.example.finalproject.entity;

import jakarta.persistence.*;

@Entity
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "agent_id", nullable = false) // ✅ Liên kết với Agent
    private Agent agent;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "phonenumber", nullable = false)
    private String phoneNumber;

    public Shop() {}

    public Shop(Agent agent, String location, String phoneNumber) {
        this.agent = agent;
        this.location = location;
        this.phoneNumber = phoneNumber;
    }

    // ✅ Getters & Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Agent getAgent() { return agent; }
    public void setAgent(Agent agent) { this.agent = agent; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}
