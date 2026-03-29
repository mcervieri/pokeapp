package com.pokeapp.application.service;

import com.pokeapp.application.dto.TypeDto;
import com.pokeapp.application.dto.TypeMatchupDto;
import com.pokeapp.domain.pokemon.Type;
import com.pokeapp.application.repository.TypeEffectivenessRepository;
import com.pokeapp.application.repository.TypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TypeService {

    private final TypeRepository typeRepository;
    private final TypeEffectivenessRepository typeEffectivenessRepository;

    @Transactional(readOnly = true)
    public List<TypeDto> findAll() {
        return typeRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<TypeDto> findById(Integer id) {
        return typeRepository.findById(id)
                .map(this::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<TypeDto> findByName(String name) {
        return typeRepository.findByName(name)
                .map(this::toDto);
    }

    private TypeDto toDto(Type t) {
        List<TypeMatchupDto> matchups = typeEffectivenessRepository
                .findByAttackingTypeId(t.getId())
                .stream()
                .map(e -> new TypeMatchupDto(
                        e.getDefendingType().getName(),
                        e.getMultiplier()))
                .toList();

        return new TypeDto(t.getId(), t.getName(), matchups);
    }
}