package com.fantasy.ligabetplay.service;

import com.fantasy.ligabetplay.client.ApiFootballClient;
import com.fantasy.ligabetplay.repository.LeagueRepository;
import com.fantasy.ligabetplay.repository.TeamRepository;

public class FootballSyncService {
    
    private final ApiFootballClient apiFootballClient;
    private final LeagueRepository leagueRepository;
    private final TeamRepository teamRepository;

    public FootballSyncService(ApiFootballClient apiFootballClient, LeagueRepository leagueRepository, TeamRepository teamRepository){

        this.apiFootballClient = apiFootballClient;
        this.leagueRepository = leagueRepository;
        this.teamRepository = teamRepository; 

    }

    public void syncLeague(){

    }

    public void syncTeams(){
        
    }
}
