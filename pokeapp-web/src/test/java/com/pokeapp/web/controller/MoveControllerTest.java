package com.pokeapp.web.controller;

import com.pokeapp.application.dto.MoveDto;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MoveController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class MoveControllerTest {

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

    private MoveDto flamethrower() {
        return new MoveDto(53, "flamethrower", "fire", "special", 90, 100, 15);
    }

    @Test
    void getAll_returnsOkWithList() throws Exception {
        when(moveService.findAll()).thenReturn(List.of(flamethrower()));

        mockMvc.perform(get("/api/v1/moves"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("flamethrower"));
    }

    @Test
    void getById_whenExists_returnsOk() throws Exception {
        when(moveService.findById(53)).thenReturn(Optional.of(flamethrower()));

        mockMvc.perform(get("/api/v1/moves/53"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("flamethrower"));
    }

    @Test
    void getById_whenNotExists_returns404() throws Exception {
        when(moveService.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/moves/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getByName_whenExists_returnsOk() throws Exception {
        when(moveService.findByName("flamethrower")).thenReturn(Optional.of(flamethrower()));

        mockMvc.perform(get("/api/v1/moves/name/flamethrower"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("flamethrower"));
    }
}