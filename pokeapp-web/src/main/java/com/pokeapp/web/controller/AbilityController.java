package com.pokeapp.web.controller;

import com.pokeapp.application.dto.AbilityDto;
import com.pokeapp.application.service.AbilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/abilities")
@RequiredArgsConstructor
public class AbilityController {

    private final AbilityService abilityService;

    @GetMapping
    public ResponseEntity<List<AbilityDto>> getAll() {
        return ResponseEntity.ok(abilityService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AbilityDto> getById(@PathVariable Integer id) {
        return abilityService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}