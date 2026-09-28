package com.fantasy.ligabetplay.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity 
@Table (name = "player_fixture_passing")
public class PlayerFixturePassing {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne  
    @JoinColumn (name = "stats_id", nullable = false, unique = true)
    private PlayerFixtureStats passingStats;

    private int passesTotal;

    private int passesKey;
    
    private int passesAccuracy;

    public PlayerFixturePassing(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlayerFixtureStats getPassingStats() {
        return passingStats;
    }

    public void setPassingStats(PlayerFixtureStats passingStats) {
        this.passingStats = passingStats;
    }

    public int getPassesTotal() {
        return passesTotal;
    }

    public void setPassesTotal(int passesTotal) {
        this.passesTotal = passesTotal;
    }

    public int getPassesKey() {
        return passesKey;
    }

    public void setPassesKey(int passesKey) {
        this.passesKey = passesKey;
    }

    public int getPassesAccuracy() {
        return passesAccuracy;
    }

    public void setPassesAccuracy(int passesAccuracy) {
        this.passesAccuracy = passesAccuracy;
    }

}
