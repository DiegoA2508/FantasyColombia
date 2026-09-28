package com.fantasy.ligabetplay.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fantasy.ligabetplay.entity.Fixture;

public interface FixtureRepository extends JpaRepository<Fixture, Long>{

    Optional<Fixture> findByApiId(Integer apiId);
}
