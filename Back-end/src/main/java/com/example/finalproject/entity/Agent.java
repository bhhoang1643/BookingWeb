package com.example.finalproject.entity;

import jakarta.persistence.*;

@Entity
public class Agent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "specialization", nullable = false)
    private String specialization;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "establishment", nullable = false)
    private String establishment;

    @Column(name = "opening_hours")
    private String openingHours;

    @Column(name = "professional_skills", columnDefinition = "TEXT")
    private String professionalSkills;

    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @Column(name = "agent_name", nullable = false)
    private String agentName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id", nullable = false)
    private Account account;

    public Agent() {}

    public Agent(String specialization, String location, String establishment, String openingHours,
                 String professionalSkills, String ownerName, String agentName, Account account) {
        this.specialization = specialization;
        this.location = location;
        this.establishment = establishment;
        this.openingHours = openingHours;
        this.professionalSkills = professionalSkills;
        this.ownerName = ownerName;
        this.agentName = agentName;
        this.account = account;
    }

    // ✅ Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getEstablishment() { return establishment; }
    public void setEstablishment(String establishment) { this.establishment = establishment; }

    public String getOpeningHours() { return openingHours; }
    public void setOpeningHours(String openingHours) { this.openingHours = openingHours; }

    public String getProfessionalSkills() { return professionalSkills; }
    public void setProfessionalSkills(String professionalSkills) { this.professionalSkills = professionalSkills; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public String getAgentName() { return agentName; }
    public void setAgentName(String agentName) { this.agentName = agentName; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
}
