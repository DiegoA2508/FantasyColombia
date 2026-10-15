package com.fantasy.ligabetplay.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fantasy.ligabetplay.entity.PlayerFixturePassing;
import com.fantasy.ligabetplay.entity.PlayerFixtureStats;

public interface PlayerFixturePassingRepository extends JpaRepository<PlayerFixturePassing, Long> {

    Optional<PlayerFixturePassing> findByPassingStats(PlayerFixtureStats stats);

}
