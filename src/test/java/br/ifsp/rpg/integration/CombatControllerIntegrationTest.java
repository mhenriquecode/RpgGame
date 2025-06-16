package br.ifsp.rpg.integration;

import br.ifsp.web.dto.CharacterDTO;
import br.ifsp.web.dto.CombatRequestDTO;
import br.ifsp.web.model.enums.ClassType;
import br.ifsp.web.model.enums.Race;
import br.ifsp.web.model.enums.Weapon;
import br.ifsp.web.service.CharacterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = br.ifsp.web.DemoAuthAppApplication.class)
@AutoConfigureMockMvc
class CombatControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private CharacterDTO novoPersonagem(String nome) {
        return new CharacterDTO(
                null, nome, ClassType.WARRIOR, Race.HUMAN, Weapon.SWORD,
                100, 20, 15, 10, 5
        );
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @WithMockUser
    void deveIniciarCombateComSucesso() throws Exception {
        CharacterDTO p1 = novoPersonagem("Player1");
        CharacterDTO p2 = novoPersonagem("Player2");

        String resp1 = mockMvc.perform(post("/api/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p1)))
                .andReturn().getResponse().getContentAsString();
        CharacterDTO created1 = objectMapper.readValue(resp1, CharacterDTO.class);

        String resp2 = mockMvc.perform(post("/api/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p2)))
                .andReturn().getResponse().getContentAsString();
        CharacterDTO created2 = objectMapper.readValue(resp2, CharacterDTO.class);

        CombatRequestDTO combatRequest = new CombatRequestDTO(
                created1.id(), 1, created2.id(), 2
        );

        mockMvc.perform(post("/api/combat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(combatRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.winnerId").exists())
                .andExpect(jsonPath("$.winnerName").exists())
                .andExpect(jsonPath("$.turnLogs").isArray());
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @WithMockUser
    void deveRetornarHistoricoDeCombates() throws Exception {
        mockMvc.perform(get("/api/combat/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @WithMockUser
    void deveRetornar400QuandoIniciarCombateComPersonagemInvalido() throws Exception {
        CombatRequestDTO combatRequest = new CombatRequestDTO(
                UUID.randomUUID(), 1, UUID.randomUUID(), 2
        );

        mockMvc.perform(post("/api/combat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(combatRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @WithMockUser
    void deveRetornar400QuandoIniciarCombateComStrategyInvalida() throws Exception {
        CharacterDTO p1 = novoPersonagem("Player1");
        CharacterDTO p2 = novoPersonagem("Player2");

        String resp1 = mockMvc.perform(post("/api/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p1)))
                .andReturn().getResponse().getContentAsString();
        CharacterDTO created1 = objectMapper.readValue(resp1, CharacterDTO.class);

        String resp2 = mockMvc.perform(post("/api/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p2)))
                .andReturn().getResponse().getContentAsString();
        CharacterDTO created2 = objectMapper.readValue(resp2, CharacterDTO.class);

        CombatRequestDTO combatRequest = new CombatRequestDTO(
                created1.id(), 99, created2.id(), -1
        );

        mockMvc.perform(post("/api/combat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(combatRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    void deveRetornar401QuandoNaoAutenticado() throws Exception {
        mockMvc.perform(get("/api/combat/history"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve retornar 400 ao iniciar combate com o mesmo personagem")
    @WithMockUser
    void shouldReturnBadRequestWhenStartingCombatWithSameCharacter() throws Exception {
        CharacterDTO p1Dto = novoPersonagem("Lancelot");
        String response = mockMvc.perform(post("/api/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p1Dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        CharacterDTO createdCharacter = objectMapper.readValue(response, CharacterDTO.class);

        CombatRequestDTO request = new CombatRequestDTO(createdCharacter.id(), 1, createdCharacter.id(), 2);

        mockMvc.perform(post("/api/combat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
