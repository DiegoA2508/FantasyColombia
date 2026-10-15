package com.fantasy.ligabetplay.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fantasy.ligabetplay.entity.PlayerFixtureAttack;
import com.fantasy.ligabetplay.entity.PlayerFixtureStats;

public interface PlayerFixtureAttackRepository extends JpaRepository<PlayerFixtureAttack, Long> {

    Optional<PlayerFixtureAttack> findByAttackStats(PlayerFixtureStats stats);
}
