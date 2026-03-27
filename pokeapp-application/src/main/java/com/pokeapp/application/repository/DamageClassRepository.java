package com.pokeapp.application.repository;

import com.pokeapp.domain.move.DamageClass;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DamageClassRepository extends JpaRepository<DamageClass, Integer> {
    Optional<DamageClass> findByName(String name);
}