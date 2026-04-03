package com.pokeapp.application.service;

import com.pokeapp.application.dto.user.CommentDto;
import com.pokeapp.application.dto.user.CreateCommentRequest;
import com.pokeapp.application.dto.user.UpdateCommentRequest;
import com.pokeapp.application.exception.ResourceNotFoundException;
import com.pokeapp.application.repository.PokemonRepository;
import com.pokeapp.application.repository.TrainerCommentRepository;
import com.pokeapp.application.repository.TrainerRepository;
import com.pokeapp.domain.pokemon.Pokemon;
import com.pokeapp.domain.trainer.Trainer;
import com.pokeapp.domain.trainer.TrainerComment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CommentService {

    private final TrainerCommentRepository commentRepository;
    private final TrainerRepository trainerRepository;
    private final PokemonRepository pokemonRepository;

    public CommentService(TrainerCommentRepository commentRepository,
            TrainerRepository trainerRepository,
            PokemonRepository pokemonRepository) {
        this.commentRepository = commentRepository;
        this.trainerRepository = trainerRepository;
        this.pokemonRepository = pokemonRepository;
    }

    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsForPokemon(Integer pokemonId) {
        return commentRepository.findByPokemonIdOrderByCreatedAtDesc(pokemonId).stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public CommentDto createComment(String username, Integer pokemonId,
            CreateCommentRequest request) {
        Trainer trainer = resolveTrainer(username);
        Pokemon pokemon = pokemonRepository.findById(pokemonId)
                .orElseThrow(() -> ResourceNotFoundException.forPokemon(pokemonId));
        TrainerComment comment = new TrainerComment(trainer, pokemon, request.body());
        return toDto(commentRepository.save(comment));
    }

    @Transactional
    public CommentDto updateComment(String username, Integer commentId,
            UpdateCommentRequest request) {
        Trainer trainer = resolveTrainer(username);
        TrainerComment comment = commentRepository
                .findByIdAndTrainerId(commentId, trainer.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.FORBIDDEN, "Comment not found or not owned by you"));
        comment.updateBody(request.body());
        return toDto(commentRepository.save(comment));
    }

    @Transactional
    public void deleteComment(String username, Integer commentId) {
        Trainer trainer = resolveTrainer(username);
        TrainerComment comment = commentRepository
                .findByIdAndTrainerId(commentId, trainer.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.FORBIDDEN, "Comment not found or not owned by you"));
        commentRepository.delete(comment);
    }

    private Trainer resolveTrainer(String username) {
        return trainerRepository.findByUsername(username)
                .orElseThrow(() -> ResourceNotFoundException.forTrainer(username));
    }

    private CommentDto toDto(TrainerComment c) {
        return new CommentDto(
                c.getId(),
                c.getPokemon().getId(),
                c.getTrainer().getUsername(),
                c.getBody(),
                c.getCreatedAt(),
                c.getUpdatedAt());
    }
}