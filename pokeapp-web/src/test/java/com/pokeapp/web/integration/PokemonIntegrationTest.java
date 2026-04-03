package com.pokeapp.web.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
@TestPropertySource(properties = {
                "spring.autoconfigure.exclude="
})
public class PokemonIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        private String token;

        @BeforeEach
        void obtainToken() throws Exception {
                MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                  "username": "mcervieri",
                                                  "password": "Marina1608@"
                                                }
                                                """))
                                .andReturn();

                String body = result.getResponse().getContentAsString();
                token = body.replaceAll(".*\"token\":\\s*\"([^\"]+)\".*", "$1");
        }

        @Test
        void getAll_returnsPagedList() throws Exception {
                mockMvc.perform(get("/api/v1/pokemon")
                                .header("Authorization", "Bearer " + token))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content").isArray())
                                .andExpect(jsonPath("$.totalElements").isNumber())
                                .andExpect(jsonPath("$.page").value(0));
        }

        @Test
        void getById_whenExists_returnsOk() throws Exception {
                mockMvc.perform(get("/api/v1/pokemon/1")
                                .header("Authorization", "Bearer " + token))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.name").exists())
                                .andExpect(jsonPath("$.types").isArray())
                                .andExpect(jsonPath("$.stats").isArray());
        }

        @Test
        void getById_whenNotExists_returns404WithErrorBody() throws Exception {
                mockMvc.perform(get("/api/v1/pokemon/99999")
                                .header("Authorization", "Bearer " + token))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.status").value(404))
                                .andExpect(jsonPath("$.error").value("Not Found"))
                                .andExpect(jsonPath("$.message").value("Pokemon not found with id: 99999"))
                                .andExpect(jsonPath("$.timestamp").exists());
        }

        @Test
        void getByName_whenExists_returnsOk() throws Exception {
                mockMvc.perform(get("/api/v1/pokemon/name/bulbasaur")
                                .header("Authorization", "Bearer " + token))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("bulbasaur"));
        }

        @Test
        void getByName_whenNotExists_returns404() throws Exception {
                mockMvc.perform(get("/api/v1/pokemon/name/missingno")
                                .header("Authorization", "Bearer " + token))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.status").value(404))
                                .andExpect(jsonPath("$.message").value("Pokemon not found with name: missingno"));
        }

        @Test
        void getAll_withTypeFilter_returnsFilteredResults() throws Exception {
                mockMvc.perform(get("/api/v1/pokemon?type=fire")
                                .header("Authorization", "Bearer " + token))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content").isArray());
        }

        @Test
        void getAll_withInvalidId_returns400() throws Exception {
                mockMvc.perform(get("/api/v1/pokemon/notanumber")
                                .header("Authorization", "Bearer " + token))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.status").value(400));
        }
}