package com.example.finalproject.dto;

import java.time.LocalDate;

public class CustomerDTO {
    private Integer id;
    private LocalDate birthYear;
    private String gender;
    private Integer point;
    private String imageFile;
    private String address;
    private Integer accountId;
    private AccountDTO account;

    public CustomerDTO() {}

    public CustomerDTO(Integer id, LocalDate birthYear, String gender, Integer point, String imageFile, String address, Integer accountId, AccountDTO account) {
        this.id = id;
        this.birthYear = birthYear;
        this.gender = gender;
        this.point = point;
        this.imageFile = imageFile;
        this.address = address;
        this.accountId = accountId;
        this.account = account;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(LocalDate birthYear) {
        this.birthYear = birthYear;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public AccountDTO getAccount() {
        return account;
    }

    public void setAccount(AccountDTO account) {
        this.account = account;
    }
}
