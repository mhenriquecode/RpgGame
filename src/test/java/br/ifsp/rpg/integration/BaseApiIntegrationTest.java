package br.ifsp.rpg.integration;

import br.ifsp.web.security.auth.AuthRequest;
import br.ifsp.web.security.auth.AuthResponse;
import br.ifsp.web.security.auth.RegisterUserRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import com.fasterxml.jackson.databind.ObjectMapper;

import static io.restassured.RestAssured.given;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
        return given()
                .contentType(ContentType.JSON)
                .body(authRequest)
                .when()
                .post("/api/v1/authenticate")
                .then()
                .statusCode(200)
                .extract().body().as(AuthResponse.class).token();

    }



}
