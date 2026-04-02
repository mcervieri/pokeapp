package com.pokeapp.application.service;

import com.pokeapp.application.dto.PagedResponse;
import com.pokeapp.application.dto.TypeDto;
import com.pokeapp.application.dto.TypeMatchupDto;
import com.pokeapp.domain.pokemon.Type;
import com.pokeapp.application.repository.TypeEffectivenessRepository;
import com.pokeapp.application.repository.TypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
    public PagedResponse<TypeDto> findAll(Pageable pageable) {
        return PagedResponse.from(
                typeRepository.findAll(pageable).map(this::toDto));
    }

    @Transactional(readOnly = true)
    public Optional<TypeDto> findById(Integer id) {
        return typeRepository.findById(id).map(this::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<TypeDto> findByName(String name) {
        return typeRepository.findByName(name).map(this::toDto);
    }

    @Transactional(readOnly = true)
    public List<TypeMatchupDto> getMatchups(Integer typeId) {
        return typeEffectivenessRepository
                .findByAttackingTypeId(typeId)
                .stream()
                .map(e -> new TypeMatchupDto(
                        e.getDefendingType().getName(),
                        e.getMultiplier()))
                .toList();
    }

    private TypeDto toDto(Type t) {
        return new TypeDto(t.getId(), t.getName(), getMatchups(t.getId()));
    }
}