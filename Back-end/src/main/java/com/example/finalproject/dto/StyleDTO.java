package com.example.finalproject.dto;

public class StyleDTO {
    private Long id;
    private Long styleTagId;
    private String styleTagName;
    private Integer agentId;

    // Constructors
    public StyleDTO() {}

    public StyleDTO(Long id, Long styleTagId, String styleTagName, Integer agentId) {
        this.id = id;
        this.styleTagId = styleTagId;
        this.styleTagName = styleTagName;
        this.agentId = agentId;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStyleTagId() {
        return styleTagId;
    }

    public void setStyleTagId(Long styleTagId) {
        this.styleTagId = styleTagId;
    }

    public String getStyleTagName() {
        return styleTagName;
    }

    public void setStyleTagName(String styleTagName) {
        this.styleTagName = styleTagName;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }
}
