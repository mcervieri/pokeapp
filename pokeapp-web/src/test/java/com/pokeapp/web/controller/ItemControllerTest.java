package com.pokeapp.web.controller;

import com.pokeapp.application.dto.ItemDto;
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

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class ItemControllerTest {

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

    private ItemDto potion() {
        return new ItemDto(17, "potion", "Restores 20 HP.", "https://example.com/potion.png");
    }

    @Test
    void getAll_returnsOkWithList() throws Exception {
        when(itemService.findAll()).thenReturn(List.of(potion()));

        mockMvc.perform(get("/api/v1/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("potion"));
    }

    @Test
    void getById_whenExists_returnsOk() throws Exception {
        when(itemService.findById(17)).thenReturn(Optional.of(potion()));

        mockMvc.perform(get("/api/v1/items/17"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("potion"));
    }

    @Test
    void getById_whenNotExists_returns404() throws Exception {
        when(itemService.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/items/99"))
                .andExpect(status().isNotFound());
    }
}