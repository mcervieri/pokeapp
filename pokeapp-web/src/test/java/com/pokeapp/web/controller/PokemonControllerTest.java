package com.pokeapp.web.controller;

import com.pokeapp.application.dto.PagedResponse;
import com.pokeapp.application.dto.PokemonDto;
import com.pokeapp.application.dto.StatValueDto;
import com.pokeapp.application.security.JwtAuthFilter;
import com.pokeapp.application.security.JwtService;
import com.pokeapp.application.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PokemonController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class PokemonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PokemonService pokemonService;
    @MockBean
    private TypeService typeService;
    @MockBean
    private MoveService moveService;
    @MockBean
    private AbilityService abilityService;
    @MockBean
    private ItemService itemService;
    @MockBean
    private NatureService natureService;
    @MockBean
    private AuthService authService;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private JwtAuthFilter jwtAuthFilter;
    @MockBean
    private UserDetailsService userDetailsService;

    private PokemonDto bulbasaur() {
        return new PokemonDto(1, "bulbasaur", 7, 69, 64,
                List.of("grass", "poison"),
                List.of(new StatValueDto("hp", 45)));
    }

    private PagedResponse<PokemonDto> pagedBulbasaur() {
        return new PagedResponse<>(List.of(bulbasaur()), 0, 20, 1, 1, true);
    }

    @Test
    void getAll_returnsOkWithPagedContent() throws Exception {
        when(pokemonService.findAll(isNull(), isNull(), any())).thenReturn(pagedBulbasaur());

        mockMvc.perform(get("/api/v1/pokemon"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("bulbasaur"))
                .andExpect(jsonPath("$.content[0].types[0]").value("grass"));
    }

    @Test
    void getAll_withTypeFilter_returnsOk() throws Exception {
        when(pokemonService.findAll(any(), isNull(), any())).thenReturn(pagedBulbasaur());

        mockMvc.perform(get("/api/v1/pokemon?type=grass"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("bulbasaur"));
    }

    @Test
    void getAll_withSearchFilter_returnsOk() throws Exception {
        when(pokemonService.findAll(isNull(), any(), any())).thenReturn(pagedBulbasaur());

        mockMvc.perform(get("/api/v1/pokemon?search=bulb"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("bulbasaur"));
    }

    @Test
    void getById_whenExists_returnsOk() throws Exception {
        when(pokemonService.findById(1)).thenReturn(Optional.of(bulbasaur()));

        mockMvc.perform(get("/api/v1/pokemon/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("bulbasaur"));
    }

    @Test
    void getById_whenNotExists_returns404() throws Exception {
        when(pokemonService.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/pokemon/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getByName_whenExists_returnsOk() throws Exception {
        when(pokemonService.findByName("bulbasaur")).thenReturn(Optional.of(bulbasaur()));

        mockMvc.perform(get("/api/v1/pokemon/name/bulbasaur"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("bulbasaur"));
    }
}