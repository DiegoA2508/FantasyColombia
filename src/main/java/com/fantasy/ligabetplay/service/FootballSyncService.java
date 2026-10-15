package com.fantasy.ligabetplay.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.fantasy.ligabetplay.client.ApiFootballClient;
import com.fantasy.ligabetplay.entity.Fixture;
import com.fantasy.ligabetplay.entity.League;
import com.fantasy.ligabetplay.entity.Player;
import com.fantasy.ligabetplay.entity.PlayerFixtureAttack;
import com.fantasy.ligabetplay.entity.PlayerFixtureCards;
import com.fantasy.ligabetplay.entity.PlayerFixtureDefense;
import com.fantasy.ligabetplay.entity.PlayerFixtureFouls;
import com.fantasy.ligabetplay.entity.PlayerFixtureGoalkeeping;
import com.fantasy.ligabetplay.entity.PlayerFixturePassing;
import com.fantasy.ligabetplay.entity.PlayerFixturePenalty;
import com.fantasy.ligabetplay.entity.PlayerFixtureStats;
import com.fantasy.ligabetplay.entity.Team;
import com.fantasy.ligabetplay.repository.FixtureRepository;
import com.fantasy.ligabetplay.repository.LeagueRepository;
import com.fantasy.ligabetplay.repository.PlayerFixtureAttackRepository;
import com.fantasy.ligabetplay.repository.PlayerFixtureCardsRepository;
import com.fantasy.ligabetplay.repository.PlayerFixtureDefenseRepository;
import com.fantasy.ligabetplay.repository.PlayerFixtureFoulsRepository;
import com.fantasy.ligabetplay.repository.PlayerFixtureGoalkeepingRepository;
import com.fantasy.ligabetplay.repository.PlayerFixturePassingRepository;
import com.fantasy.ligabetplay.repository.PlayerFixturePenaltyRepository;
import com.fantasy.ligabetplay.repository.PlayerFixtureStatsRepository;
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
    private final PlayerFixtureStatsRepository playerFixtureStatsRepository;
    private final PlayerFixtureAttackRepository playerFixtureAttackRepository;
    private final PlayerFixtureCardsRepository playerFixtureCardsRepository;
    private final PlayerFixtureDefenseRepository playerFixtureDefenseRepository;
    private final PlayerFixtureFoulsRepository playerFixtureFoulsRepository;
    private final PlayerFixtureGoalkeepingRepository playerFixtureGoalkeepingRepository;
    private final PlayerFixturePassingRepository playerFixturePassingRepository;
    private final PlayerFixturePenaltyRepository playerFixturePenaltyRepository;

    public FootballSyncService(ApiFootballClient apiFootballClient, LeagueRepository leagueRepository, TeamRepository teamRepository, PlayerRepository playerRepository,FixtureRepository fixtureRepository,
                                PlayerFixtureStatsRepository playerFixtureStatsRepository, PlayerFixtureAttackRepository playerFixtureAttackRepository, PlayerFixtureCardsRepository playerFixtureCardsRepository,
                                PlayerFixtureDefenseRepository playerFixtureDefenseRepository, PlayerFixtureFoulsRepository playerFixtureFoulsRepository, PlayerFixtureGoalkeepingRepository playerFixtureGoalkeepingRepository,
                                PlayerFixturePassingRepository playerFixturePassingRepository, PlayerFixturePenaltyRepository playerFixturePenaltyRepository
    ){

        this.apiFootballClient = apiFootballClient;
        this.leagueRepository = leagueRepository;
        this.teamRepository = teamRepository; 
        this.playerRepository = playerRepository;
        this.fixtureRepository = fixtureRepository;
        this.playerFixtureStatsRepository = playerFixtureStatsRepository;
        this.playerFixtureAttackRepository = playerFixtureAttackRepository;
        this.playerFixtureCardsRepository = playerFixtureCardsRepository;
        this.playerFixtureDefenseRepository = playerFixtureDefenseRepository;
        this.playerFixtureFoulsRepository = playerFixtureFoulsRepository;
        this.playerFixtureGoalkeepingRepository = playerFixtureGoalkeepingRepository;
        this.playerFixturePassingRepository = playerFixturePassingRepository;
        this.playerFixturePenaltyRepository = playerFixturePenaltyRepository;
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

    public void syncPlayerFixtureStats() {

        List<Fixture> fixtures = fixtureRepository.findAll();

        System.out.println(
                "Cantidad de fixtures encontrados: " + fixtures.size());

        int totalTeams = 0;
        int totalPlayers = 0;
        int totalSaved = 0;
        int totalSkipped = 0;

        System.out.println("====== INICIANDO SINCRONIZACIÓN DE STATS ======");

        for (Fixture fixture : fixtures) {

                System.out.println("Procesando fixture API ID: " + fixture.getApiId());

                JsonNode response = null;

                // ==========================================
                // OBTENER ESTADÍSTICAS DEL FIXTURE
                // ==========================================

                int intentos = 0;
                int maxIntentos = 3;

                while (intentos < maxIntentos) {

                try {

                        response = apiFootballClient.getPlayerStatsByFixture(fixture.getApiId());

                        break;

                } catch (HttpClientErrorException.TooManyRequests e) {

                        intentos++;

                        System.out.println("Límite de peticiones alcanzado para fixture "
                                        + fixture.getApiId()
                                        + ". Intento "
                                        + intentos
                                        + " de "
                                        + maxIntentos
                        );

                        if (intentos >= maxIntentos) {

                        System.out.println("Se omitió fixture "+ fixture.getApiId()
                        );

                        totalSkipped++;
                        break;
                        }

                        try {

                        System.out.println("Esperando 20 segundos...");

                        Thread.sleep(20000);

                        } catch (InterruptedException ex) {

                        Thread.currentThread().interrupt();

                        throw new RuntimeException(
                                "La sincronización fue interrumpida");
                        }
                }
                }

                if (response == null) {
                continue;
                }

                // ==========================================
                // RESPONSE
                // ==========================================

                JsonNode teams = response.path("response");

                if (!teams.isArray()) {

                System.out.println("Response inválido para fixture "+ fixture.getApiId());

                totalSkipped++;
                continue;
                }

                totalTeams += teams.size();

                // ==========================================
                // EQUIPOS
                // ==========================================

                for (JsonNode teamNode : teams) {

                Integer teamApiId = teamNode
                        .path("team")
                        .path("id")
                        .asInt();

                Team team = teamRepository
                        .findByApiId(teamApiId)
                        .orElseThrow(() ->
                                new RuntimeException("No se encontró el equipo con API ID: "+ teamApiId)
                        );

                JsonNode players = teamNode.path("players");

                if (!players.isArray()) {
                        continue;
                }

                // ==========================================
                // JUGADORES
                // ==========================================

                for (JsonNode playerItem : players) {

                        totalPlayers++;

                        JsonNode playerNode =
                                playerItem.path("player");

                        JsonNode statistics =
                                playerItem
                                        .path("statistics")
                                        .path(0);

                        if (statistics.isMissingNode()|| statistics.isNull()) {

                                totalSkipped++;
                                continue;
                        }

                        Integer playerApiId =
                                playerNode
                                        .path("id")
                                        .asInt();

                        // ==========================================
                        // PLAYER
                        // ==========================================

                        Player player = playerRepository
                                .findByApiId(playerApiId)
                                .orElseGet(() -> {

                                Player newPlayer = new Player();

                                newPlayer.setApiId(playerApiId);

                                newPlayer.setName(
                                        playerNode
                                                .path("name")
                                                .asText()
                                );

                                newPlayer.setPhoto(
                                        playerNode
                                                .path("photo")
                                                .asText(null)
                                );

                                newPlayer.setTeamId(team);

                                newPlayer.setPosition(
                                        statistics
                                                .path("games")
                                                .path("position")
                                                .asText(null)
                                );

                                return playerRepository.save(newPlayer);
                                });

                        // ==========================================
                        // 1. PLAYER FIXTURE STATS
                        // ==========================================

                        PlayerFixtureStats stats =
                                playerFixtureStatsRepository
                                        .findByPlayerAndFixture(
                                                player,
                                                fixture
                                        )
                                        .orElse(
                                                new PlayerFixtureStats()
                                        );

                        stats.setPlayer(player);
                        stats.setFixture(fixture);
                        stats.setTeam(team);

                        JsonNode games =
                                statistics.path("games");

                        stats.setMinutes(
                                games.path("minutes").asInt(0)
                        );

                        stats.setNumber(
                                games.path("number").asInt(0)
                        );

                        stats.setPosition(
                                games.path("position").asText(null)
                        );

                        if (games.path("rating").isNull()
                                || games.path("rating")
                                        .asText()
                                        .isBlank()) {

                        stats.setRating(null);

                        } else {

                        stats.setRating(
                                new BigDecimal(
                                        games.path("rating").asText()
                                )
                        );
                        }

                        stats.setCaptain(
                                games.path("captain").asBoolean(false)
                        );

                        stats.setSubstitute(
                                games.path("substitute").asBoolean(false)
                        );

                        // Por ahora los puntos se calculan después
                        stats.setPoints(0);

                        stats = playerFixtureStatsRepository.save(stats);


                        // ==========================================
                        // 2. ATTACK
                        // ==========================================

                        PlayerFixtureAttack attack =
                                playerFixtureAttackRepository
                                        .findByAttackStats(stats)
                                        .orElse(
                                                new PlayerFixtureAttack()
                                        );

                        attack.setAttackStats(stats);

                        JsonNode shots =
                                statistics.path("shots");

                        JsonNode dribbles =
                                statistics.path("dribbles");

                        JsonNode goals =
                                statistics.path("goals");

                        attack.setGoals(goals.path("total").asInt(0));

                        attack.setAssists(goals.path("assists").asInt(0));

                        attack.setShotsTotal(
                                shots.path("total").asInt(0)
                        );

                        attack.setShotsOn(
                                shots.path("on").asInt(0)
                        );

                        attack.setOffsides(
                                statistics.path("offsides").asInt(0)
                        );

                        attack.setDribblesAttempts(
                                dribbles.path("attempts").asInt(0)
                        );

                        attack.setDribblesSuccess(
                                dribbles.path("success").asInt(0)
                        );

                        attack.setDribblesPast(
                                dribbles.path("past").asInt(0)
                        );

                        playerFixtureAttackRepository.save(attack);


                        // ==========================================
                        // 3. PASSING
                        // ==========================================

                        PlayerFixturePassing passing =
                                playerFixturePassingRepository
                                        .findByPassingStats(stats)
                                        .orElse(
                                                new PlayerFixturePassing()
                                        );
                        
                        passing.setPassingStats(stats);

                        JsonNode passes = statistics.path("passes");

                        passing.setPassesTotal(passes.path("total").asInt(0));

                        passing.setPassesKey(passes.path("key").asInt(0));

                        passing.setPassesAccuracy(passes.path("accuracy").asInt(0));

                        playerFixturePassingRepository.save(passing);


                        // ==========================================
                        // 4. DEFENSE
                        // ==========================================

                        PlayerFixtureDefense defense = playerFixtureDefenseRepository
                                .findByDefenseStats(stats)
                                .orElse(new PlayerFixtureDefense());

                        defense.setDefenseStats(stats);

                        JsonNode tackles = statistics.path("tackles");
                        JsonNode duels = statistics.path("duels");

                        defense.setTacklesTotal(tackles.path("total").asInt(0));
                        defense.setTacklesBlocks(tackles.path("blocks").asInt(0));
                        defense.setTacklesInterceptions(tackles.path("interceptions").asInt(0));
                        defense.setDuelsTotal(duels.path("total").asInt(0));
                        defense.setDuelsWon(duels.path("won").asInt(0));

                        playerFixtureDefenseRepository.save(defense);


                        // ==========================================
                        // 5. FOULS
                        // ==========================================

                        PlayerFixtureFouls fouls = playerFixtureFoulsRepository
                                .findByFoulsStats(stats)
                                .orElse(new PlayerFixtureFouls());

                        fouls.setFoulsStats(stats);

                        JsonNode foulsNode = statistics.path("fouls");

                        fouls.setFoulsDrawn(foulsNode.path("drawn").asInt(0));
                        fouls.setFoulsCommitted(foulsNode.path("committed").asInt(0));

                        playerFixtureFoulsRepository.save(fouls);


                        // ==========================================
                        // 6. CARDS
                        // ==========================================

                        PlayerFixtureCards cards = playerFixtureCardsRepository
                                .findByCardStats(stats)
                                .orElse(new PlayerFixtureCards());
                        
                        cards.setCardStats(stats);

                        JsonNode cardsNode = statistics.path("cards");

                        cards.setYellow(cardsNode.path("yellow").asInt(0));
                        cards.setRed(cardsNode.path("red").asInt(0));

                        playerFixtureCardsRepository.save(cards);


                        // ==========================================
                        // 7. GOALKEEPING
                        // ==========================================

                        PlayerFixtureGoalkeeping goalkeeping = playerFixtureGoalkeepingRepository
                                .findByGoalkeepStats(stats)
                                .orElse(new PlayerFixtureGoalkeeping());
                        
                        goalkeeping.setGoalkeepStats(stats);

                        JsonNode goalsNode = statistics.path("goals");

                        goalkeeping.setGoalsConceded(goalsNode.path("conceded").asInt(0));
                        goalkeeping.setGoalsSaves(goalsNode.path("saves").asInt(0));

                        playerFixtureGoalkeepingRepository.save(goalkeeping);


                        // ==========================================
                        // 8. PENALTY
                        // ==========================================

                        PlayerFixturePenalty penalty = playerFixturePenaltyRepository
                                .findByPenaltyStats(stats)
                                .orElse(new PlayerFixturePenalty());

                        penalty.setPenaltyStats(stats);

                        JsonNode penaltyNode = statistics.path("penalty");

                        penalty.setPenaltyWon(penaltyNode.path("won").asInt(0));
                        penalty.setPenaltyCommitted(penaltyNode.path("commited").asInt(0));
                        penalty.setPenaltyScored(penaltyNode.path("scored").asInt(0));
                        penalty.setPenaltyMissed(penaltyNode.path("missed").asInt(0));
                        penalty.setPenaltySaved(penaltyNode.path("saved").asInt(0));

                        playerFixturePenaltyRepository.save(penalty);

                        totalSaved++;

                        System.out.println(
                                "STATS GUARDADAS -> jugador: "
                                        + playerApiId
                                        + " | fixture: "
                                        + fixture.getApiId()
                        );
                }
                }

                // ==========================================
                // PAUSA ENTRE FIXTURES
                // ==========================================

                try {

                Thread.sleep(3000);

                } catch (InterruptedException e) {

                Thread.currentThread().interrupt();

                throw new RuntimeException(
                        "La sincronización fue interrumpida",
                        e
                );
                }
        }

        // ==========================================
        // RESUMEN
        // ==========================================

        System.out.println("==============================");
        System.out.println("RESUMEN DE SINCRONIZACIÓN");
        System.out.println("==============================");
        System.out.println("Fixtures: " + fixtures.size());
        System.out.println("Equipos: " + totalTeams);
        System.out.println("Jugadores: " + totalPlayers);
        System.out.println("Registros guardados: " + totalSaved);
        System.out.println("Omitidos: " + totalSkipped);
        System.out.println("==============================");
        }


}
