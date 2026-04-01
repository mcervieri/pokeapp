package com.pokeapp.web.controller;

import com.pokeapp.application.dto.MoveDto;
import com.pokeapp.application.service.MoveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/moves")
@RequiredArgsConstructor
public class MoveController {

    private final MoveService moveService;

    @GetMapping
    public ResponseEntity<List<MoveDto>> getAll() {
        return ResponseEntity.ok(moveService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MoveDto> getById(@PathVariable Integer id) {
        return moveService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<MoveDto> getByName(@PathVariable String name) {
        return moveService.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}