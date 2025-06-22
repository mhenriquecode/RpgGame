package br.ifsp.rpg.ui.User;

import br.ifsp.rpg.ui.BaseUITest;
import br.ifsp.rpg.ui.pages.LoginPage;
import br.ifsp.rpg.ui.pages.RegisterPage;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.support.ui.WebDriverWait;


import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Locale;

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

}
