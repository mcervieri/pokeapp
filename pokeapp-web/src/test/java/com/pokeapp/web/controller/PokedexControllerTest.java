package com.pokeapp.web.controller;

import com.pokeapp.application.dto.user.PokedexEntryDto;
import com.pokeapp.application.service.PokedexService;
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

@WebMvcTest(PokedexController.class)
class PokedexControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    PokedexService pokedexService;

    private PokedexEntryDto entry(boolean seen, boolean caught) {
        return new PokedexEntryDto(1, "bulbasaur", "https://example.com/1.png",
                seen, caught, OffsetDateTime.now());
    }

    @Test
    @WithMockUser(username = "ash")
    void markSeen_returns200() throws Exception {
        when(pokedexService.markSeen(eq("ash"), eq(1))).thenReturn(entry(true, false));

        mockMvc.perform(post("/api/v1/pokedex/1/seen").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.seen").value(true))
                .andExpect(jsonPath("$.caught").value(false));
    }

    @Test
    @WithMockUser(username = "ash")
    void markCaught_returns200WithBothTrue() throws Exception {
        when(pokedexService.markCaught(eq("ash"), eq(1))).thenReturn(entry(true, true));

        mockMvc.perform(post("/api/v1/pokedex/1/caught").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.seen").value(true))
                .andExpect(jsonPath("$.caught").value(true));
    }

    @Test
    @WithMockUser(username = "ash")
    void unmarkCaught_returns200WithCaughtFalse() throws Exception {
        when(pokedexService.unmarkCaught(eq("ash"), eq(1))).thenReturn(entry(true, false));

        mockMvc.perform(post("/api/v1/pokedex/1/uncaught").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.caught").value(false));
    }

    @Test
    @WithMockUser(username = "ash")
    void getCaught_returns200WithList() throws Exception {
        when(pokedexService.getCaught(eq("ash"))).thenReturn(List.of(entry(true, true)));

        mockMvc.perform(get("/api/v1/pokedex/caught"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pokemonName").value("bulbasaur"))
                .andExpect(jsonPath("$[0].caught").value(true));
    }

    @Test
    @WithMockUser(username = "ash")
    void getSeen_returns200WithList() throws Exception {
        when(pokedexService.getSeen(eq("ash"))).thenReturn(List.of(entry(true, false)));

        mockMvc.perform(get("/api/v1/pokedex/seen"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].seen").value(true));
    }

    @Test
    void markSeen_unauthenticated_returns401or403() throws Exception {
        mockMvc.perform(post("/api/v1/pokedex/1/seen"))
                .andExpect(status().is4xxClientError());
    }
}