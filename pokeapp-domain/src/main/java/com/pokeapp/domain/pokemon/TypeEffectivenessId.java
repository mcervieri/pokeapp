package com.pokeapp.domain.pokemon;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TypeEffectivenessId implements Serializable {

    private Integer attackingTypeId;
    private Integer defendingTypeId;

    protected TypeEffectivenessId() {
    }

    public TypeEffectivenessId(Integer attackingTypeId, Integer defendingTypeId) {
        this.attackingTypeId = attackingTypeId;
        this.defendingTypeId = defendingTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof TypeEffectivenessId that))
            return false;
        return Objects.equals(attackingTypeId, that.attackingTypeId)
                && Objects.equals(defendingTypeId, that.defendingTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attackingTypeId, defendingTypeId);
    }

    public Integer getAttackingTypeId() {
        return attackingTypeId;
    }

    public Integer getDefendingTypeId() {
        return defendingTypeId;
    }
}