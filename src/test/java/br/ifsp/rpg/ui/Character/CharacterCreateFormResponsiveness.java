package br.ifsp.rpg.ui.Character;

import br.ifsp.rpg.ui.BaseUITest;
import br.ifsp.rpg.ui.pages.CharacterListPage;
import br.ifsp.rpg.ui.pages.LoginPage;
import br.ifsp.rpg.ui.pages.RegisterPage;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;

import static org.assertj.core.api.Assertions.assertThat;

public class CharacterCreateFormResponsiveness extends BaseUITest {

    private Faker faker;
    private String email;
    private String password;

    @BeforeEach
    public void setUp() {
        super.setUp();
        faker = new Faker();

        driver.get(baseUrl);

        LoginPage loginPage = new LoginPage(driver, wait);
        RegisterPage registerPage = loginPage.navigateToRegisterPage();
        email = faker.internet().emailAddress();
        password = faker.internet().password(8, 16, true, true);

        registerPage.registerUser(
                faker.name().firstName(),
                faker.name().lastName(),
                email,
                password,
                password
        );

        driver.get(baseUrl);
        CharacterListPage listPage = loginPage.loginAsCharacter(email, password);
        listPage.clicarCriarNovoPersonagem();

        driver.get(baseUrl + "/characters");
        listPage.clicarCriarNovoPersonagem();
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Formulário deve ser exibido corretamente em tela desktop")
    void shouldDisplayFormCorrectlyOnDesktop() {
        driver.manage().window().setSize(new Dimension(1280, 800));
        driver.get(baseUrl + "/personagens/criar");
        WebElement formContainer = driver.findElement(By.cssSelector(".character-form-container"));
        assertThat(formContainer.isDisplayed()).isTrue();
        assertThat(driver.findElement(By.id("name")).isDisplayed()).isTrue();
        assertThat(driver.findElement(By.id("race")).isDisplayed()).isTrue();
        assertThat(driver.findElement(By.id("classType")).isDisplayed()).isTrue();
        assertThat(driver.findElement(By.id("weapon")).isDisplayed()).isTrue();
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Formulário deve ser exibido corretamente em tela de tablet")
    void shouldDisplayFormCorrectlyOnTablet() {
        driver.manage().window().setSize(new Dimension(768, 1024));
        driver.get(baseUrl + "/personagens/criar");
        WebElement formContainer = driver.findElement(By.cssSelector(".character-form-container"));
        assertThat(formContainer.isDisplayed()).isTrue();
    }
}
