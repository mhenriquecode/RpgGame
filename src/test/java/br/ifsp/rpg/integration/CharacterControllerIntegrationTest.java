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
class CharacterControllerIntegrationTest extends BaseApiIntegrationTest {

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
    void deveCriarPersonagemComSucesso() {
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
    void deveBuscarPersonagemExistentePorId() {
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
    void deveListarTodosPersonagens() {
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
    void deveAtualizarPersonagem() {
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
    void deveRemoverPersonagem() {
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
    void deveRetornarListaVaziaQuandoNaoHaPersonagens() {
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
    void deveRetornar404QuandoPersonagemNaoExiste() {
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
    void deveRetornar400QuandoCriarPersonagemComDadosInvalidos() {
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
    void deveRetornar400QuandoAtualizarPersonagemComDadosInvalidos() {
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
    void deveRetornar404AoRemoverPersonagemInexistente() {
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
    void deveRetornar401QuandoNaoAutenticado() {
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
