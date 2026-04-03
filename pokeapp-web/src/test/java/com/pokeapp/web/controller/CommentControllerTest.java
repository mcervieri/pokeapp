package com.pokeapp.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokeapp.application.dto.user.CommentDto;
import com.pokeapp.application.dto.user.CreateCommentRequest;
import com.pokeapp.application.dto.user.UpdateCommentRequest;
import com.pokeapp.application.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    CommentService commentService;

    private CommentDto sampleComment() {
        return new CommentDto(1, 1, "ash", "Great pokemon!", OffsetDateTime.now(), OffsetDateTime.now());
    }

    @Test
    @WithMockUser(username = "ash")
    void getComments_returns200() throws Exception {
        when(commentService.getCommentsForPokemon(1)).thenReturn(List.of(sampleComment()));

        mockMvc.perform(get("/api/v1/pokemon/1/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].body").value("Great pokemon!"))
                .andExpect(jsonPath("$[0].username").value("ash"));
    }

    @Test
    @WithMockUser(username = "ash")
    void createComment_returns201() throws Exception {
        when(commentService.createComment(eq("ash"), eq(1), any(CreateCommentRequest.class)))
                .thenReturn(sampleComment());

        mockMvc.perform(post("/api/v1/pokemon/1/comments")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CreateCommentRequest("Great pokemon!"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.body").value("Great pokemon!"));
    }

    @Test
    @WithMockUser(username = "ash")
    void updateComment_returns200() throws Exception {
        CommentDto updated = new CommentDto(1, 1, "ash", "Updated body",
                OffsetDateTime.now(), OffsetDateTime.now());
        when(commentService.updateComment(eq("ash"), eq(1), any(UpdateCommentRequest.class)))
                .thenReturn(updated);

        mockMvc.perform(patch("/api/v1/comments/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UpdateCommentRequest("Updated body"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").value("Updated body"));
    }

    @Test
    @WithMockUser(username = "ash")
    void deleteComment_returns204() throws Exception {
        doNothing().when(commentService).deleteComment("ash", 1);

        mockMvc.perform(delete("/api/v1/comments/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    void getComments_unauthenticated_returns401() throws Exception {
        mockMvc.perform(get("/api/v1/pokemon/1/comments"))
                .andExpect(status().isUnauthorized());
    }
}