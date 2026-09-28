package com.fantasy.ligabetplay.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity 
@Table (name = "player_fixture_attack")
public class PlayerFixtureAttack {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne  
    @JoinColumn(name = "stats_id", nullable = false, unique = true)
    private PlayerFixtureStats attackStats;

    private int shotsTotal;

    private int shotsOn;

    private int offsides;

    private int dribblesAttempts;

    private int dribblesSuccess;
    
    private int dribblesPast;

    public PlayerFixtureAttack (){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlayerFixtureStats getAttackStats() {
        return attackStats;
    }

    public void setAttackStats(PlayerFixtureStats attackStats) {
        this.attackStats = attackStats;
    }

    public int getShotsTotal() {
        return shotsTotal;
    }

    public void setShotsTotal(int shotsTotal) {
        this.shotsTotal = shotsTotal;
    }

    public int getShotsOn() {
        return shotsOn;
    }

    public void setShotsOn(int shotsOn) {
        this.shotsOn = shotsOn;
    }

    public int getOffsides() {
        return offsides;
    }

    public void setOffsides(int offsides) {
        this.offsides = offsides;
    }

    public int getDribblesAttempts() {
        return dribblesAttempts;
    }

    public void setDribblesAttempts(int dribblesAttempts) {
        this.dribblesAttempts = dribblesAttempts;
    }

    public int getDribblesSuccess() {
        return dribblesSuccess;
    }

    public void setDribblesSuccess(int dribblesSuccess) {
        this.dribblesSuccess = dribblesSuccess;
    }

    public int getDribblesPast() {
        return dribblesPast;
    }

    public void setDribblesPast(int dribblesPast) {
        this.dribblesPast = dribblesPast;
    }

}
