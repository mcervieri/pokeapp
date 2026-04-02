package com.pokeapp.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokeapp.application.dto.team.*;
import com.pokeapp.application.exception.ResourceNotFoundException;
import com.pokeapp.application.service.TeamService;
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

@WebMvcTest(TeamController.class)
class TeamControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    TeamService teamService;

    @Test
    @WithMockUser(username = "ash")
    void getMyTeams_returns200() throws Exception {
        when(teamService.getMyTeams("ash")).thenReturn(List.of(
                new TeamSummaryDto(1, "My Team", "OU", 3, OffsetDateTime.now(), OffsetDateTime.now())));

        mockMvc.perform(get("/api/v1/teams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("My Team"))
                .andExpect(jsonPath("$[0].format").value("OU"))
                .andExpect(jsonPath("$[0].slotCount").value(3));
    }

    @Test
    @WithMockUser(username = "ash")
    void createTeam_returns201() throws Exception {
        TeamDetailDto detail = new TeamDetailDto(
                1, "Dream Team", "VGC", OffsetDateTime.now(), OffsetDateTime.now(), List.of());
        when(teamService.createTeam(any(), eq("ash"))).thenReturn(detail);

        mockMvc.perform(post("/api/v1/teams")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new CreateTeamRequest("Dream Team", "VGC"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Dream Team"));
    }

    @Test
    @WithMockUser(username = "ash")
    void getTeamById_notOwned_returns404() throws Exception {
        when(teamService.getTeamById(eq(99), eq("ash")))
                .thenThrow(new ResourceNotFoundException("Team not found: 99"));

        mockMvc.perform(get("/api/v1/teams/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "ash")
    void deleteTeam_returns204() throws Exception {
        doNothing().when(teamService).deleteTeam(eq(1), eq("ash"));

        mockMvc.perform(delete("/api/v1/teams/1").with(csrf()))
                .andExpect(status().isNoContent());
    }
}