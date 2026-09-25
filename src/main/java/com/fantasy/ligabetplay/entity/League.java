package com.fantasy.ligabetplay.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity 
@Table(name = "league")
public class League {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "api_id", nullable = false, unique = true)
    private Integer apiId;     

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 100)
    private String country;

    @Column(length = 500)
    private String logo;

    @Column(length = 30)
    private String type;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public League() {   // Default constructor
    }

    public Long getId() {
        return id;
    }

    public Integer getApiId() {
        return apiId;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getLogo() {
        return logo;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setType(String type) {
        this.type = type;
    }

    @PrePersist 
    protected void onCreate(){
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate 
    protected void onUpdate(){
        updatedAt = LocalDateTime.now();
    }

}
