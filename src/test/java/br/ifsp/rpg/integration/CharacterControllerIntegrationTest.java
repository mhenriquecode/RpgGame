package br.ifsp.rpg.integration;

import br.ifsp.web.dto.CharacterDTO;
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

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = br.ifsp.web.DemoAuthAppApplication.class)
@AutoConfigureMockMvc
@Tag("ApiTest")
@Tag("IntegrationTest")
class CharacterControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private CharacterDTO novoPersonagem() {
        return new CharacterDTO(
                null,
                "Arthas",
                ClassType.WARRIOR,
                Race.HUMAN,
                Weapon.SWORD,
                100,
                20,
                15,
                10,
                5
        );
    }

    @Test
    @WithMockUser
    void deveCriarPersonagemComSucesso() throws Exception {
        CharacterDTO dto = novoPersonagem();

        mockMvc.perform(post("/api/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @WithMockUser
    void deveRetornar404QuandoPersonagemNaoExiste() throws Exception {
        mockMvc.perform(get("/api/characters/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void deveListarTodosPersonagens() throws Exception {
        mockMvc.perform(get("/api/characters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser
    void deveAtualizarPersonagem() throws Exception {
        CharacterDTO dto = novoPersonagem();
        String response = mockMvc.perform(post("/api/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andReturn().getResponse().getContentAsString();
        CharacterDTO created = objectMapper.readValue(response, CharacterDTO.class);

        CharacterDTO atualizado = new CharacterDTO(
                created.id(),
                "Novo Nome",
                created.classType(),
                created.race(),
                created.weapon(),
                created.maxHealth(),
                created.strength(),
                created.defense(),
                created.speed(),
                created.armor()
        );

        mockMvc.perform(put("/api/characters/{id}", created.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Novo Nome"));
    }

    @Test
    @WithMockUser
    void deveRemoverPersonagem() throws Exception {
        CharacterDTO dto = novoPersonagem();
        String response = mockMvc.perform(post("/api/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andReturn().getResponse().getContentAsString();
        CharacterDTO created = objectMapper.readValue(response, CharacterDTO.class);

        mockMvc.perform(delete("/api/characters/{id}", created.id()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void deveRetornar400QuandoCriarPersonagemComDadosInvalidos() throws Exception {
        CharacterDTO dto = new CharacterDTO(
                null,
                "",
                ClassType.WARRIOR,
                Race.HUMAN,
                Weapon.SWORD,
                100,
                20,
                15,
                10,
                5
        );

        mockMvc.perform(post("/api/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}
