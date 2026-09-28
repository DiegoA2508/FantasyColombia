package com.fantasy.ligabetplay.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity 
@Table (name = "player_fixture_penalty")
public class PlayerFixturePenalty {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne  
    @JoinColumn (name = "Stats_id", nullable = false, unique = true)
    private PlayerFixtureStats penaltyStats;

    private int penaltyWon;

    private int penaltyCommitted;
    
    private int penaltyScored;
    
    private int penaltyMissed;
    
    private int penaltySaved;

    public PlayerFixturePenalty (){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlayerFixtureStats getPenaltyStats() {
        return penaltyStats;
    }

    public void setPenaltyStats(PlayerFixtureStats penaltyStats) {
        this.penaltyStats = penaltyStats;
    }

    public int getPenaltyWon() {
        return penaltyWon;
    }

    public void setPenaltyWon(int penaltyWon) {
        this.penaltyWon = penaltyWon;
    }

    public int getPenaltyCommitted() {
        return penaltyCommitted;
    }

    public void setPenaltyCommitted(int penaltyCommitted) {
        this.penaltyCommitted = penaltyCommitted;
    }

    public int getPenaltyScored() {
        return penaltyScored;
    }

    public void setPenaltyScored(int penaltyScored) {
        this.penaltyScored = penaltyScored;
    }

    public int getPenaltyMissed() {
        return penaltyMissed;
    }

    public void setPenaltyMissed(int penaltyMissed) {
        this.penaltyMissed = penaltyMissed;
    }

    public int getPenaltySaved() {
        return penaltySaved;
    }

    public void setPenaltySaved(int penaltySaved) {
        this.penaltySaved = penaltySaved;
    }

    
}
