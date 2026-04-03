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

  static final String TEST_USERNAME = "test_integration_user";
  static final String TEST_PASSWORD = "Integration@99";
  static final String TEST_EMAIL = "test_integration@pokeapp.test";

  @Test
  void unauthenticatedRequest_returnsPublicData() throws Exception {
    mockMvc.perform(get("/api/v1/pokemon"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  void register_withValidData_returnsToken() throws Exception {
    // Each run uses a unique suffix so re-runs on the same DB don't conflict
    String unique = String.valueOf(System.currentTimeMillis());

    mockMvc.perform(post("/api/v1/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
              "username": "test_user_%s",
              "email": "test_%s@pokeapp.test",
              "password": "Integration@99"
            }
            """.formatted(unique, unique)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.token").exists())
        .andExpect(jsonPath("$.token").isNotEmpty());
  }

  @Test
  void login_withInvalidCredentials_returns401() throws Exception {
    mockMvc.perform(post("/api/v1/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
              "username": "ghost_user_xyz",
              "password": "wrongpassword"
            }
            """))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void authenticatedRequest_withValidToken_returns200() throws Exception {
    // Register a fresh user, then immediately use the returned token
    String unique = String.valueOf(System.currentTimeMillis());

    MvcResult registerResult = mockMvc.perform(post("/api/v1/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
              "username": "test_user_%s",
              "email": "test_%s@pokeapp.test",
              "password": "Integration@99"
            }
            """.formatted(unique, unique)))
        .andExpect(status().isCreated())
        .andReturn();

    String body = registerResult.getResponse().getContentAsString();
    String token = body.replaceAll(".*\"token\":\\s*\"([^\"]+)\".*", "$1");

    mockMvc.perform(get("/api/v1/pokemon")
        .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }
}