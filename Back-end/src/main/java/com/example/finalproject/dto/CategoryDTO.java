package com.example.finalproject.dto;

public class CategoryDTO {
    private Integer id;
    private String name;
    private Integer agentId;

    public CategoryDTO() {}

    public CategoryDTO(Integer id, String name, Integer agentId) {
        this.id = id;
        this.name = name;
        this.agentId = agentId;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getAgentId() { return agentId; }
    public void setAgentId(Integer agentId) { this.agentId = agentId; }
}