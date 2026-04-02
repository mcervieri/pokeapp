package com.pokeapp.web.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
@DirtiesContext
public class AuthIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void unauthenticatedRequest_returnsPublicData() throws Exception {
    mockMvc.perform(get("/api/v1/pokemon"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  void login_withValidCredentials_returnsToken() throws Exception {
    mockMvc.perform(post("/api/v1/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
              "username": "mcervieri",
              "password": "Marina1608@"
            }
            """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").exists())
        .andExpect(jsonPath("$.token").isNotEmpty());
  }

  @Test
  void login_withInvalidCredentials_returns401() throws Exception {
    mockMvc.perform(post("/api/v1/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
              "username": "mcervieri",
              "password": "wrongpassword"
            }
            """))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void authenticatedRequest_withValidToken_returns200() throws Exception {
    MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
              "username": "mcervieri",
              "password": "Marina1608@"
            }
            """))
        .andExpect(status().isOk())
        .andReturn();

    String body = loginResult.getResponse().getContentAsString();
    String token = body.replaceAll(".*\"token\":\\s*\"([^\"]+)\".*", "$1");

    mockMvc.perform(get("/api/v1/pokemon")
        .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }
}