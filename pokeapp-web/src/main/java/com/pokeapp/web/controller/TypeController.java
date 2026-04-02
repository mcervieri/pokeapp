package com.pokeapp.web.controller;

import com.pokeapp.application.dto.PagedResponse;
import com.pokeapp.application.dto.TypeDto;
import com.pokeapp.application.dto.TypeMatchupDto;
import com.pokeapp.application.service.TypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/types")
@RequiredArgsConstructor
public class TypeController {

    private final TypeService typeService;

    @GetMapping
    public ResponseEntity<PagedResponse<TypeDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(typeService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeDto> getById(@PathVariable Integer id) {
        return typeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/matchups")
    public ResponseEntity<List<TypeMatchupDto>> getMatchups(@PathVariable Integer id) {
        return ResponseEntity.ok(typeService.getMatchups(id));
    }
}