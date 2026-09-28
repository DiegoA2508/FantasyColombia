package com.fantasy.ligabetplay.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity 
@Table (name = "player_fixture_fouls")
public class PlayerFixtureFouls {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne  
    @JoinColumn (name = "stats_id", nullable = false, unique = true)
    private PlayerFixtureStats foulsStats;

    private int foulsDrawn;

    private int foulsCommitted;

    public PlayerFixtureFouls(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlayerFixtureStats getFoulsStats() {
        return foulsStats;
    }

    public void setFoulsStats(PlayerFixtureStats foulsStats) {
        this.foulsStats = foulsStats;
    }

    public int getFoulsDrawn() {
        return foulsDrawn;
    }

    public void setFoulsDrawn(int foulsDrawn) {
        this.foulsDrawn = foulsDrawn;
    }

    public int getFoulsCommitted() {
        return foulsCommitted;
    }

    public void setFoulsCommitted(int foulsCommitted) {
        this.foulsCommitted = foulsCommitted;
    }

    

}
