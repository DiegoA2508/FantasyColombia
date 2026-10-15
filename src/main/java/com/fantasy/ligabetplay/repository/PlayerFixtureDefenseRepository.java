package com.fantasy.ligabetplay.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fantasy.ligabetplay.entity.PlayerFixtureDefense;
import com.fantasy.ligabetplay.entity.PlayerFixtureStats;

public interface PlayerFixtureDefenseRepository extends JpaRepository<PlayerFixtureDefense, Long> {

    Optional<PlayerFixtureDefense> findByDefenseStats(PlayerFixtureStats stats);

}
