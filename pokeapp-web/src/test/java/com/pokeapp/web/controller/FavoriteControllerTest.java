package com.pokeapp.web.controller;

import com.pokeapp.application.dto.user.FavoriteDto;
import com.pokeapp.application.service.FavoriteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FavoriteController.class)
class FavoriteControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    FavoriteService favoriteService;

    @Test
    @WithMockUser(username = "ash")
    void addFavorite_returns204() throws Exception {
        doNothing().when(favoriteService).addFavorite("ash", 1);

        mockMvc.perform(post("/api/v1/favorites/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "ash")
    void removeFavorite_returns204() throws Exception {
        doNothing().when(favoriteService).removeFavorite("ash", 1);

        mockMvc.perform(delete("/api/v1/favorites/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "ash")
    void getFavorites_returns200WithList() throws Exception {
        when(favoriteService.getFavorites("ash")).thenReturn(List.of(
                new FavoriteDto(1, "bulbasaur", "https://example.com/1.png",
                        OffsetDateTime.now())));

        mockMvc.perform(get("/api/v1/favorites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pokemonName").value("bulbasaur"))
                .andExpect(jsonPath("$[0].pokemonId").value(1));
    }

    @Test
    void getFavorites_unauthenticated_returns401() throws Exception {
        mockMvc.perform(get("/api/v1/favorites"))
                .andExpect(status().isUnauthorized());
    }
}