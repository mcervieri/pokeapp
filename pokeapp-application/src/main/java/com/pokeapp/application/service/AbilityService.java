package com.pokeapp.application.service;

import com.pokeapp.application.dto.AbilityDto;
import com.pokeapp.application.dto.PagedResponse;
import com.pokeapp.domain.pokemon.Ability;
import com.pokeapp.application.repository.AbilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AbilityService {

    private final AbilityRepository abilityRepository;

    @Transactional(readOnly = true)
    public PagedResponse<AbilityDto> findAll(String search, Pageable pageable) {
        return PagedResponse.from(
                abilityRepository.findBySearch(search, pageable).map(this::toDto));
    }

    @Transactional(readOnly = true)
    public Optional<AbilityDto> findById(Integer id) {
        return abilityRepository.findById(id).map(this::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<AbilityDto> findByName(String name) {
        return abilityRepository.findByName(name).map(this::toDto);
    }

    private AbilityDto toDto(Ability a) {
        return new AbilityDto(a.getId(), a.getName(), a.getEffectText());
    }
}