package com.fantasy.ligabetplay.service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import org.springframework.stereotype.Service;

import com.fantasy.ligabetplay.client.ApiFootballClient;
import com.fantasy.ligabetplay.entity.Fixture;
import com.fantasy.ligabetplay.entity.League;
import com.fantasy.ligabetplay.entity.Player;
import com.fantasy.ligabetplay.entity.Team;
import com.fantasy.ligabetplay.repository.FixtureRepository;
import com.fantasy.ligabetplay.repository.LeagueRepository;
import com.fantasy.ligabetplay.repository.PlayerRepository;
import com.fantasy.ligabetplay.repository.TeamRepository;
import com.fasterxml.jackson.databind.JsonNode;

@Service 
public class FootballSyncService {
    
    private final ApiFootballClient apiFootballClient;
    private final LeagueRepository leagueRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final FixtureRepository fixtureRepository;

    public FootballSyncService(ApiFootballClient apiFootballClient, LeagueRepository leagueRepository, TeamRepository teamRepository, PlayerRepository playerRepository,FixtureRepository fixtureRepository){

        this.apiFootballClient = apiFootballClient;
        this.leagueRepository = leagueRepository;
        this.teamRepository = teamRepository; 
        this.playerRepository = playerRepository;
        this.fixtureRepository = fixtureRepository;
    }

    public League syncLeague(){

        JsonNode response = apiFootballClient.searchLeagues(null, "Colombia");

        JsonNode leagues = response.path("response");

        if(!leagues.isArray() || leagues.isEmpty()){
            throw new RuntimeException("No se encontró la liga Primera A de Colombia");
        }

        JsonNode leagueData = null;

        for (JsonNode item : leagues) {
            
            String name = item.path("league").path("name").asText();

            if("Primera A".equalsIgnoreCase(name)){
                leagueData = item;
                break;
            }
        }

        if (leagueData == null) {
            throw new RuntimeException("No se encontro la liga Primera A de Colombia");
        }

        JsonNode leagueNode = leagueData.path("league");
        JsonNode countryNode = leagueData.path("country");

        Integer apiId = leagueNode.path("id").asInt();

        League league = leagueRepository.findByApiId(apiId).orElse(new League());

        league.setApiId(apiId);
        league.setName(leagueNode.path("name").asText());
        league.setCountry(countryNode.path("name").asText());
        league.setLogo(leagueNode.path("logo").asText());
        league.setType(leagueNode.path("type").asText());

        return leagueRepository.save(league);
    }

    public void syncTeams(){

        League league = leagueRepository.findByApiId(239).orElseThrow(() -> 
                            new RuntimeException("No se encontro la liga Primera A"));

        JsonNode response = apiFootballClient.getTeamByLeagueAndSeason(league.getApiId(),2024);

        JsonNode teams = response.path("response");

        if(!teams.isArray() || teams.isEmpty()){
            throw new RuntimeException("No se encontraron equipos para la liga");
        }

        for (JsonNode item : teams) {

            JsonNode teamNode = item.path("team");
            if (teamNode.isMissingNode() || teamNode.isNull()) {
                continue;
            }

            Integer apiId = teamNode.path("id").asInt();
            if (apiId == null || apiId == 0) {
                continue;
            }

            Team team = teamRepository.findByApiId(apiId).orElse(new Team());

            team.setApiId(apiId);
            team.setLeague(league);
            team.setName(teamNode.path("name").asText(null));
            team.setCode(teamNode.path("code").asText(null));
            team.setLogo(teamNode.path("logo").asText(null));

            JsonNode countryNode = teamNode.path("country");
            team.setCountry(countryNode.path("name").asText(null));

            if (!teamNode.path("founded").isNull() && !teamNode.path("founded").isMissingNode()) {
                team.setFounded(teamNode.path("founded").asInt());
            } else {
                team.setFounded(0);
            }

            if (!teamNode.path("national").isNull() && !teamNode.path("national").isMissingNode()){
                team.setNational(teamNode.path("national").asBoolean());
            } else {
                team.setNational(false);
            }

            teamRepository.save(team);
        }

    }

