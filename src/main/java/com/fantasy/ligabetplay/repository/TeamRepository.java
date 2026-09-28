package com.fantasy.ligabetplay.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fantasy.ligabetplay.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long>{

    Optional<Team> findByApiId(Integer apiId);
}