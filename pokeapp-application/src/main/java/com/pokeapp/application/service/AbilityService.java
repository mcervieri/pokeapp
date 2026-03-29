package com.pokeapp.application.service;

import com.pokeapp.application.dto.AbilityDto;
import com.pokeapp.domain.pokemon.Ability;
import com.pokeapp.application.repository.AbilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AbilityService {

    private final AbilityRepository abilityRepository;

    @Transactional(readOnly = true)
    public List<AbilityDto> findAll() {
        return abilityRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<AbilityDto> findById(Integer id) {
        return abilityRepository.findById(id)
                .map(this::toDto);
    }

    private AbilityDto toDto(Ability a) {
        return new AbilityDto(
                a.getId(),
                a.getName(),
                a.getEffectText());
    }
}