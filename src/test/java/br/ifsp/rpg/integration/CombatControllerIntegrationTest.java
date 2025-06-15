package br.ifsp.rpg.integration;

import br.ifsp.web.dto.CharacterDTO;
import br.ifsp.web.dto.CombatRequestDTO;
import br.ifsp.web.model.enums.ClassType;
import br.ifsp.web.model.enums.Race;
import br.ifsp.web.model.enums.Weapon;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = br.ifsp.web.DemoAuthAppApplication.class)
@AutoConfigureMockMvc
@Tag("ApiTest")
@Tag("IntegrationTest")
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
    @WithMockUser
    void deveRetornarHistoricoDeCombates() throws Exception {
        mockMvc.perform(get("/api/combat/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
