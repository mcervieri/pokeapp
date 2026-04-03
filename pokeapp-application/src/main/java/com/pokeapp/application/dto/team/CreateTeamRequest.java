package com.pokeapp.application.dto.team;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTeamRequest(

                @NotBlank(message = "Team name must not be blank") @Size(max = 100, message = "Team name must not exceed 100 characters") String name,

                @Size(max = 50, message = "Format must not exceed 50 characters") String format) {
}