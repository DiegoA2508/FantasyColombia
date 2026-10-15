package com.fantasy.ligabetplay.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fantasy.ligabetplay.entity.PlayerFixtureFouls;
import com.fantasy.ligabetplay.entity.PlayerFixtureStats;

public interface PlayerFixtureFoulsRepository extends JpaRepository<PlayerFixtureFouls, Long> {

    Optional<PlayerFixtureFouls> findByFoulsStats(PlayerFixtureStats stats);

}
