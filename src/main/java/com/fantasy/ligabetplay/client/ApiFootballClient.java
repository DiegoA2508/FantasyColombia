package com.fantasy.ligabetplay.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ApiFootballClient {

    private final RestClient restClient;

    public ApiFootballClient(RestClient apiFootballRestClient) {
        this.restClient = apiFootballRestClient;
    }

    /**
     * Countries: cubre los 4 casos de uso que expone la API.
     * GET /countries                       (todos)
     * GET /countries?name={name}           (por nombre exacto)
     * GET /countries?code={code}           (por codigo ISO, ej: FR)
     * GET /countries?search={search}       (busqueda parcial, ej: "engl")
     * Los tres parametros son opcionales y mutuamente independientes:
     * se envia al endpoint solo el que venga con valor.
     */
    public JsonNode getCountries(String name, String code, String search) {
        return restClient.get()
                .uri(uriBuilder -> {
                    var b = uriBuilder.path("/countries");
                    if (name != null && !name.isBlank()) {
                        b.queryParam("name", name);
                    }
                    if (code != null && !code.isBlank()) {
                        b.queryParam("code", code);
                    }
                    if (search != null && !search.isBlank()) {
                        b.queryParam("search", search);
                    }
                    return b.build();
                })
                .retrieve()
                .body(JsonNode.class);
    }

    /**
     * Paso 1: buscar la liga por nombre para identificar su ID.
     * GET /leagues?search=Betplay  (o ?country=Colombia)
     */
    public JsonNode searchLeagues(String search, String country) {
        return restClient.get()
                .uri(uriBuilder -> {
                    var b = uriBuilder.path("/leagues");
                    if (search != null && !search.isBlank()) {
                        b.queryParam("search", search);
                    }
                    if (country != null && !country.isBlank()) {
                        b.queryParam("country", country);
                    }
                    return b.build();
                })
                .retrieve()
                .body(JsonNode.class);
    }

    /**
     * Paso 2: consultar los partidos (fixtures) de una liga en una temporada dada.
     * GET /fixtures?league={leagueId}&season={season}
     */
    public JsonNode getFixturesByLeagueAndSeason(int leagueId, int season) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/fixtures")
                        .queryParam("league", leagueId)
                        .queryParam("season", season)
                        .build())
                .retrieve()
                .body(JsonNode.class);
    }

    /**
     * Variante util: consultar un fixture puntual por su ID.
     * GET /fixtures?id={fixtureId}
     */
    public JsonNode getFixtureById(long fixtureId) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/fixtures")
                        .queryParam("id", fixtureId)
                        .build())
                .retrieve()
                .body(JsonNode.class);
    }

    /**
     * Paso 3: estadisticas individuales de los jugadores de un partido.
     * GET /fixtures/players?fixture={fixtureId}
     */
    public JsonNode getPlayerStatsByFixture(long fixtureId) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/fixtures/players")
                        .queryParam("fixture", fixtureId)
                        .build())
                .retrieve()
                .body(JsonNode.class);
    }


    /**
     * Paso 4: Consultar los equipos de una liga en una temporada dada 
     * GET /teams?league={leagueId}&season={season}
     */
    public JsonNode getTeamByLeagueAndSeason(int leagueId, int season){
        return restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/teams")
                        .queryParam("league", leagueId)
                        .queryParam("season", season)
                        .build())
                .retrieve()
                .body(JsonNode.class);
    }

    /**
     * Paso 5: Consultar los jugadores de una liga en una temporada dada 
         * GET /players?league={leagueId}&season={season}
     */
    public JsonNode getPlayersByLeagueAndSeason(int leagueId, int season){
        return restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/players")
                        .queryParam("league", leagueId)
                        .queryParam("season", season)
                        .build())
                .retrieve()
                .body(JsonNode.class);
    }

    /**
     * Paso 6: Consultar los jugadores de un partido dado
     * GET /fixtures/players?fixture={fixtureId}
     */
    public JsonNode getPlayersByFixture(long fixtureId){
        return restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/fixtures/players")
                .queryParam("fixture", fixtureId)
                        .build())
                .retrieve()
                .body(JsonNode.class);
    }

}
