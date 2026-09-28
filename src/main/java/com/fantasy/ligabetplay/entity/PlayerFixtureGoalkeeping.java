package com.fantasy.ligabetplay.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity 
@Table (name ="player_fixture_goalkeeping")
public class PlayerFixtureGoalkeeping {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne  
    @JoinColumn (name = "stats_id", nullable = false, unique = true)
    private PlayerFixtureStats goalkeepStats;

    @Column (name = "goals_conceded")
    private int goalsConceded;

    @Column (name = "goals_saves")
    private int goalsSaves;

    public PlayerFixtureGoalkeeping (){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlayerFixtureStats getGoalkeepStats() {
        return goalkeepStats;
    }

    public void setGoalkeepStats(PlayerFixtureStats goalkeepStats) {
        this.goalkeepStats = goalkeepStats;
    }

    public int getGoalsConceded() {
        return goalsConceded;
    }

    public void setGoalsConceded(int goalsConceded) {
        this.goalsConceded = goalsConceded;
    }

    public int getGoalsSaves() {
        return goalsSaves;
    }

    public void setGoalsSaves(int goalsSaves) {
        this.goalsSaves = goalsSaves;
    } 

    
    
}
