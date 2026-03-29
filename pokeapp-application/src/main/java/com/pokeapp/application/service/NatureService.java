package com.pokeapp.application.service;

import com.pokeapp.application.dto.NatureDto;
import com.pokeapp.domain.pokemon.Nature;
import com.pokeapp.application.repository.NatureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NatureService {

    private final NatureRepository natureRepository;

    @Transactional(readOnly = true)
    public List<NatureDto> findAll() {
        return natureRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<NatureDto> findById(Integer id) {
        return natureRepository.findById(id)
                .map(this::toDto);
    }

    private NatureDto toDto(Nature n) {
        return new NatureDto(
                n.getId(),
                n.getName(),
                n.getIncreasedStat() != null ? n.getIncreasedStat().getName() : null,
                n.getDecreasedStat() != null ? n.getDecreasedStat().getName() : null);
    }
}