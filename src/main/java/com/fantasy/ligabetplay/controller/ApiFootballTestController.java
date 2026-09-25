package com.fantasy.ligabetplay.controller;

import com.fantasy.ligabetplay.client.ApiFootballClient;
import com.fantasy.ligabetplay.config.ApiFootballProperties;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiFootballTestController {

    private final ApiFootballClient client;
    private final ApiFootballProperties properties;

    public ApiFootballTestController(ApiFootballClient client, ApiFootballProperties properties) {
        this.client = client;
        this.properties = properties;
    }

    /**
     * Countries — refleja los 4 casos de uso de la API:
     * GET /api/countries                     -> todos los paises disponibles
     * GET /api/countries?name=england         -> por nombre exacto
     * GET /api/countries?code=fr              -> por codigo ISO
     * GET /api/countries?search=engl          -> busqueda parcial
     * (Colombia: GET /api/countries?name=Colombia)
     */
    @GetMapping("/countries")
    public JsonNode getCountries(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String search) {
        return client.getCountries(name, code, search);
    }

    /**
     * PASO 1: Test de la API + identificar el ID de la Liga BetPlay.
     * Ej: GET /api/leagues/search?name=Betplay
     *     GET /api/leagues/search?country=Colombia
     */
    @GetMapping("/leagues/search")
    public JsonNode searchLeagues(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String country) {
        return client.searchLeagues(name, country);
    }

    /**
     * Atajo con el ID ya configurado en application.yml (liga-betplay-id).
     * Util para confirmar rapido cual quedo detectado.
     */
    @GetMapping("/leagues/liga-betplay/id")
    public Integer getLigaBetplayIdConfigurado() {
        return properties.ligaBetplayId();
    }

    /**
     * PASO 2: Consultar los partidos de la Liga BetPlay en una temporada.
     * Ej: GET /api/fixtures?season=2026
     *     GET /api/fixtures?leagueId=239&season=2026
     */
    @GetMapping("/fixtures")
    public JsonNode getFixtures(
            @RequestParam(required = false) Integer leagueId,
            @RequestParam int season) {
        int idLiga = (leagueId != null) ? leagueId : properties.ligaBetplayId();
        return client.getFixturesByLeagueAndSeason(idLiga, season);
    }

    /** Detalle puntual de un fixture (util para copiar el ID exacto). */
    @GetMapping("/fixtures/{fixtureId}")
    public JsonNode getFixture(@PathVariable long fixtureId) {
        return client.getFixtureById(fixtureId);
    }

    /**
     * PASO 3: Estadisticas individuales de los jugadores de un partido especifico.
     * Ej: GET /api/fixtures/718543/players
     */
    @GetMapping("/fixtures/{fixtureId}/players")
    public JsonNode getPlayerStats(@PathVariable long fixtureId) {
        return client.getPlayerStatsByFixture(fixtureId);
    }
}
