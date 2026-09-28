package com.fantasy.ligabetplay.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "team")
public class Team {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "api_id", nullable = false, unique = true)
    private Integer apiId;

    @ManyToOne 
    @JoinColumn (name = "league_id", nullable = false) 
    private League leagueId;

    @Column (nullable = false, length = 100)
    private String name;
    
    @Column (length = 10)
    private String code; 
    
    @Column (length = 500)
    private String logo; 
    
    @Column (length = 100)
    private String country;

    @Column(name = "founded")
    private Integer founded;
    
    @Column(name = "national")
    private Boolean national;
    
    @Column (name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column (name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getApiId() {
        return apiId;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
    }

    public League getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(League leagueId) {
        this.leagueId = leagueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getFounded() {
        return founded;
    }

    public void setFounded(int founded) {
        this.founded = founded;
    }

    public Boolean isNational() {
        return national;
    }

    public void setNational(boolean national) {
        this.national = national;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Team() { 
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
