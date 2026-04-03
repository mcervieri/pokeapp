package com.pokeapp.web.controller;

import com.pokeapp.application.dto.user.CommentDto;
import com.pokeapp.application.dto.user.CreateCommentRequest;
import com.pokeapp.application.dto.user.UpdateCommentRequest;
import com.pokeapp.application.service.CommentService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Comments", description = "Pokémon comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/pokemon/{pokemonId}/comments")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Integer pokemonId) {
        return ResponseEntity.ok(commentService.getCommentsForPokemon(pokemonId));
    }

    @PostMapping("/pokemon/{pokemonId}/comments")
    public ResponseEntity<CommentDto> createComment(
            @PathVariable Integer pokemonId,
            @Valid @RequestBody CreateCommentRequest request,
            Authentication authentication) {
        CommentDto created = commentService.createComment(
                authentication.getName(), pokemonId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable Integer commentId,
            @Valid @RequestBody UpdateCommentRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(
                commentService.updateComment(authentication.getName(), commentId, request));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Integer commentId,
            Authentication authentication) {
        commentService.deleteComment(authentication.getName(), commentId);
        return ResponseEntity.noContent().build();
    }
}