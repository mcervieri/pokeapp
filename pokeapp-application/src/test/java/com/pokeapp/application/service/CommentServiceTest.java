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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    TrainerCommentRepository commentRepository;
    @Mock
    TrainerRepository trainerRepository;
    @Mock
    PokemonRepository pokemonRepository;

    @InjectMocks
    CommentService commentService;

    private Trainer trainer() {
        return new Trainer("ash");
    }

    private Pokemon pokemon() {
        return new Pokemon(1, "bulbasaur", null, true, 7, 69, 64, null);
    }

    @Test
    void getCommentsForPokemon_returnsEmptyWhenNone() {
        when(commentRepository.findByPokemonIdOrderByCreatedAtDesc(1)).thenReturn(List.of());

        List<CommentDto> result = commentService.getCommentsForPokemon(1);

        assertThat(result).isEmpty();
    }

    @Test
    void createComment_savesAndReturnsDto() {
        Trainer trainer = trainer();
        Pokemon pokemon = pokemon();
        when(trainerRepository.findByUsername("ash")).thenReturn(Optional.of(trainer));
        when(pokemonRepository.findById(1)).thenReturn(Optional.of(pokemon));
        TrainerComment comment = new TrainerComment(trainer, pokemon, "Great pokemon!");
        when(commentRepository.save(any())).thenReturn(comment);

        CommentDto result = commentService.createComment(
                "ash", 1, new CreateCommentRequest("Great pokemon!"));

        assertThat(result.body()).isEqualTo("Great pokemon!");
        assertThat(result.username()).isEqualTo("ash");
        verify(commentRepository).save(any(TrainerComment.class));
    }

    @Test
    void createComment_throwsWhenPokemonNotFound() {
        when(trainerRepository.findByUsername("ash")).thenReturn(Optional.of(trainer()));
        when(pokemonRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.createComment(
                "ash", 999, new CreateCommentRequest("test")))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateComment_throwsWhenNotOwned() {
        when(trainerRepository.findByUsername("ash")).thenReturn(Optional.of(trainer()));
        when(commentRepository.findByIdAndTrainerId(any(), any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.updateComment(
                "ash", 99, new UpdateCommentRequest("new body")))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void deleteComment_throwsWhenNotOwned() {
        when(trainerRepository.findByUsername("ash")).thenReturn(Optional.of(trainer()));
        when(commentRepository.findByIdAndTrainerId(any(), any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.deleteComment("ash", 99))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void deleteComment_deletesWhenOwned() {
        Trainer trainer = trainer();
        Pokemon pokemon = pokemon();
        TrainerComment comment = new TrainerComment(trainer, pokemon, "nice");
        when(trainerRepository.findByUsername("ash")).thenReturn(Optional.of(trainer));
        when(commentRepository.findByIdAndTrainerId(any(), any()))
                .thenReturn(Optional.of(comment));

        commentService.deleteComment("ash", 1);

        verify(commentRepository).delete(comment);
    }
}