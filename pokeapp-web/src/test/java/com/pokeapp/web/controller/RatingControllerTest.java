package com.pokeapp.web.controller;

import com.pokeapp.application.dto.user.RatingDto;
import com.pokeapp.application.service.RatingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RatingController.class)
class RatingControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    RatingService ratingService;

    @Test
    @WithMockUser(username = "ash")
    void rate_returns200WithDto() throws Exception {
        RatingDto dto = new RatingDto(1, "ash", 5, OffsetDateTime.now());
        when(ratingService.rateOrUpdate("ash", 1, 5)).thenReturn(dto);

        mockMvc.perform(put("/api/v1/pokemon/1/rating")
                .with(csrf())
                .param("score", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score").value(5))
                .andExpect(jsonPath("$.username").value("ash"));
    }

    @Test
    @WithMockUser(username = "ash")
    void getAverage_returns200() throws Exception {
        when(ratingService.getAverageRating(1)).thenReturn(4.2);

        mockMvc.perform(get("/api/v1/pokemon/1/rating/average"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(4.2));
    }

    @Test
    @WithMockUser(username = "ash")
    void getAverage_returnsZeroWhenNoRatings() throws Exception {
        when(ratingService.getAverageRating(1)).thenReturn(null);

        mockMvc.perform(get("/api/v1/pokemon/1/rating/average"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(0.0));
    }

    @Test
    @WithMockUser(username = "ash")
    void deleteRating_returns204() throws Exception {
        doNothing().when(ratingService).deleteRating("ash", 1);

        mockMvc.perform(delete("/api/v1/pokemon/1/rating").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    void rate_unauthenticated_returns401() throws Exception {
        mockMvc.perform(put("/api/v1/pokemon/1/rating").param("score", "5"))
                .andExpect(status().isForbidden());
    }
}