package br.ifsp.rpg.integration;

import br.ifsp.web.DemoAuthAppApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.matchesRegex;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        classes = DemoAuthAppApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TransactionControllerIntegrationTest extends BaseApiIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("hello endpoint should return hello message when authorized")
    @Test
    public void helloEndpointShouldReturnHelloMessageWhenAuthorized() throws Exception {
        String token = getAuthToken();

        mockMvc.perform(get("/api/v1/hello")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string(startsWith("Hello: ")))
                .andExpect(content().string(matchesRegex("Hello: [0-9a-f\\-]{36}")));
    }

    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("hello endpoint should return hello unauthorized when no token")
    @Test
    public void helloEndpointShouldReturnUnauthorizedWhenNoToken() throws Exception {
        mockMvc.perform(get("/api/v1/hello"))
                .andExpect(status().isUnauthorized());
    }
}
