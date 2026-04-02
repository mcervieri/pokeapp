package com.pokeapp.web.controller;

import com.pokeapp.application.dto.NatureDto;
import com.pokeapp.application.dto.PagedResponse;
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

@WebMvcTest(NatureController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class NatureControllerTest {

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

    private NatureDto adamant() {
        return new NatureDto(3, "adamant", "attack", "special-attack");
    }

    private PagedResponse<NatureDto> pagedAdamant() {
        return new PagedResponse<>(List.of(adamant()), 0, 20, 1, 1, true);
    }

    @Test
    void getAll_returnsOkWithPagedContent() throws Exception {
        when(natureService.findAll(isNull(), any())).thenReturn(pagedAdamant());

        mockMvc.perform(get("/api/v1/natures"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("adamant"));
    }

    @Test
    void getById_whenExists_returnsOk() throws Exception {
        when(natureService.findById(3)).thenReturn(Optional.of(adamant()));

        mockMvc.perform(get("/api/v1/natures/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("adamant"));
    }

    @Test
    void getById_whenNotExists_returns404() throws Exception {
        when(natureService.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/natures/99"))
                .andExpect(status().isNotFound());
    }
}