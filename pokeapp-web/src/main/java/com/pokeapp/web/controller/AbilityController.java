package com.pokeapp.web.controller;

import com.pokeapp.application.dto.AbilityDto;
import com.pokeapp.application.dto.PagedResponse;
import com.pokeapp.application.service.AbilityService;
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
        return abilityService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<AbilityDto> getByName(@PathVariable String name) {
        return abilityService.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}