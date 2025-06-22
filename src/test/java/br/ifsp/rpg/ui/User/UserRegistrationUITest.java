package br.ifsp.rpg.ui.User;

import br.ifsp.rpg.ui.BaseUITest;
import br.ifsp.rpg.ui.pages.CharacterListPage;
import br.ifsp.rpg.ui.pages.LoginPage;
import br.ifsp.rpg.ui.pages.RegisterPage;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

public class UserRegistrationUITest extends BaseUITest {

    private Faker faker;
    private LoginPage loginPage;

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
    @DisplayName("Deve registrar um novo usuário e conseguir fazer login")
    void shouldRegisterNewUserAndClearTheForm() {
        LoginPage loginPage = new LoginPage(driver, wait);
        RegisterPage registerPage = loginPage.navigateToRegisterPage();
        String password = faker.internet().password(8, 16, true, true);

        registerPage.registerUser(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().emailAddress(),
                password,
                password
        );

        wait.until(ExpectedConditions.attributeToBe(registerPage.getNameInput(), "value", ""));

        assertThat(registerPage.getNameInputValue()).isEmpty();
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Deve falhar ao registrar com senhas que não coincidem e permanecer na página")
    void shouldFailToRegisterWithMismatchedPasswordsAndStayOnPage() {
        RegisterPage registerPage = loginPage.navigateToRegisterPage();

        registerPage.registerUser(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().emailAddress(),
                "password123",
                "differentPassword"
        );

        String errorMessage = registerPage.getErrorMessage();
        assertThat(errorMessage).contains("As senhas não coincidem");
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Deve falhar ao registrar com campo em branco e permanecer na página")
    void shouldFailToRegisterWithBlankFieldAndStayOnPage() {
        LoginPage loginPage = new LoginPage(driver, wait);
        RegisterPage registerPage = loginPage.navigateToRegisterPage();
        String initialUrl = registerPage.getCurrentUrl();

        registerPage.registerUser(
                "",
                faker.name().lastName(),
                faker.internet().emailAddress(),
                "password123",
                "password123"
        );

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertThat(driver.getCurrentUrl()).isEqualTo(initialUrl);
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Deve falhar ao tentar registrar com nome e sobrenome contendo apenas espaços")
    void shouldFailToRegisterWithWhitespaceNameAndLastname() {
        LoginPage loginPage = new LoginPage(driver, wait);
        RegisterPage registerPage = loginPage.navigateToRegisterPage();
        String password = faker.internet().password(8, 16, true, true);

        registerPage.registerUser(
                "          ",
                "          ",
                faker.internet().emailAddress(),
                password,
                password
        );

        String errorMessage = registerPage.getErrorMessage();

        assertThat(errorMessage).isNotEmpty();
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Deve falhar ao tentar registrar com nome e sobrenome contendo apenas caracteres especiais")
    void shouldFailToRegisterWithSpecialCharactersInNameAndLastname() {
        LoginPage loginPage = new LoginPage(driver, wait);
        RegisterPage registerPage = loginPage.navigateToRegisterPage();
        String password = faker.internet().password(8, 16, true, true);

        registerPage.registerUser(
                "!@#$%",
                "%^&*()",
                faker.internet().emailAddress(),
                password,
                password
        );

        String errorMessage = registerPage.getErrorMessage();

        assertThat(errorMessage).isNotEmpty();
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Deve falhar ao tentar registrar email já existente")
    void shouldFailToRegisterWithEmailAlreadyExist() {

        RegisterPage registerPage = loginPage.navigateToRegisterPage();
        String existingEmail = faker.internet().emailAddress();
        String password = faker.internet().password(8, 16, true, true);

        registerPage.registerUser(
                faker.name().firstName(),
                faker.name().lastName(),
                existingEmail,
                password, password
        );

        wait.until(ExpectedConditions.attributeToBe(registerPage.getNameInput(), "value", ""));

        registerPage.registerUser(
                faker.name().firstName(),
                faker.name().lastName(),
                existingEmail,
                password, password
        );

        String errorMessage = registerPage.getErrorMessage();
        assertThat(errorMessage).contains("Email already registered: " + existingEmail);

    }

    @Test
    @Tag("UiTest")
    @DisplayName("Deve permitir a navegação da página de login para a de registro")
    void shouldNavigateFromLoginPageToRegisterPage() {

        RegisterPage registerPage = loginPage.navigateToRegisterPage();

        wait.until(ExpectedConditions.visibilityOf(registerPage.getNameInput()));
        assertThat(registerPage.getNameInput().isDisplayed()).isTrue();

    }

    @Test
    @Tag("UiTest")
    @DisplayName("Deve permitir a navegação da página de registro de volta para a de login")
    void shouldNavigateFromRegisterToLoginPage() {

        RegisterPage registerPage = loginPage.navigateToRegisterPage();
        wait.until(ExpectedConditions.visibilityOf(registerPage.getNameInput()));

        LoginPage newLoginPage = registerPage.navigateToLoginPage();
        wait.until(ExpectedConditions.visibilityOf(newLoginPage.getLoginButton()));
        assertThat(newLoginPage.getLoginButton().isDisplayed()).isTrue();

    }

    @Test
    @Tag("UiTest")
    @DisplayName("Deve ser bloqueado pelo navegador ao tentar registrar com e-mail sem '@'")
    void shouldBeBlockedByBrowserForEmailWithoutAtSymbol() {
        RegisterPage registerPage = loginPage.navigateToRegisterPage();
        String invalidEmail = "emailinvalido.com";

        registerPage.registerUser("Nome", "Valido", invalidEmail, "pass123", "pass123");

        assertThat(registerPage.getEmailInputValue()).isEqualTo(invalidEmail);
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Deve ser bloqueado pelo navegador ao tentar registrar com e-mail com acento")
    void shouldBeBlockedByBrowserForAccentedEmail() {
        RegisterPage registerPage = loginPage.navigateToRegisterPage();
        String invalidEmail = "usuário@inválido.com";

        registerPage.registerUser("Nome", "Valido", invalidEmail, "pass123", "pass123");

        assertThat(registerPage.getEmailInputValue()).isEqualTo(invalidEmail);
    }


}