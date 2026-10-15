package com.fantasy.ligabetplay.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fantasy.ligabetplay.entity.PlayerFixturePenalty;
import com.fantasy.ligabetplay.entity.PlayerFixtureStats;

public interface PlayerFixturePenaltyRepository extends JpaRepository<PlayerFixturePenalty, Long> {

    Optional<PlayerFixturePenalty> findByPenaltyStats(PlayerFixtureStats stats);

}
