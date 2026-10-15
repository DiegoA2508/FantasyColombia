package com.fantasy.ligabetplay.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fantasy.ligabetplay.entity.PlayerFixtureCards;
import com.fantasy.ligabetplay.entity.PlayerFixtureStats;

public interface PlayerFixtureCardsRepository extends JpaRepository<PlayerFixtureCards, Long>{

    Optional<PlayerFixtureCards> findByCardStats(PlayerFixtureStats stats);

}
