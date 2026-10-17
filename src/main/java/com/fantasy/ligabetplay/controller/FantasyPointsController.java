package com.fantasy.ligabetplay.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fantasy.ligabetplay.service.FantasyPointsService;

@RestController 
@RequestMapping("/api/fantasy") 
public class FantasyPointsController {

    private final FantasyPointsService fantasyPointsService;

    public FantasyPointsController(FantasyPointsService fantasyPointsService) {
        this.fantasyPointsService = fantasyPointsService;
    }

    @PostMapping("recalculate-points")
    public Map<String, Object> recalculatePoints(){

        int processed = fantasyPointsService.recalculateAllPoints();

        return Map.of("message", "Puntos Fantasy recalculados correctamente", "processedRecords", processed);
    }

}
