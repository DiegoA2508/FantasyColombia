package com.fantasy.ligabetplay.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity 
@Table (name = "player_fixture_defense")
public class PlayerFixtureDefense {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne 
    @JoinColumn (name = "stats_id", nullable = false, unique = true)
    private PlayerFixtureStats defenseStats;

    private int tacklesTotal;

    private int tacklesBlocks;
    
    private int tacklesInterceptions;

    private int duelsTotal;
    
    private int duelsWon;
    
    public PlayerFixtureDefense(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlayerFixtureStats getDefenseStats() {
        return defenseStats;
    }

    public void setDefenseStats(PlayerFixtureStats defenseStats) {
        this.defenseStats = defenseStats;
    }

    public int getTacklesTotal() {
        return tacklesTotal;
    }

    public void setTacklesTotal(int tacklesTotal) {
        this.tacklesTotal = tacklesTotal;
    }

    public int getTacklesBlocks() {
        return tacklesBlocks;
    }

    public void setTacklesBlocks(int tacklesBlocks) {
        this.tacklesBlocks = tacklesBlocks;
    }

    public int getTacklesInterceptions() {
        return tacklesInterceptions;
    }

    public void setTacklesInterceptions(int tacklesInterceptions) {
        this.tacklesInterceptions = tacklesInterceptions;
    }

    public int getDuelsTotal() {
        return duelsTotal;
    }

    public void setDuelsTotal(int duelsTotal) {
        this.duelsTotal = duelsTotal;
    }

    public int getDuelsWon() {
        return duelsWon;
    }

    public void setDuelsWon(int duelsWon) {
        this.duelsWon = duelsWon;
    }

    
}
