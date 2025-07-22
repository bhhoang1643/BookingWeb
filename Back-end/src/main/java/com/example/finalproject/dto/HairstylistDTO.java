package com.example.finalproject.dto;

public class HairstylistDTO {
    private Integer stylistId;
    private Integer shopId;
    private String name;
    private Integer experience;
    private String specialty;
    private String image;

    private String shopName;
    private String agentName;

    public HairstylistDTO() {
    }


    public HairstylistDTO(Integer stylistId, Integer shopId, String name,
                          Integer experience, String specialty, String image,
                          String shopName, String agentName) {
        this.stylistId = stylistId;
        this.shopId = shopId;
        this.name = name;
        this.experience = experience;
        this.specialty = specialty;
        this.image = image;
        this.shopName = shopName;
        this.agentName = agentName;
    }

    // ✅ Constructor 6 tham số cho create/update (giữ tương thích)
    public HairstylistDTO(Integer stylistId, Integer shopId, String name,
                          Integer experience, String specialty, String image) {
        this(stylistId, shopId, name, experience, specialty, image, null, null);
    }

    // ====== Getters ======
    public Integer getStylistId() {
        return stylistId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public String getName() {
        return name;
    }

    public Integer getExperience() {
        return experience;
    }

    public String getSpecialty() {
        return specialty;
    }

    public String getImage() {
        return image;
    }

    public String getShopName() {
        return shopName;
    }

    public String getAgentName() {
        return agentName;
    }

    // ====== Setters ======
    public void setStylistId(Integer stylistId) {
        this.stylistId = stylistId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }
}
