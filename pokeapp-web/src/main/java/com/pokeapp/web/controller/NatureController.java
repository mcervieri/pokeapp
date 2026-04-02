package com.pokeapp.web.controller;

import com.pokeapp.application.dto.NatureDto;
import com.pokeapp.application.dto.PagedResponse;
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
        return natureService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<NatureDto> getByName(@PathVariable String name) {
        return natureService.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}