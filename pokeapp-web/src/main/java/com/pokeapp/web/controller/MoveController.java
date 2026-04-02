package com.pokeapp.web.controller;

import com.pokeapp.application.dto.MoveDto;
import com.pokeapp.application.dto.PagedResponse;
import com.pokeapp.application.exception.ResourceNotFoundException;
import com.pokeapp.application.service.MoveService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/moves")
@RequiredArgsConstructor
public class MoveController {

    private final MoveService moveService;

    @GetMapping
    public ResponseEntity<PagedResponse<MoveDto>> getAll(
            @RequestParam(required = false) String search,
            Pageable pageable) {
        return ResponseEntity.ok(moveService.findAll(search, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MoveDto> getById(@PathVariable Integer id) {
        MoveDto dto = moveService.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forMove(id));
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<MoveDto> getByName(@PathVariable String name) {
        MoveDto dto = moveService.findByName(name)
                .orElseThrow(() -> ResourceNotFoundException.forMove(name));
        return ResponseEntity.ok(dto);
    }
}