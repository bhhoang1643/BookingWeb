package com.example.finalproject.dto;

public class PreferencesDTO {
    private Integer id;
    private Integer customerId;
    private Long styleTagId;
    private String styleTagName; // optional, để hiển thị tên

    // Constructors
    public PreferencesDTO() {}

    public PreferencesDTO(Integer id, Integer customerId, Long styleTagId, String styleTagName) {
        this.id = id;
        this.customerId = customerId;
        this.styleTagId = styleTagId;
        this.styleTagName = styleTagName;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
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
}
