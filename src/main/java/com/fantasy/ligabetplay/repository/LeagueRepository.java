package com.fantasy.ligabetplay.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fantasy.ligabetplay.entity.League;

public interface LeagueRepository extends JpaRepository<League, Long> {

    Optional<League> findByApiId(Integer apiId);
}
