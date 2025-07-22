package com.example.finalproject.dto;

import java.math.BigDecimal;

public class ProductDTO {
    private Integer id;
    private String image;
    private String name;
    private BigDecimal price;
    private Integer categoryId;
    private Integer agentId;
    private String description;

    public ProductDTO() {}

    public ProductDTO(Integer id, String image, String name, BigDecimal price, Integer categoryId, Integer agentId, String description) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
        this.agentId = agentId;
        this.description = description;
    }


    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }

    public Integer getAgentId() { return agentId; }
    public void setAgentId(Integer agentId) { this.agentId = agentId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}