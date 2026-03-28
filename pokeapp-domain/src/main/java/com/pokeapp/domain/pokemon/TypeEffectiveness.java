package com.pokeapp.domain.pokemon;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "type_effectiveness")
public class TypeEffectiveness {

    @EmbeddedId
    private TypeEffectivenessId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("attackingTypeId")
    @JoinColumn(name = "attacking_type_id")
    private Type attackingType;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("defendingTypeId")
    @JoinColumn(name = "defending_type_id")
    private Type defendingType;

    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal multiplier;

    public TypeEffectiveness() {
    }

    public TypeEffectiveness(Type attackingType, Type defendingType, BigDecimal multiplier) {
        this.attackingType = attackingType;
        this.defendingType = defendingType;
        this.id = new TypeEffectivenessId(attackingType.getId(), defendingType.getId());
        this.multiplier = multiplier;
    }

    public TypeEffectivenessId getId() {
        return id;
    }

    public Type getAttackingType() {
        return attackingType;
    }

    public Type getDefendingType() {
        return defendingType;
    }

    public BigDecimal getMultiplier() {
        return multiplier;
    }
}