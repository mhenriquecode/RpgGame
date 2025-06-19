package br.ifsp.rpg.integration;

import br.ifsp.web.dto.CharacterDTO;
import br.ifsp.web.dto.CombatRequestDTO;
import br.ifsp.web.model.RpgCharacter;
import br.ifsp.web.model.enums.ClassType;
import br.ifsp.web.model.enums.Race;
import br.ifsp.web.model.enums.Weapon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


import static io.restassured.RestAssured.given;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.Matchers.lessThan;


class CombatControllerIntegrationTest extends BaseApiIntegrationTest {

    private String authToken;
    private CharacterDTO player1;
    private CharacterDTO player2;

    @BeforeEach
    void setUpData() throws Exception {
        authToken = getAuthToken();

        RpgCharacter characterPlayer1 = new RpgCharacter("Ana", ClassType.WARRIOR, Race.ELF, Weapon.SWORD);
        RpgCharacter characterPlayer2 = new RpgCharacter("Alan", ClassType.DUELIST, Race.ORC, Weapon.HAMMER);

        player1 = createCharacterViaApi(authToken, CharacterDTO.from(characterPlayer1));
        player2 = createCharacterViaApi(authToken, CharacterDTO.from(characterPlayer2));

    }

//    @Test
//    @Tag("ApiTest")
//    @Tag("IntegrationTest")
//    @WithMockUser
//    void deveIniciarCombateComSucesso() throws Exception {
//        CharacterDTO p1 = novoPersonagem("Player1");
//        CharacterDTO p2 = novoPersonagem("Player2");
//
//        String resp1 = mockMvc.perform(post("/api/characters")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(p1)))
//                .andReturn().getResponse().getContentAsString();
//        CharacterDTO created1 = objectMapper.readValue(resp1, CharacterDTO.class);
//
//        String resp2 = mockMvc.perform(post("/api/characters")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(p2)))
//                .andReturn().getResponse().getContentAsString();
//        CharacterDTO created2 = objectMapper.readValue(resp2, CharacterDTO.class);
//
//        CombatRequestDTO combatRequest = new CombatRequestDTO(
//                created1.id(), 1, created2.id(), 2
//        );
//
//        mockMvc.perform(post("/api/combat")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(combatRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.winnerId").exists())
//                .andExpect(jsonPath("$.winnerName").exists())
//                .andExpect(jsonPath("$.turnLogs").isArray());
//    }

//    @Test
//    @Tag("ApiTest")
//    @Tag("IntegrationTest")
//    @WithMockUser
//    void deveRetornarHistoricoDeCombates() throws Exception {
//        mockMvc.perform(get("/api/combat/history"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray());
//    }

//    @Test
//    @Tag("ApiTest")
//    @Tag("IntegrationTest")
//    @WithMockUser
//    void deveRetornar400QuandoIniciarCombateComPersonagemInvalido() throws Exception {
//        CombatRequestDTO combatRequest = new CombatRequestDTO(
//                UUID.randomUUID(), 1, UUID.randomUUID(), 2
//        );
//
//        mockMvc.perform(post("/api/combat")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(combatRequest)))
//                .andExpect(status().isBadRequest());
//    }

//    @Test
//    @Tag("ApiTest")
//    @Tag("IntegrationTest")
//    @WithMockUser
//    void deveRetornar400QuandoIniciarCombateComStrategyInvalida() throws Exception {
//        CharacterDTO p1 = novoPersonagem("Player1");
//        CharacterDTO p2 = novoPersonagem("Player2");
//
//        String resp1 = mockMvc.perform(post("/api/characters")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(p1)))
//                .andReturn().getResponse().getContentAsString();
//        CharacterDTO created1 = objectMapper.readValue(resp1, CharacterDTO.class);
//
//        String resp2 = mockMvc.perform(post("/api/characters")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(p2)))
//                .andReturn().getResponse().getContentAsString();
//        CharacterDTO created2 = objectMapper.readValue(resp2, CharacterDTO.class);
//
//        CombatRequestDTO combatRequest = new CombatRequestDTO(
//                created1.id(), 99, created2.id(), -1
//        );
//
//        mockMvc.perform(post("/api/combat")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(combatRequest)))
//                .andExpect(status().isBadRequest());
//    }

//    @Test
//    @Tag("ApiTest")
//    @Tag("IntegrationTest")
//    void deveRetornar401QuandoNaoAutenticado() throws Exception {
//        mockMvc.perform(get("/api/combat/history"))
//                .andExpect(status().isUnauthorized());
//    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve retornar 400 ao iniciar combate com o mesmo personagem")
    void shouldReturnBadRequestWhenStartingCombatWithSameCharacter() throws Exception {
        CombatRequestDTO combatRequest = new CombatRequestDTO(player1.id(), 1, player1.id(), 1);

        given()
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .body(combatRequest)
                .when()
                .post("/api/combat")
                .then()
                .statusCode(400);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1",
            "1, 2",
            "2, 1",
            "1, 3",
            "3, 1"
    })
    @DisplayName("Deve executar o combate com sucesso quando há pelo menos um atacante")
    void shouldExecuteSuccessfullyCombatWithAtLeastOneAttacking(int player1Strategy, int player2Strategy) {
        CombatRequestDTO combatRequest = new CombatRequestDTO(player1.id(), player1Strategy, player2.id(), player2Strategy);

        given()
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .body(combatRequest)
                .when()
                .post("/api/combat")
                .then()
                .statusCode(200)
                .time(lessThan(10L), SECONDS);
    }
}
