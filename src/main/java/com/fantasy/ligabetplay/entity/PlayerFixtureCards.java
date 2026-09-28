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
@Table (name = "player_fixture_cards")
public class PlayerFixtureCards {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne  
    @JoinColumn (name ="stats_id", nullable = false, unique = true)
    private PlayerFixtureStats cardStats;

    @Column (name = "yellow_cards")
    private int yellow;

    @Column (name = "red_cards")
    private int red;

    public PlayerFixtureCards(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlayerFixtureStats getCardStats() {
        return cardStats;
    }

    public void setCardStats(PlayerFixtureStats cardStats) {
        this.cardStats = cardStats;
    }

    public int getYellow() {
        return yellow;
    }

    public void setYellow(int yellow) {
        this.yellow = yellow;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    
    
}
