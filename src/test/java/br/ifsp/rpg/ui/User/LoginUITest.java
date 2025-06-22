package br.ifsp.rpg.ui.User;

import br.ifsp.rpg.ui.BaseUITest;
import br.ifsp.rpg.ui.pages.HomePage;
import br.ifsp.rpg.ui.pages.LoginPage;
import br.ifsp.rpg.ui.pages.RegisterPage;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.WebDriverWait;


import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Locale;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LoginUITest extends BaseUITest {

    private Faker faker;
    private LoginPage loginPage;

    private void registerTestUser(String email, String password) {
        RegisterPage registerPage = loginPage.navigateToRegisterPage();

        registerPage.registerUser(
                faker.name().firstName(),
                faker.name().lastName(),
                email,
                password, password
        );

        wait.until(ExpectedConditions.attributeToBe(registerPage.getNameInput(), "value", ""));
        registerPage.navigateToLoginPage();
    }

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        faker = new Faker(new Locale("pt-BR"));
        driver.get(baseUrl);
        loginPage = new LoginPage(driver, wait);
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Deve fazer login com sucesso e gerar token")
    void shouldLoginSuccessfully() {

        String email = faker.name().lastName() + "@test.com";
        String password = faker.internet().password(8, 16);
        registerTestUser(email, password);

        HomePage homePage = loginPage.login(email, password);

        assertThat(homePage.isPageLoaded()).isTrue();
        assertThat(driver.getCurrentUrl()).isEqualTo(baseUrl + "/");
    }

}
