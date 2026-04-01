package com.pokeapp.web.controller;

import com.pokeapp.application.dto.NatureDto;
import com.pokeapp.application.service.NatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/natures")
@RequiredArgsConstructor
public class NatureController {

    private final NatureService natureService;

    @GetMapping
    public ResponseEntity<List<NatureDto>> getAll() {
        return ResponseEntity.ok(natureService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NatureDto> getById(@PathVariable Integer id) {
        return natureService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}