package com.pokeapp.web.controller;

import com.pokeapp.application.dto.PagedResponse;
import com.pokeapp.application.dto.TypeDto;
import com.pokeapp.application.dto.TypeMatchupDto;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TypeController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class TypeControllerTest {

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

    private TypeDto fire() {
        return new TypeDto(10, "fire",
                List.of(new TypeMatchupDto("grass", new BigDecimal("2.0"))));
    }

    private PagedResponse<TypeDto> pagedFire() {
        return new PagedResponse<>(List.of(fire()), 0, 20, 1, 1, true);
    }

    @Test
    void getAll_returnsOkWithPagedContent() throws Exception {
        when(typeService.findAll(any())).thenReturn(pagedFire());

        mockMvc.perform(get("/api/v1/types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("fire"));
    }

    @Test
    void getById_whenExists_returnsOk() throws Exception {
        when(typeService.findById(10)).thenReturn(Optional.of(fire()));

        mockMvc.perform(get("/api/v1/types/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("fire"));
    }

    @Test
    void getById_whenNotExists_returns404() throws Exception {
        when(typeService.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/types/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getMatchups_returnsOk() throws Exception {
        when(typeService.getMatchups(10)).thenReturn(fire().damageRelations());

        mockMvc.perform(get("/api/v1/types/10/matchups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].targetType").value("grass"));
    }
}