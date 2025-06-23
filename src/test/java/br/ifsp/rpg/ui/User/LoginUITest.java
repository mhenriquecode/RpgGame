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
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.WebDriverWait;


import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Locale;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

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
        faker = new Faker(new Locale("en-US"));
        driver.get(baseUrl);
        loginPage = new LoginPage(driver, wait);
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Deve fazer login com sucesso")
    void shouldLoginSuccessfully() {

        String email = faker.name().username() + "@test.com";
        String password = faker.internet().password(8, 16);
        registerTestUser(email, password);

        HomePage homePage = loginPage.login(email, password);
        assertThat(homePage.isPageLoaded()).isTrue();
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Deve falhar ao fazer login com senha incorreta")
    void shouldFailLoginWithIncorrectPassword() {
        String email = faker.name().username() + "@test.com";
        String password = faker.internet().password(8, 16);
        registerTestUser(email, password);

        loginPage.attemptLogin(email, "swrong-password");

        String errorMessage = loginPage.getErrorMessage();
        assertThat(errorMessage).contains("Request failed with status code 401");
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Deve falhar ao fazer login com usuário não existente")
    void shouldFailLoginWithNonExistentUser() {

        String email = faker.name().username() + "@test.com";
        String password = faker.internet().password(8, 16);

        loginPage.attemptLogin(email, password);

        String errorMessage = loginPage.getErrorMessage();
        assertThat(errorMessage).contains("Request failed with status code 401");

    }

    @Test
    @Tag("UiTest")
    @DisplayName("Deve fazer logout com sucesso e retornar para a tela de login")
    void shouldLogoutSuccessfullyAndReturnToLoginPage() {
        String email = faker.name().username() + "@test.com";
        String password = faker.internet().password(8, 16);
        registerTestUser(email, password);

        HomePage homePage = loginPage.login(email, password);
        assertThat(homePage.isPageLoaded()).isTrue();

        LoginPage newLoginPage = homePage.logout();

        assertThat(newLoginPage.isPageLoaded()).isTrue();
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Deve ser bloqueado pelo navegador ao tentar fazer login com a senha em branco")
    void shouldBeBlockedByBrowserWithBlankPassword() {
        String email = faker.name().username() + "@test.com";
        String password = faker.internet().password(8, 16);
        registerTestUser(email, password);

        loginPage.attemptLogin(email, "");


        assertThat(loginPage.getEmailInputValue()).isEqualTo(email);
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Deve ser bloqueado pelo navegador ao tentar fazer login com o email em branco")
    void shouldBeBlockedByBrowserWithBlankEmail() {
        String password = faker.internet().password(8, 16);

        loginPage.attemptLogin("", password);

        assertThat(loginPage.isPageLoaded()).isTrue();
    }

}
