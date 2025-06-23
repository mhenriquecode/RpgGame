package br.ifsp.rpg.integration;

import br.ifsp.web.dto.CharacterDTO;
import br.ifsp.web.dto.CombatRequestDTO;
import br.ifsp.web.model.RpgCharacter;
import br.ifsp.web.model.enums.ClassType;
import br.ifsp.web.model.enums.Race;
import br.ifsp.web.model.enums.Weapon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zaxxer.hikari.SQLExceptionOverride;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;


class CombatControllerIntegrationTest extends BaseApiIntegrationTest {

    private String authToken;
    private CharacterDTO player1;
    private CharacterDTO player2;


    private CharacterDTO novoPersonagem(String nome) {
        return new CharacterDTO(
                null,
                nome,
                ClassType.WARRIOR,
                Race.HUMAN,
                Weapon.SWORD,
                100,
                10,
                5,
                8,
                5
        );
    }

    @BeforeEach
    void setUpData() throws Exception {
        authToken = getAuthToken();

        RpgCharacter characterPlayer1 = new RpgCharacter("Ana", ClassType.WARRIOR, Race.ELF, Weapon.SWORD);
        RpgCharacter characterPlayer2 = new RpgCharacter("Alan", ClassType.DUELIST, Race.ORC, Weapon.HAMMER);

        player1 = createCharacterViaApi(authToken, CharacterDTO.from(characterPlayer1));
        player2 = createCharacterViaApi(authToken, CharacterDTO.from(characterPlayer2));

    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve iniciar combate com sucesso")
    void shouldStartCombatSuccessfully() {
        CharacterDTO created1 = null;
        try {
            created1 = createCharacterViaApi(authToken, novoPersonagem("Player1"));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        CharacterDTO created2 = null;
        try {
            created2 = createCharacterViaApi(authToken, novoPersonagem("Player2"));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        CombatRequestDTO combatRequest = new CombatRequestDTO(
                created1.id(), 1, created2.id(), 2
        );

        given()
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .body(combatRequest)
                .when()
                .post("/api/combat")
                .then()
                .statusCode(200)
                .body("winnerId", notNullValue())
                .body("winnerName", notNullValue())
                .body("turnLogs", notNullValue());
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve retornar histórico de combates")
    void shouldReturnCombatHistory() {
        given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .get("/api/combat/history")
                .then()
                .statusCode(200)
                .body("$", isA(List.class));
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve retornar 400 quando iniciar combate com personagem inválido")
    void shouldReturn400WhenStartingCombatWithInvalidCharacter() {
        CombatRequestDTO combatRequest = new CombatRequestDTO(
                UUID.randomUUID(), 1, UUID.randomUUID(), 2
        );

        given()
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .body(combatRequest)
                .when()
                .post("/api/combat")
                .then()
                .statusCode(400);
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve retornar 400 quando iniciar combate com estratégia inválida")
    void shouldReturn400WhenStartingCombatWithInvalidStrategy() {
        CharacterDTO created1 = null;
        try {
            created1 = createCharacterViaApi(authToken, novoPersonagem("Player1"));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        CharacterDTO created2 = null;
        try {
            created2 = createCharacterViaApi(authToken, novoPersonagem("Player2"));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        CombatRequestDTO combatRequest = new CombatRequestDTO(
                created1.id(), 99, created2.id(), -1
        );

        given()
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .body(combatRequest)
                .when()
                .post("/api/combat")
                .then()
                .statusCode(400);
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve retornar 401 quando não autenticado")
    void shouldReturn401WhenNotAuthenticated() {
        given()
                .when()
                .get("/api/combat/history")
                .then()
                .statusCode(401);
    }

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
    @Tag("ApiTest")
    @Tag("IntegrationTest")
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

    @ParameterizedTest
    @CsvSource({
            "2, 2",
            "3, 2",
            "2, 3",
            "3, 3"
    })
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    @DisplayName("Deve retornar erro ao ter duas estratégias defensivas para players diferentes")
    void shouldReturnErrorWhenHavingTwoDefensiveStrategiesForDifferentPlayers(int player1Strategy, int player2Strategy) {

        CombatRequestDTO combatRequest = new CombatRequestDTO(player1.id(), player1Strategy, player2.id(), player2Strategy);

        given()
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .body(combatRequest)
                .when()
                .post("/api/combat")
                .then()
                .time(lessThan(10L), SECONDS);

    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve retornar 400 se o corpo da requisição estiver vazio")
    void shoulReturn401IfRequestHasEmptyBody() {

        given()
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .body("")
                .when()
                .post("/api/combat")
                .then()
                .statusCode(400);

    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve retornar 400 Bad Request se o ID do personagem for nulo")
    void shouldReturn400IfCharacterIdIsNull() {

        CombatRequestDTO combatRequest = new CombatRequestDTO(player1.id(), 1, null, 1);

        given()
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .body(combatRequest)
                .when()
                .post("/api/combat")
                .then()
                .statusCode(400);

    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve adicionar um registro ao histórico após um combate bem-sucedido")
    void shouldAddARecordToHistoryAfterASuccessfulCombat() {
        int initialSize = given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .get("api/combat/history")
                .then()
                .statusCode(200)
                .extract().body().path("size()");

        CombatRequestDTO combatRequest = new CombatRequestDTO(player1.id(), 1, player2.id(), 1);

        given()
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .body(combatRequest)
                .when()
                .post("/api/combat")
                .then()
                .statusCode(200);

        given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .get("api/combat/history")
                .then()
                .statusCode(200)
                .body("$", hasSize(initialSize + 1));
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve retornar 400 Bad Request para tipos de dados inválidos no corpo")
    void shouldReturn400ForInvalidDataTypesInTheBody() {
        String malformedJsonBody = String.format(
                """
                {
                    "player1Id": "%s",
                    "player1Strategy": "ESTRATEGIA_INVALIDA",
                    "player2Id": "%s",
                    "player2Strategy": 1
                }
                """,
                player1.id(), player2.id()
        );

        given()
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .body(malformedJsonBody)
                .when()
                .post("/api/combat")
                .then()
                .statusCode(400);
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve executar um combate com sucesso e declarar um dos participantes como vencedor")
    void shouldSuccessfullyExecuteACombatAndDeclareOneOfTheParticipantsAsTheWinner() {

        CombatRequestDTO combatRequest = new CombatRequestDTO(player1.id(), 1, player2.id(), 1);

        given()
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .body(combatRequest)
                .when()
                .post("/api/combat")
                .then()
                .statusCode(200)
                .body("winnerId", notNullValue())
                .body("winnerName", anyOf(equalTo(player1.name()), equalTo(player2.name())));

    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve retornar um histórico de combate vazio para um usuário novo")
    void shouldReturnAnEmptyCombatHistoryForANewUser() {

        String newAuthToken = getAuthToken();

        given()
                .header("Authorization", "Bearer " + newAuthToken)
                .when()
                .get("/api/combat/history")
                .then()
                .statusCode(200)
                .body("size()", is(0));

    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve retornar 400 se faltar um campo obrigatório no corpo de requisição")
    void shouldReturn400IfARequiredFieldIsMissingFromTheRequestBody() {

        String jsonWithMissingField = String.format(
                """
                {
                    "player1Id": "%s",
                    "player1Strategy": 1,
                    "player2Id": "%s"
                }
                """,
                player1.id(), player2.id()
        );

        given()
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .body(jsonWithMissingField)
                .when()
                .post("/api/combat")
                .then()
                .statusCode(400);

    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Não deve permitir que um usuário inicie um combate usando o personagem de outro usuário")
    void shouldNotAllowAUserToInitiateCombatUsingAnotherUsersCharacter() throws Exception {

        String secondAuthToken = getAuthToken();

        RpgCharacter playerUser2 = new RpgCharacter("Maligno", ClassType.BERSERK, Race.ORC, Weapon.AXE);
        CharacterDTO playerUser2DTO = createCharacterViaApi(secondAuthToken, CharacterDTO.from(playerUser2));

        CombatRequestDTO combatRequest = new CombatRequestDTO(player1.id(), 1, playerUser2DTO.id(), 1);

        given()
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .body(combatRequest)
                .when()
                .post("/api/combat")
                .then()
                .statusCode(anyOf(is(403), is(404)));

    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve listar apenas os personagens do usuário autenticado")
    void deveListarApenasPersonagensDoUsuarioAutenticado() throws Exception {

        RpgCharacter charA2Model = new RpgCharacter("Candidorr", ClassType.WARRIOR, Race.DWARF, Weapon.AXE);
        createCharacterViaApi(authToken, CharacterDTO.from(charA2Model));

        String secondAuthToken = getAuthToken();

        RpgCharacter charB1Model = new RpgCharacter("Fefeu", ClassType.PALADIN, Race.ORC, Weapon.HAMMER);
        createCharacterViaApi(secondAuthToken, CharacterDTO.from(charB1Model));

        given()
                .header("Authorization", "Bearer " + authToken)
                .get("/api/characters")
                .then()
                .statusCode(200)
                .body("size()", is(3))
                .body("name", hasItems("Ana", "Candidorr", "Alan"))
                .body("name", not(hasItem("Fefeu")));
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve atualizar um personagem com sucesso")
    void shouldUpdateACharacter() throws Exception {

        RpgCharacter originalCharModel = new RpgCharacter("UpdateMe", ClassType.WARRIOR, Race.HUMAN, Weapon.SWORD);
        CharacterDTO originalChar = createCharacterViaApi(authToken, CharacterDTO.from(originalCharModel));

        CharacterDTO updatedDto = createUpdatedCharacterDTO(originalChar, "Updated");

        given()
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .body(updatedDto)
                .when()
                .put("/api/characters/{id}", originalChar.id())
                .then()
                .statusCode(200)
                .body("name", equalTo("Updated"));
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Não deve permitir que um usuário atualize o personagem de outro usuário")
    void shouldNotAllowAUserToUpdateAnotherUsersCharacter() {

        String newAuthToken = getAuthToken();

        CharacterDTO updatedDto = createUpdatedCharacterDTO(player1, "Hacked O_o");

        given()
                .header("Authorization", "Bearer " + newAuthToken)
                .contentType("application/json")
                .body(updatedDto)
                .when()
                .put("/api/characters/{id}", player1.id())
                .then()
                .statusCode(anyOf(is(403), is(404)));

    }

    private CharacterDTO createUpdatedCharacterDTO(CharacterDTO original, String newName) {
        return new CharacterDTO(
                original.id(),
                newName,
                original.classType(),
                original.race(),
                original.weapon(),
                original.maxHealth(),
                original.strength(),
                original.defense(),
                original.speed(),
                original.armor()
        );
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve deletar um personagem com sucesso")
    void shouldDeleteACharacter() throws Exception {

        given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .delete("/api/characters/{id}", player1.id())
                .then()
                .statusCode(204);

        given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .delete("/api/characters/{id}", player1.id())
                .then()
                .statusCode(401);

    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Não deve permitir que um usuário delete o personagem de outro usuário")
    void shouldNotPermiteToDeleteAnotherUserCharacter() {

        String newAuthToken = getAuthToken();

        given()
                .header("Authorization", "Bearer " + newAuthToken)
                .when()
                .delete("/api/characters/{id}", player1.id())
                .then()
                .statusCode(anyOf(is(403), is(404)));

    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve retornar 400 ao tentar iniciar combate com um personagem deletado")
    void shouldReturn400WhenStartingCombatWithDeletedCharacter() throws Exception {
        RpgCharacter charToDeleteModel = new RpgCharacter("ToDelete", ClassType.WARRIOR, Race.HUMAN, Weapon.SWORD);
        CharacterDTO charToDelete = createCharacterViaApi(authToken, CharacterDTO.from(charToDeleteModel));

        given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .delete("/api/characters/{id}", charToDelete.id())
                .then()
                .statusCode(204);

        CombatRequestDTO combatRequest = new CombatRequestDTO(player1.id(), 1, charToDelete.id(), 1);

        given()
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .body(combatRequest)
                .when()
                .post("/api/combat")
                .then()
                .statusCode(400);
    }

}
