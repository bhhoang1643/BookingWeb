package com.example.finalproject.dto;

import com.example.finalproject.entity.Account.Role;
import java.time.LocalDateTime;

public class AccountDTO {
    private Integer accountId;
    private String username;
    private String email;
    private String phoneNumber;
    private LocalDateTime submittedAt;
    private String status;
    private Role role;

    public AccountDTO() {}


    public AccountDTO(Integer accountId, String username, String email, String phoneNumber, LocalDateTime submittedAt, String status, Role role) {
        this.accountId = accountId;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.submittedAt = submittedAt;
        this.status = status;
        this.role = role;
    }


    public AccountDTO(Integer accountId, String username, String email, String phoneNumber) {
        this.accountId = accountId;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }


    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
