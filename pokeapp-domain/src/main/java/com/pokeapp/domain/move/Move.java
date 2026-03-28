package com.pokeapp.domain.move;

import com.pokeapp.domain.pokemon.Type;
import jakarta.persistence.*;

@Entity
@Table(name = "move")
public class Move {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pokeapi_id", nullable = false, unique = true)
    private Integer pokeapiId;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    private Type type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "damage_class_id", nullable = false)
    private DamageClass damageClass;

    @Column
    private Integer power; // NULL for status moves

    @Column
    private Integer accuracy; // NULL for moves that never miss

    @Column
    private Integer pp;

    @Column(nullable = false)
    private Integer priority;

    @Column(name = "effect_text", columnDefinition = "TEXT")
    private String effectText;

    protected Move() {
    }

    public Move(Integer pokeapiId, String name, Type type, DamageClass damageClass,
            Integer power, Integer accuracy, Integer pp,
            Integer priority, String effectText) {
        this.pokeapiId = pokeapiId;
        this.name = name;
        this.type = type;
        this.damageClass = damageClass;
        this.power = power;
        this.accuracy = accuracy;
        this.pp = pp;
        this.priority = priority;
        this.effectText = effectText;
    }

    public Integer getId() {
        return id;
    }

    public Integer getPokeapiId() {
        return pokeapiId;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public DamageClass getDamageClass() {
        return damageClass;
    }

    public Integer getPower() {
        return power;
    }

    public Integer getAccuracy() {
        return accuracy;
    }

    public Integer getPp() {
        return pp;
    }

    public Integer getPriority() {
        return priority;
    }

    public String getEffectText() {
        return effectText;
    }
}