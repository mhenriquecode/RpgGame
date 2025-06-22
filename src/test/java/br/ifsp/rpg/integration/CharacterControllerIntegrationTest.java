package br.ifsp.rpg.integration;

import br.ifsp.web.dto.CharacterDTO;
import br.ifsp.web.model.RpgCharacter;
import br.ifsp.web.model.enums.ClassType;
import br.ifsp.web.model.enums.Race;
import br.ifsp.web.model.enums.Weapon;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.*;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.empty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.port;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        classes = br.ifsp.web.DemoAuthAppApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class CharacterControllerIntegrationTest extends BaseApiIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @LocalServerPort
    private int port;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void limparBancoAntesDeCadaTeste() {
        jdbcTemplate.execute("PRAGMA foreign_keys = OFF");
        jdbcTemplate.execute("DELETE FROM characters");
        jdbcTemplate.execute("DELETE FROM app_user");
        jdbcTemplate.execute("PRAGMA foreign_keys = ON");
    }

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
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve criar personagem com sucesso")
    void shouldCreateCharacterSuccessfully() {
        String token = getAuthToken();
        CharacterDTO dto = novoPersonagem();

        given()
                .port(port)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/api/characters")
                .then()
                .statusCode(201)
                .body("id", notNullValue());
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve buscar personagem existente por ID")
    void shouldRetrieveExistingCharacterById() {
        String token = getAuthToken();
        CharacterDTO criado;

        try {
            criado = createCharacterViaApi(token, novoPersonagem());
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            Assertions.fail("Falha ao criar personagem: " + e.getMessage());
            return;
        }

        given()
                .port(port)
                .header("Authorization", "Bearer " + token)
                .pathParam("id", criado.id())
                .when()
                .get("/api/characters/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(criado.id().toString()))
                .body("name", equalTo("Arthas"));
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve listar todos personagens")
    void shouldListAllCharacters() {
        String token = getAuthToken();
        try {
            createCharacterViaApi(token, novoPersonagem());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        given()
                .port(port)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/characters")
                .then()
                .statusCode(200)
                .body("$", not(empty()));
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve atualizar personagem")
    void shouldUpdateCharacter() {
        String token = getAuthToken();
        CharacterDTO criado = null;
        try {
            criado = createCharacterViaApi(token, novoPersonagem());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        CharacterDTO atualizacao = new CharacterDTO(
                criado.id(),
                "Novo Nome",
                criado.classType(),
                criado.race(),
                criado.weapon(),
                criado.maxHealth(),
                criado.strength(),
                criado.defense(),
                criado.speed(),
                criado.armor()
        );

        given()
                .port(port)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .pathParam("id", criado.id())
                .body(atualizacao)
                .when()
                .put("/api/characters/{id}")
                .then()
                .statusCode(200)
                .body("name", equalTo("Novo Nome"));
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve remover personagem")
    void shouldDeleteCharacter() {
        String token = getAuthToken();
        CharacterDTO criado = null;
        try {
            criado = createCharacterViaApi(token, novoPersonagem());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        given()
                .port(port)
                .header("Authorization", "Bearer " + token)
                .pathParam("id", criado.id())
                .when()
                .delete("/api/characters/{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve retornar lista vazia quando não há personagens")
    void shouldReturnEmptyListWhenNoCharacters() {
        String token = getAuthToken();

        given()
                .port(port)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/characters")
                .then()
                .statusCode(200)
                .body("$", empty());
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve retornar 404 quando personagem não existe")
    void shouldReturn404WhenCharacterDoesNotExist() {
        String token = getAuthToken();
        UUID idInexistente = UUID.randomUUID();

        given()
                .port(port)
                .header("Authorization", "Bearer " + token)
                .pathParam("id", idInexistente)
                .when()
                .get("/api/characters/{id}")
                .then()
                .statusCode(404);
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve retornar 400 quando criar personagem com dados inválidos")
    void shouldReturn400WhenCreatingCharacterWithInvalidData() {
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

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/api/characters")
                .then()
                .statusCode(400);
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve retornar 400 quando atualizar personagem com dados inválidos")
    void shouldReturn400WhenUpdatingCharacterWithInvalidData() {
        String token = getAuthToken();
        CharacterDTO criado = null;
        try {
            criado = createCharacterViaApi(token, novoPersonagem());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        CharacterDTO atualizacao = new CharacterDTO(
                criado.id(),
                "",
                criado.classType(),
                criado.race(),
                criado.weapon(),
                criado.maxHealth(),
                criado.strength(),
                criado.defense(),
                criado.speed(),
                criado.armor()
        );

        given()
                .port(port)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .pathParam("id", criado.id())
                .body(atualizacao)
                .when()
                .put("/api/characters/{id}")
                .then()
                .statusCode(400);
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve retornar 404 ao remover personagem inexistente")
    void shouldReturn404WhenDeletingNonexistentCharacter() {
        String token = getAuthToken();

        given()
                .port(port)
                .header("Authorization", "Bearer " + token)
                .pathParam("id", UUID.randomUUID())
                .when()
                .delete("/api/characters/{id}")
                .then()
                .statusCode(404);
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve retornar 401 quando não autenticado")
    void shouldReturn401WhenNotAuthenticated() {
        given()
                .port(port)
                .when()
                .get("/api/characters")
                .then()
                .statusCode(401);
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve retornar 415 quando Content-Type inválido")
    void shouldReturn415WhenContentTypeIsInvalid() {
        String token = getAuthToken();
        String xml = "<character><name>Arthas</name></character>";

        given()
                .port(port)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.XML)
                .body(xml)
                .when()
                .post("/api/characters")
                .then()
                .statusCode(415);
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

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve retornar 401 ao criar personagem sem autenticação")
    void shouldReturn401WhenCreatingCharacterWithoutAuth(){
        CharacterDTO dto = novoPersonagem();

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/api/characters")
                .then()
                .statusCode(401);
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve retornar 401 ao buscar personagem por ID sem autenticação")
    void shouldReturn401WhenGettingCharacterByIdWithoutAuth() {
        given()
                .port(port)
                .pathParam("id", UUID.randomUUID())
                .when()
                .get("/api/characters/{id}")
                .then()
                .statusCode(401);
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve retornar 401 ao atualizar personagem sem autenticação")
    void shouldReturn401WhenUpdatingCharacterWithoutAuth() {
        CharacterDTO updateDto = novoPersonagem();

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .pathParam("id", UUID.randomUUID())
                .body(updateDto)
                .when()
                .put("/api/characters/{id}")
                .then()
                .statusCode(401);
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve retornar 401 ao remover personagem sem autenticação")
    void shouldReturn401WhenDeletingCharacterWithoutAuth() {
        given()
                .port(port)
                .pathParam("id", UUID.randomUUID())
                .when()
                .delete("/api/characters/{id}")
                .then()
                .statusCode(401);
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve criar personagem e retornar payload completo")
    void shouldCreateCharacterAndReturnCompletePayload() throws JsonProcessingException {
        String token = getAuthToken();
        CharacterDTO dto = novoPersonagem();

        given()
                .port(port)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/api/characters")
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("id",         notNullValue())
                .body("name",       equalTo(dto.name()))
                .body("classType",  equalTo(dto.classType().toString()))
                .body("race",       equalTo(dto.race().toString()))
                .body("weapon",     equalTo(dto.weapon().toString()));
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve buscar personagem e retornar payload completo")
    void shouldRetrieveCharacterAndReturnCompletePayload() throws JsonProcessingException {
        String token = getAuthToken();
        CharacterDTO created = createCharacterViaApi(token, novoPersonagem());

        given()
                .port(port)
                .header("Authorization", "Bearer " + token)
                .pathParam("id", created.id())
                .when()
                .get("/api/characters/{id}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id",         equalTo(created.id().toString()))
                .body("name",       equalTo(created.name()))
                .body("classType",  equalTo(created.classType().toString()))
                .body("race",       equalTo(created.race().toString()))
                .body("weapon",     equalTo(created.weapon().toString()));
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve listar múltiplos personagens e retornar payload completo")
    void shouldListAllCharactersAndReturnCompletePayload() throws JsonProcessingException {
        String token = getAuthToken();
        CharacterDTO first  = createCharacterViaApi(token, novoPersonagem());
        CharacterDTO second = createCharacterViaApi(token,
                new CharacterDTO(
                        null,
                        "Hatsune Miku",
                        ClassType.PALADIN,
                        Race.ELF,
                        Weapon.DAGGER,
                        69, 17, 6, 9, 690
                )
        );

        given()
                .port(port)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/characters")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()",    equalTo(2))
                .body("[0].id",    equalTo(first.id().toString()))
                .body("[0].name",       equalTo(first.name()))
                .body("[0].classType",  equalTo(first.classType().toString()))
                .body("[0].race",       equalTo(first.race().toString()))
                .body("[0].weapon",     equalTo(first.weapon().toString()))
                .body("[1].id",    equalTo(second.id().toString()))
                .body("[1].name",       equalTo("Hatsune Miku"))
                .body("[1].classType",  equalTo(second.classType().toString()))
                .body("[1].race",       equalTo(second.race().toString()))
                .body("[1].weapon",     equalTo(second.weapon().toString()));
    }
}
