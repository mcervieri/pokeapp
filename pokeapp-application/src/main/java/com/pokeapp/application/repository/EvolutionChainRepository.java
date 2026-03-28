package com.pokeapp.application.repository;

import com.pokeapp.domain.pokemon.EvolutionChain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvolutionChainRepository extends JpaRepository<EvolutionChain, Integer> {
}