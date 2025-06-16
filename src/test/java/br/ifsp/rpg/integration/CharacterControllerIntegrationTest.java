package br.ifsp.rpg.integration;

import br.ifsp.web.dto.CharacterDTO;
import br.ifsp.web.model.RpgCharacter;
import br.ifsp.web.model.enums.ClassType;
import br.ifsp.web.model.enums.Race;
import br.ifsp.web.model.enums.Weapon;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.port;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = br.ifsp.web.DemoAuthAppApplication.class)
@AutoConfigureMockMvc
class CharacterControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private CharacterDTO novoPersonagem() {
        return new CharacterDTO(
                null, "Arthas", ClassType.WARRIOR, Race.HUMAN, Weapon.SWORD,
                100, 20, 15, 10, 5
        );
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
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
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @WithMockUser
    void deveBuscarPersonagemExistentePorId() throws Exception {
        CharacterDTO dto = novoPersonagem();
        String response = mockMvc.perform(post("/api/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andReturn().getResponse().getContentAsString();
        CharacterDTO created = objectMapper.readValue(response, CharacterDTO.class);

        mockMvc.perform(get("/api/characters/{id}", created.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(created.id().toString()))
                .andExpect(jsonPath("$.name").value("Arthas"));
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @WithMockUser
    void deveListarTodosPersonagens() throws Exception {
        mockMvc.perform(get("/api/characters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @WithMockUser
    void deveAtualizarPersonagem() throws Exception {
        CharacterDTO dto = novoPersonagem();
        String response = mockMvc.perform(post("/api/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andReturn().getResponse().getContentAsString();
        CharacterDTO created = objectMapper.readValue(response, CharacterDTO.class);

        CharacterDTO atualizado = new CharacterDTO(
                created.id(), "Novo Nome", created.classType(), created.race(),
                created.weapon(), created.maxHealth(), created.strength(),
                created.defense(), created.speed(), created.armor()
        );

        mockMvc.perform(put("/api/characters/{id}", created.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Novo Nome"));
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
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
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @WithMockUser
    void deveRetornarListaVaziaQuandoNaoHaPersonagens() throws Exception {
        mockMvc.perform(get("/api/characters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @WithMockUser
    void deveRetornar404QuandoPersonagemNaoExiste() throws Exception {
        mockMvc.perform(get("/api/characters/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @WithMockUser
    void deveRetornar400QuandoCriarPersonagemComDadosInvalidos() throws Exception {
        CharacterDTO dto = new CharacterDTO(
                null, "", ClassType.WARRIOR, Race.HUMAN, Weapon.SWORD,
                100, 20, 15, 10, 5
        );
        mockMvc.perform(post("/api/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @WithMockUser
    void deveRetornar400QuandoAtualizarPersonagemComDadosInvalidos() throws Exception {
        CharacterDTO dto = novoPersonagem();
        String response = mockMvc.perform(post("/api/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andReturn().getResponse().getContentAsString();
        CharacterDTO created = objectMapper.readValue(response, CharacterDTO.class);

        CharacterDTO atualizado = new CharacterDTO(
                created.id(), "", created.classType(), created.race(),
                created.weapon(), created.maxHealth(), created.strength(),
                created.defense(), created.speed(), created.armor()
        );

        mockMvc.perform(put("/api/characters/{id}", created.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @WithMockUser
    void deveRetornar404AoAtualizarPersonagemInexistente() throws Exception {
        CharacterDTO atualizado = novoPersonagem();
        mockMvc.perform(put("/api/characters/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @WithMockUser
    void deveRetornar404AoRemoverPersonagemInexistente() throws Exception {
        mockMvc.perform(delete("/api/characters/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    void deveRetornar401QuandoNaoAutenticado() throws Exception {
        mockMvc.perform(get("/api/characters"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @WithMockUser
    void deveRetornar415QuandoContentTypeInvalido() throws Exception {
        String xml = "<character><name>Arthas</name></character>";
        mockMvc.perform(post("/api/characters")
                        .contentType(MediaType.APPLICATION_XML)
                        .content(xml))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @DisplayName("Deve tratar nomes com aspas simples sem causar erro de SQL")
    @WithMockUser
    void deveTratarNomesComAspasSimplesSemCausarErroDeSQL() throws Exception {
        RpgCharacter characterModel = new RpgCharacter(
                "D'gok, the Unbroken",
                ClassType.BERSERK,
                Race.ORC,
                Weapon.AXE
        );
        CharacterDTO characterWithSingleQuote = CharacterDTO.from(characterModel);

        mockMvc.perform(post("/api/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(characterWithSingleQuote)))
                .andExpect(status().isCreated());
    }
}
