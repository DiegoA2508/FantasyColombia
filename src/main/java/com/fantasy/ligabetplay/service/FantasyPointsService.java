package com.fantasy.ligabetplay.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fantasy.ligabetplay.entity.Fixture;
import com.fantasy.ligabetplay.entity.PlayerFixtureAttack;
import com.fantasy.ligabetplay.entity.PlayerFixtureStats;
import com.fantasy.ligabetplay.entity.Team;
import com.fantasy.ligabetplay.repository.PlayerFixtureAttackRepository;
import com.fantasy.ligabetplay.repository.PlayerFixtureStatsRepository;

@Service 
public class FantasyPointsService {

    private final PlayerFixtureStatsRepository statsRepository;
    private final PlayerFixtureAttackRepository attackRepository;
    
    public FantasyPointsService(PlayerFixtureStatsRepository statsRepository,
            PlayerFixtureAttackRepository attackRepository) {
        this.statsRepository = statsRepository;
        this.attackRepository = attackRepository;
    }

    //Calcula los puntos de un jugador en un partido
    public int calculatePoints(PlayerFixtureStats stats){
        
        int points = 0;

        //1. Puntos por rating dado por la API
        points += calculateRatingPoints(stats.getRating());

        //2. Goles y asistencias 
        Optional<PlayerFixtureAttack> attackOptional = attackRepository.findByAttackStats(stats);
        
        if(attackOptional.isPresent()){
            PlayerFixtureAttack attack = attackOptional.get();

            points += attack.getGoals() * getGoalPoints(stats.getPosition());

            points += attack.getAssists() * 3; //3 puntos por asistencia
        }

        //3. Porteria a cero
        if(hasCleanSheet(stats)){
            String position = normalizePosition(stats.getPosition());

            if( "D".equals(position) || "G".equals(position)){
                points += 4; //4 puntos por porteria a cero para defensas y porteros
            }
        }

        return points;
    }

    private int calculateRatingPoints(BigDecimal rating){
        
        if (rating == null) {
            return 0;
        }

        if(rating.compareTo(new BigDecimal("5.0")) < 0){
            return -2;
        } else if (rating.compareTo(new BigDecimal("6.0")) < 0){
            return 0;        
        } else if (rating.compareTo(new BigDecimal("6.5")) < 0){
            return 1;        
        } else if (rating.compareTo(new BigDecimal("7.0")) < 0){
            return 2;        
        } else if (rating.compareTo(new BigDecimal("7.5")) < 0){
            return 3;        
        } else if (rating.compareTo(new BigDecimal("8.0")) < 0){
            return 4;        
        } else if (rating.compareTo(new BigDecimal("8.5")) < 0){
            return 5;        
        } else if (rating.compareTo(new BigDecimal("9.0")) < 0){
            return 6;        
        }

        return 7;
    }

    private int getGoalPoints(String position){

        String normalized = normalizePosition(position);

        switch(normalized){
            case "F":
                return 4; //4 puntos por gol para delanteros
            case "M":
                return 5; //5 puntos por gol para mediocampistas
            case "D":
                return 6; //6 puntos por gol para defensas
            case "G":
                return 8; //8 puntos por gol para porteros
            default:
                return 0;
        }
    }

    private boolean hasCleanSheet(PlayerFixtureStats stats){

        String position = normalizePosition(stats.getPosition());

        if(!"D".equals(position) && !"G".equals(position)){
            return false; //Solo defensas y porteros pueden tener porteria a cero
        }

        if(stats.getMinutes() < 60){
            return false; //Si jugo menos de 60 minutos no puede tener porteria a cero
        }

        Fixture fixture = stats.getFixture();
        Team playerTeam = stats.getTeam();

        if(fixture == null || playerTeam == null){
            return false;
        }

        Integer homeGoals = fixture.getHomeGoals();
        Integer awayGoals = fixture.getAwayGoals();

        if(homeGoals == null || awayGoals == null){
            return false;
        }

        if(fixture.getHomeTeam() != null && fixture.getHomeTeam().getId().equals(playerTeam.getId())){
            return awayGoals == 0; //Si es equipo visitante, porteria a cero si el equipo local no marco goles
        }

        if(fixture.getAwayTeamId().getId().equals(playerTeam.getId())){
            return homeGoals == 0; //Si es equipo local, porteria a cero si el equipo visitante no marco goles
        }

        return false;
    }

    private String normalizePosition(String position){

        if(position == null){
            return "";
        }

        return position.trim().toUpperCase();
    }

    //Recalcula los registros existentes
    @Transactional 
    public int recalculateAllPoints(){

        List<PlayerFixtureStats> allStats = statsRepository.findAll();

        for (PlayerFixtureStats stats : allStats){

            int points = calculatePoints(stats);
            
            stats.setPoints(points);
        }

        statsRepository.saveAll(allStats);

        return allStats.size();
    }
    
}
