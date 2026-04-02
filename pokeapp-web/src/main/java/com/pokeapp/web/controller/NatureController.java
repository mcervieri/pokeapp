package com.pokeapp.web.controller;

import com.pokeapp.application.dto.NatureDto;
import com.pokeapp.application.dto.PagedResponse;
import com.pokeapp.application.exception.ResourceNotFoundException;
import com.pokeapp.application.service.NatureService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/natures")
@RequiredArgsConstructor
public class NatureController {

    private final NatureService natureService;

    @GetMapping
    public ResponseEntity<PagedResponse<NatureDto>> getAll(
            @RequestParam(required = false) String search,
            Pageable pageable) {
        return ResponseEntity.ok(natureService.findAll(search, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NatureDto> getById(@PathVariable Integer id) {
        NatureDto dto = natureService.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forNature(id));
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<NatureDto> getByName(@PathVariable String name) {
        NatureDto dto = natureService.findByName(name)
                .orElseThrow(() -> ResourceNotFoundException.forNature(name));
        return ResponseEntity.ok(dto);
    }
}