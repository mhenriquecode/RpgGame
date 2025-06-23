package br.ifsp.rpg.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.ConnectException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StaticResourcesTest extends BaseUITest {

    private final String baseUrl = "http://localhost:5173";

    @Test
    @Tag("Resource")
    @DisplayName("Deve lançar ConnectException quando o ico não existir")
    void shouldThrowConnectExceptionWhenServerOffline() {
        assertThatThrownBy(() -> {
            URL url = new URL(baseUrl + "/favicon.ico");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.getResponseCode();
        }).isInstanceOf(ConnectException.class);
    }
}
