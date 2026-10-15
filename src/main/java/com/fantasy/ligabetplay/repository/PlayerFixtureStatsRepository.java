package com.fantasy.ligabetplay.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fantasy.ligabetplay.entity.Fixture;
import com.fantasy.ligabetplay.entity.Player;
import com.fantasy.ligabetplay.entity.PlayerFixtureStats;

public interface PlayerFixtureStatsRepository extends JpaRepository<PlayerFixtureStats, Long> {

    Optional<PlayerFixtureStats> findByPlayerAndFixture(Player player, Fixture fixture);

}
