package com.pokeapp.application.service;

import com.pokeapp.application.dto.MoveDto;
import com.pokeapp.domain.move.Move;
import com.pokeapp.application.repository.MoveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MoveService {

    private final MoveRepository moveRepository;

    @Transactional(readOnly = true)
    public List<MoveDto> findAll() {
        return moveRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<MoveDto> findById(Integer id) {
        return moveRepository.findById(id)
                .map(this::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<MoveDto> findByName(String name) {
        return moveRepository.findByName(name)
                .map(this::toDto);
    }

    private MoveDto toDto(Move m) {
        return new MoveDto(
                m.getId(),
                m.getName(),
                m.getType() != null ? m.getType().getName() : null,
                m.getDamageClass() != null ? m.getDamageClass().getName() : null,
                m.getPower(),
                m.getAccuracy(),
                m.getPp());
    }
}