    public void syncPlayers() {

        League league = leagueRepository
                .findByApiId(239)
                .orElseThrow(() ->
                        new RuntimeException(
                                "No se encontró la liga Primera A"));

        int season = 2024;
        int page = 1;
        int totalPages;

        do {

            JsonNode response = apiFootballClient
                    .getPlayersByLeagueAndSeason(
                            league.getApiId(),
                            season,
                            page
                    );

            // LOG DE PAGINACIÓN
            System.out.println(
                    "Página solicitada: " + page +
                    " | Página recibida: " +
                    response.path("paging").path("current").asInt() +
                    " | Total páginas: " +
                    response.path("paging").path("total").asInt() +
                    " | Jugadores: " +
                    response.path("results").asInt()
            );

            JsonNode players = response.path("response");

            if (!players.isArray()) {
                throw new RuntimeException(
                        "La respuesta de API-Football no contiene jugadores");
            }

            for (JsonNode item : players) {

                JsonNode playerNode = item.path("player");

                /*
                * API-Football puede tener más de una estadística
                * para un jugador. Para nuestra consulta de liga
                * tomamos la primera.
                */
                JsonNode statistics = item
                        .path("statistics")
                        .path(0);

                if (statistics.isMissingNode()) {
                    continue;
                }

                // ID del jugador en API-Football
                Integer apiId = playerNode
                        .path("id")
                        .asInt();

                // ID del equipo en API-Football
                Integer teamApiId = statistics
                        .path("team")
                        .path("id")
                        .asInt();

                // Buscar el equipo que ya tenemos en nuestra BD
                Team team = teamRepository
                        .findByApiId(teamApiId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "No se encontró el equipo con API ID: "
                                                + teamApiId));

                // Buscar jugador existente o crear uno nuevo
                Player player = playerRepository
                        .findByApiId(apiId)
                        .orElse(new Player());

                player.setApiId(apiId);

                player.setTeamId(team);

                String firstName = playerNode
                        .path("firstname")
                        .asText("");

                String lastName = playerNode
                        .path("lastname")
                        .asText("");

                String fullName = (firstName + "" + lastName).trim();

                if (fullName.isBlank()) {
                    fullName = playerNode
                            .path("name")
                            .asText();
                }

                player.setName(fullName);

                player.setPhoto(
                        playerNode
                                .path("photo")
                                .asText(null)
                );

                player.setPosition(
                        statistics
                                .path("games")
                                .path("position")
                                .asText(null)
                );

                playerRepository.save(player);
            }

            totalPages = response
                    .path("paging")
                    .path("total")
                    .asInt(1);

            System.out.println("Procesando página " + page);        
            
            page++;

        } while (page <= totalPages);
    }

    public void syncFixtures(){

         // obtener liga
         League league = leagueRepository
                .findByApiId(239)
                .orElseThrow(() ->
                        new RuntimeException(
                                "No se encontró la liga Primera A"));
        
        int season = 2024;
    
        // obtener fixtures de API-Football
        JsonNode response = apiFootballClient
                    .getFixturesByLeagueAndSeason(
                            league.getApiId(),
                            season
                    );
        
        // recorrer partidos
        JsonNode fixtures = response.path("response");

            if (!fixtures.isArray()) {
                throw new RuntimeException(
                        "La respuesta de API-Football no contiene partidos");
            }

            for (JsonNode item : fixtures) {

                JsonNode fixtureNode = item.path("fixture");
                JsonNode leagueNode = item.path("league");
                JsonNode teamsNode = item.path("teams");
                JsonNode goalsNode = item.path("goals");

                // ID del partido en API-Football
                Integer apiId = fixtureNode
                        .path("id")
                        .asInt();

                // ID del equipo local en API-Football
                Integer homeTeamApiId = teamsNode
                        .path("home")
                        .path("id")
                        .asInt();

                Integer awayTeamApiId = teamsNode
                        .path("away")
                        .path("id")
                        .asInt();

                int homeGoals = goalsNode
                        .path("home")
                        .asInt();

                int awayGoals = goalsNode
                        .path("away")
                        .asInt();

                // Buscar el equipo local que ya tenemos en nuestra BD
                Team homeTeam = teamRepository
                        .findByApiId(homeTeamApiId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "No se encontró el equipo local con API ID: "
                                                + homeTeamApiId));

                // Buscar el equipo visitante que ya tenemos en nuestra BD
                Team awayTeam = teamRepository
                        .findByApiId(awayTeamApiId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "No se encontró el equipo local con API ID: "
                                                + awayTeamApiId));

                // Buscar partido existente o crear uno nuevo
                Fixture fixture = fixtureRepository
                        .findByApiId(apiId)
                        .orElse(new Fixture());

                fixture.setApiId(apiId);

                fixture.setLeague(league);

                fixture.setHomeTeam(homeTeam);

                fixture.setAwayTeam(awayTeam);

                fixture.setSeason(
                        leagueNode
                                .path("season").asInt());
                
                fixture.setDate(
                        OffsetDateTime.parse(fixtureNode.path("date").asText()).toLocalDateTime() 
                );

                fixture.setStatus(
                        fixtureNode.path("status").path("short").asText()
                );

                fixture.setHomeGoals(homeGoals);

                fixture.setAwayGoals(awayGoals);

                fixtureRepository.save(fixture);
            }

    }
}
