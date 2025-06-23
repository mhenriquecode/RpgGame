package br.ifsp.rpg.ui.Character;

import br.ifsp.rpg.ui.BaseUITest;
import br.ifsp.rpg.ui.pages.CharacterListPage;
import br.ifsp.rpg.ui.pages.CharacterFormPage;
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

public class CharacterListUITest extends BaseUITest {

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
    @DisplayName("Deve deletar personagem da lista ao clicar no botão deletar")
    void shouldDeleteCharacterFromList() {
        String name = "PersonagemParaDeletar" + System.currentTimeMillis();
        CharacterFormPage form = new CharacterFormPage(driver, wait);
        form.fillCharacterForm(
                name,
                "ELF",
                "DUELIST",
                "DAGGER"
        );
        form.submit();

        driver.get(baseUrl + "/personagens/lista");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".character-list-container")));

        List<WebElement> items = driver.findElements(By.xpath("//li[contains(.,'" + name + "')]"));
        assertThat(items).isNotEmpty();

        WebElement item = items.get(0);
        WebElement deleteButton = item.findElement(By.cssSelector(".delete-button"));

        deleteButton.click();

        driver.switchTo().alert().accept();

        wait.until(ExpectedConditions.invisibilityOf(item));

        List<WebElement> nomesApos = driver.findElements(By.xpath("//*[contains(text(),'" + name + "')]"));
        assertThat(nomesApos).isEmpty();
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Deve filtrar personagens pelo nome no campo de busca")
    void shouldFilterCharactersByName() {
        String name = "PersonagemBusca" + System.currentTimeMillis();
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

        WebElement searchInput = driver.findElement(By.cssSelector(".search-input-charlist"));
        searchInput.sendKeys(name);

        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".character-list-container"), name));

        List<WebElement> nomes = driver.findElements(By.xpath("//*[contains(text(),'" + name + "')]"));
        assertThat(nomes).isNotEmpty();

        WebElement clearButton = driver.findElement(By.cssSelector(".clear-search-button-charlist"));
        clearButton.click();
        wait.until(ExpectedConditions.attributeToBe(searchInput, "value", ""));
    }
}
