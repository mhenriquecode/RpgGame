package br.ifsp.rpg.ui.Character;

import br.ifsp.rpg.ui.BaseUITest;
import br.ifsp.rpg.ui.pages.CharacterFormPage;
import br.ifsp.rpg.ui.pages.CharacterListPage;
import br.ifsp.rpg.ui.pages.LoginPage;
import br.ifsp.rpg.ui.pages.RegisterPage;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CharacterUITest extends BaseUITest {

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
        loginPage = new LoginPage(driver, wait);
        CharacterListPage listPage = loginPage.login(email, password);

        driver.get(baseUrl + "/characters");
        listPage.clicarCriarNovoPersonagem();
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Deve criar personagem com dados válidos")
    void shouldCreateCharacterWithValidData() {
        CharacterFormPage form = new CharacterFormPage(driver, wait);
        form.fillCharacterForm(
                faker.name().firstName(),
                "ORC",
                "BERSERK",
                "AXE"
        );
        form.submit();
        assertThat(driver.getCurrentUrl()).contains("/personagens/criar");
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Deve exibir erro ao tentar criar personagem sem nome")
    void shouldShowErrorWhenNameIsBlank() {
        CharacterFormPage form = new CharacterFormPage(driver, wait);
        form.fillCharacterForm(
                "",
                "ORC",
                "BERSERK",
                "AXE"
        );
        form.submit();
        assertThat(form.getErrorMessage()).isNotEmpty();
    }
}
