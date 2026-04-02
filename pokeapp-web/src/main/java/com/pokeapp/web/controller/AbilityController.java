package com.pokeapp.web.controller;

import com.pokeapp.application.dto.AbilityDto;
import com.pokeapp.application.dto.PagedResponse;
import com.pokeapp.application.service.AbilityService;
import com.pokeapp.web.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/abilities")
@RequiredArgsConstructor
public class AbilityController {

    private final AbilityService abilityService;

    @GetMapping
    public ResponseEntity<PagedResponse<AbilityDto>> getAll(
            @RequestParam(required = false) String search,
            Pageable pageable) {
        return ResponseEntity.ok(abilityService.findAll(search, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AbilityDto> getById(@PathVariable Integer id) {
        AbilityDto dto = abilityService.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forAbility(id));
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<AbilityDto> getByName(@PathVariable String name) {
        AbilityDto dto = abilityService.findByName(name)
                .orElseThrow(() -> ResourceNotFoundException.forAbility(name));
        return ResponseEntity.ok(dto);
    }
}