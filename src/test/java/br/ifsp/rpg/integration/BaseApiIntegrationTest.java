package br.ifsp.rpg.integration;

import br.ifsp.web.dto.CharacterDTO;
import br.ifsp.web.security.auth.AuthRequest;
import br.ifsp.web.security.auth.AuthResponse;
import br.ifsp.web.security.auth.RegisterUserRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.core.JsonProcessingException;

import static io.restassured.RestAssured.given;


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = br.ifsp.web.DemoAuthAppApplication.class
)
public abstract class BaseApiIntegrationTest {

    @LocalServerPort
    private int port;

    protected final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    protected String getAuthToken() {

        RegisterUserRequest registerRequest = new RegisterUserRequest("Test", "User", "auth@test.com", "password");

        given()
                .contentType(ContentType.JSON)
                .body(registerRequest)
                .when()
                .post("/api/v1/register")
                .then()
                .statusCode(201);

        AuthRequest authRequest = new AuthRequest(registerRequest.email(), registerRequest.password());
        String token =  given()
                .contentType(ContentType.JSON)
                .body(authRequest)
                .when()
                .post("/api/v1/authenticate")
                .then()
                .statusCode(200)
                .extract().body().as(AuthResponse.class).token();
        return token;

    }

    protected CharacterDTO createCharacterViaApi(String token, CharacterDTO character) throws JsonProcessingException {
        String responseBody = given()
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(character)
                .post("/api/characters")
                .then()
                .statusCode(201)
                .extract().body().asString();

        return objectMapper.readValue(responseBody, CharacterDTO.class);
    }

}
