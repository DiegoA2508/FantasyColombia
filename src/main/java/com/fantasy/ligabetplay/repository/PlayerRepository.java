package com.fantasy.ligabetplay.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fantasy.ligabetplay.entity.Player;

public interface PlayerRepository extends JpaRepository<Player, Long>{

    Optional<Player> findByApiId(Integer apiId);
}
