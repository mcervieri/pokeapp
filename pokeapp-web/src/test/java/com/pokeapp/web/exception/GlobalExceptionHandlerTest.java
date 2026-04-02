package com.pokeapp.web.exception;

import com.pokeapp.application.security.JwtAuthFilter;
import com.pokeapp.application.security.JwtService;
import com.pokeapp.application.service.*;
import com.pokeapp.web.controller.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {
        PokemonController.class,
        TypeController.class,
        AbilityController.class,
        ItemController.class,
        MoveController.class,
        NatureController.class
})
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Import(GlobalExceptionHandler.class)
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PokemonService pokemonService;
    @MockBean
    private TypeService typeService;
    @MockBean
    private AbilityService abilityService;
    @MockBean
    private ItemService itemService;
    @MockBean
    private MoveService moveService;
    @MockBean
    private NatureService natureService;
    @MockBean
    private AuthService authService;
    @MockBean
    private JwtAuthFilter jwtAuthFilter;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    void whenPokemonNotFound_returns404WithErrorBody() throws Exception {
        when(pokemonService.findById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/pokemon/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Pokemon not found with id: 9999"))
                .andExpect(jsonPath("$.path").value("/api/v1/pokemon/9999"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void whenPokemonNameNotFound_returns404WithErrorBody() throws Exception {
        when(pokemonService.findByName(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/pokemon/name/missingno"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Pokemon not found with name: missingno"))
                .andExpect(jsonPath("$.path").value("/api/v1/pokemon/name/missingno"));
    }

    @Test
    void whenInvalidIdType_returns400WithErrorBody() throws Exception {
        mockMvc.perform(get("/api/v1/pokemon/notanumber"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.path").value("/api/v1/pokemon/notanumber"));
    }

    @Test
    void whenAbilityNotFound_returns404WithErrorBody() throws Exception {
        when(abilityService.findById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/abilities/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Ability not found with id: 9999"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void whenItemNotFound_returns404WithErrorBody() throws Exception {
        when(itemService.findById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/items/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Item not found with id: 9999"));
    }

    @Test
    void whenMoveNotFound_returns404WithErrorBody() throws Exception {
        when(moveService.findById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/moves/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Move not found with id: 9999"));
    }

    @Test
    void whenNatureNotFound_returns404WithErrorBody() throws Exception {
        when(natureService.findById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/natures/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Nature not found with id: 9999"));
    }
}