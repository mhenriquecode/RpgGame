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
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

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
        CharacterListPage listPage = loginPage.loginAsCharacter(email, password);
        listPage.clicarCriarNovoPersonagem();

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

    @Test
    @Tag("UiTest")
    @DisplayName("Deve exibir erro ao tentar criar personagem com nome muito longo")
    void shouldShowErrorWhenNameIsTooLong() {
        CharacterFormPage form = new CharacterFormPage(driver, wait);
        String longName = "A".repeat(101);
        form.fillCharacterForm(
                longName,
                "ORC",
                "BERSERK",
                "AXE"
        );
        form.submit();
        assertThat(form.getErrorMessage()).isNotEmpty();
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Deve exibir erro ao tentar criar personagem com nome apenas de espaços")
    void shouldShowErrorWhenNameIsWhitespace() {
        CharacterFormPage form = new CharacterFormPage(driver, wait);
        form.fillCharacterForm(
                "     ",
                "ORC",
                "BERSERK",
                "AXE"
        );
        form.submit();
        assertThat(form.getErrorMessage()).isNotEmpty();
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Deve permitir criar personagem com caracteres especiais no nome")
    void shouldAllowSpecialCharactersInName() {
        CharacterFormPage form = new CharacterFormPage(driver, wait);
        form.fillCharacterForm(
                "!@#$%¨&*()_+{}:<>?|",
                "ELF",
                "DUELIST",
                "DAGGER"
        );
        form.submit();
        assertThat(driver.getCurrentUrl()).contains("/personagens/criar");
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Deve aceitar nome com SQL Injection e mostrar personagem criado na lista")
    void shouldShowSqlInjectionNameInCharacterList() {
        String sqlInjection = "'; DROP TABLE characters; --";
        CharacterFormPage form = new CharacterFormPage(driver, wait);
        form.fillCharacterForm(
                sqlInjection,
                "ELF",
                "DUELIST",
                "DAGGER"
        );
        form.submit();

        driver.get(baseUrl + "/personagens/lista");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".character-list-container")));

        List<WebElement> nomes = driver.findElements(By.xpath("//*[contains(text(),\"'; DROP TABLE characters; --\")]"));
        assertThat(nomes).isNotEmpty();

        List<WebElement> erros = driver.findElements(By.className("error-message"));
        assertThat(erros).isEmpty();
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Não deve criar personagem duplicado ao clicar duas vezes rapidamente no botão de criar")
    void shouldNotCreateDuplicateCharacterOnDoubleClick() {
        String name = faker.name().firstName() + System.currentTimeMillis(); // Garantir unicidade no teste
        CharacterFormPage form = new CharacterFormPage(driver, wait);
        form.fillCharacterForm(
                name,
                "ORC",
                "BERSERK",
                "AXE"
        );

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();
        submitButton.click();

        driver.get(baseUrl + "/personagens/lista");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".character-list-container")));

        List<WebElement> nomes = driver.findElements(By.xpath("//*[contains(text(),'" + name + "')]"));
        assertThat(nomes.size()).isEqualTo(1);
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Deve remover espaços extras no início e fim do nome")
    void shouldTrimWhitespaceInName() {
        String name = "   " + faker.name().firstName() + "   ";
        CharacterFormPage form = new CharacterFormPage(driver, wait);
        form.fillCharacterForm(
                name,
                "ORC",
                "BERSERK",
                "AXE"
        );
        form.submit();
        driver.get(baseUrl + "/personagens/lista");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".character-list-container")));
        List<WebElement> nomes = driver.findElements(By.xpath("//*[contains(text(),'" + name.trim() + "')]"));
        assertThat(nomes).isNotEmpty();
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Não deve permitir criar personagem com nome apenas numérico")
    void shouldNotAllowNumericOnlyName() {
        String name = "1234567890";
        CharacterFormPage form = new CharacterFormPage(driver, wait);
        form.fillCharacterForm(
                name,
                "ORC",
                "BERSERK",
                "AXE"
        );
        form.submit();
        assertThat(form.getErrorMessage()).isNotEmpty();

        driver.get(baseUrl + "/personagens/lista");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".character-list-container")));
        List<WebElement> nomes = driver.findElements(By.xpath("//*[contains(text(),'" + name + "')]"));
        assertThat(nomes).isEmpty();
    }

}
