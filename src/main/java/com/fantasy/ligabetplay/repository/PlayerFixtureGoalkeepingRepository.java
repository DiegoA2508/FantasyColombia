package com.fantasy.ligabetplay.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fantasy.ligabetplay.entity.PlayerFixtureGoalkeeping;
import com.fantasy.ligabetplay.entity.PlayerFixtureStats;

public interface PlayerFixtureGoalkeepingRepository extends JpaRepository<PlayerFixtureGoalkeeping, Long> {

    Optional<PlayerFixtureGoalkeeping> findByGoalkeepStats(PlayerFixtureStats stats);

}
