package br.ifsp.rpg.ui.User;

import br.ifsp.rpg.ui.BaseUITest;
import br.ifsp.rpg.ui.pages.LoginPage;
import br.ifsp.rpg.ui.pages.RegisterPage;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;


public class UserRegistrationUITest extends BaseUITest {

    private Faker faker;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        faker = new Faker(new Locale("pt-BR"));
        driver.get(baseUrl);
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Deve registrar um novo usuário com sucesso e redirecionar para login")
    void shouldRegisterNewUserSuccessfullyAndRedirect() {
        LoginPage loginPage = new LoginPage(driver, wait);
        String name = faker.name().firstName();
        String lastname = faker.name().lastName();
        String email = faker.internet().emailAddress();
        String password = faker.internet().password(8, 16, true, true);

        RegisterPage registerPage = loginPage.navigateToRegisterPage();
        registerPage.registerUser(name, lastname, email, password, password);

        String expectedUrl = baseUrl + "/";
        wait.until(ExpectedConditions.urlToBe(expectedUrl));
        assertThat(driver.getCurrentUrl()).isEqualTo(expectedUrl);
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Deve falhar ao registrar com senhas que não coincidem e permanecer na página")
    void shouldFailToRegisterWithMismatchedPasswordsAndStayOnPage() {
        LoginPage loginPage = new LoginPage(driver, wait);
        RegisterPage registerPage = loginPage.navigateToRegisterPage();
        String initialUrl = registerPage.getCurrentUrl();

        String password = faker.internet().password(8, 16);
        String differentPassword = faker.internet().password(8, 16);

        registerPage.registerUser(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().emailAddress(),
                password,
                differentPassword
        );

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertThat(driver.getCurrentUrl()).isEqualTo(initialUrl);
    }


}