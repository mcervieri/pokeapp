package com.pokeapp.application.repository;

import com.pokeapp.domain.pokemon.TypeEffectiveness;
import com.pokeapp.domain.pokemon.TypeEffectivenessId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TypeEffectivenessRepository extends JpaRepository<TypeEffectiveness, TypeEffectivenessId> {
    List<TypeEffectiveness> findByAttackingTypeId(Integer attackingTypeId);

    List<TypeEffectiveness> findByDefendingTypeId(Integer defendingTypeId);
}