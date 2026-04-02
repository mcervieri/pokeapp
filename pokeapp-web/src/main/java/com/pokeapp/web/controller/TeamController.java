package com.pokeapp.web.controller;

import com.pokeapp.application.dto.team.*;
import com.pokeapp.application.service.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    public List<TeamSummaryDto> getMyTeams(Principal principal) {
        return teamService.getMyTeams(principal.getName());
    }

    @GetMapping("/{id}")
    public TeamDetailDto getTeamById(@PathVariable Integer id, Principal principal) {
        return teamService.getTeamById(id, principal.getName());
    }

    @PostMapping
    public ResponseEntity<TeamDetailDto> createTeam(
            @RequestBody CreateTeamRequest request,
            Principal principal) {
        TeamDetailDto created = teamService.createTeam(request, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public TeamDetailDto updateTeam(
            @PathVariable Integer id,
            @RequestBody UpdateTeamRequest request,
            Principal principal) {
        return teamService.updateTeam(id, request, principal.getName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Integer id, Principal principal) {
        teamService.deleteTeam(id, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/slots/{position}")
    public TeamSlotDto upsertSlot(
            @PathVariable Integer id,
            @PathVariable Integer position,
            @RequestBody UpsertSlotRequest request,
            Principal principal) {
        return teamService.upsertSlot(id, position, request, principal.getName());
    }

    @DeleteMapping("/{id}/slots/{position}")
    public ResponseEntity<Void> deleteSlot(
            @PathVariable Integer id,
            @PathVariable Integer position,
            Principal principal) {
        teamService.deleteSlot(id, position, principal.getName());
        return ResponseEntity.noContent().build();
    }
}