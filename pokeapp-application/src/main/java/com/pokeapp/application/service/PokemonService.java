package com.pokeapp.application.service;

import com.pokeapp.application.dto.PokemonDto;
import com.pokeapp.application.dto.StatValueDto;
import com.pokeapp.domain.pokemon.Pokemon;
import com.pokeapp.application.repository.PokemonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PokemonService {

    private final PokemonRepository pokemonRepository;

    @Transactional(readOnly = true)
    public List<PokemonDto> findAll() {
        return pokemonRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<PokemonDto> findById(Integer id) {
        return pokemonRepository.findById(id)
                .map(this::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<PokemonDto> findByName(String name) {
        return pokemonRepository.findByName(name)
                .map(this::toDto);
    }

    private PokemonDto toDto(Pokemon p) {
        List<String> types = p.getPokemonTypes()
                .stream()
                .map(pt -> pt.getType().getName())
                .toList();

        List<StatValueDto> stats = p.getPokemonStats()
                .stream()
                .map(ps -> new StatValueDto(
                        ps.getStat().getName(),
                        ps.getBaseValue()))
                .toList();

        return new PokemonDto(
                p.getId(),
                p.getName(),
                p.getHeightDm(),
                p.getWeightHg(),
                p.getBaseExperience(),
                types,
                stats);
    }
}