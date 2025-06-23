package br.ifsp.rpg.ui.Combat;

import br.ifsp.rpg.ui.BaseUITest;
import br.ifsp.rpg.ui.pages.*;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CombatUITest extends BaseUITest {

    private Faker faker;
    private String email;
    private String password;
    private HomePage homePage;
    private String character1Name;
    private String character2Name;
    private CombatPage combatPage;

    @BeforeEach
    public void setUp() {
        super.setUp();
        faker = new Faker();

        driver.get(baseUrl);
        LoginPage loginPage = new LoginPage(driver, wait);
        RegisterPage registerPage = loginPage.navigateToRegisterPage();
        email = faker.internet().emailAddress();
        password = faker.internet().password(8, 16, true, true);
        registerPage.registerUser(faker.name().firstName(), faker.name().lastName(), email, password, password);
        wait.until(ExpectedConditions.urlToBe(baseUrl + "/"));
        driver.get(baseUrl);
        homePage = loginPage.login(email, password);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Sair']")));

        character1Name = faker.name().firstName() + " o Guerreiro";
        character2Name = faker.name().firstName() + " o Arqueiro";

        CharacterFormPage characterFormPage = homePage.navigateToCreateCharacterPage();

        characterFormPage.createCharacter(character1Name, "ORC", "BERSERK", "SWORD");

        characterFormPage.createCharacter(character2Name, "HUMAN", "PALADIN", "AXE");

        driver.get(baseUrl);
        combatPage = homePage.navigateToCombatPage();
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Deve iniciar um combate com sucesso com entradas válidas")
    void shouldStartCombatSuccessfullyWithValidInputs() {
        combatPage.selectCharacter1ByName(character1Name);
        combatPage.selectCharacter2ByName(character2Name);

        combatPage.clickStartCombat();
        String winnerName = combatPage.getWinnerName();

        assertThat(winnerName).isNotBlank();
        assertThat(winnerName).isIn(character1Name, character2Name);
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Deve navegar de volta para a tela inicial após o combate")
    void shouldNavigateBackToHomePageAfterCombat() {
        combatPage.selectCharacter1ByName(character1Name);
        combatPage.selectCharacter2ByName(character2Name);
        combatPage.clickStartCombat();
        combatPage.getWinnerName();

        HomePage newHomePage = combatPage.navigateBackToHome();

        assertThat(newHomePage.isPageLoaded()).isTrue();
        assertThat(driver.getCurrentUrl()).isEqualTo(baseUrl + "/");
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Deve navegar para a tela de Histórico de Combate após o combate")
    void shouldNavigateToCombatHistoryPageAfterCombat() {
        combatPage.selectCharacter1ByName(character1Name);
        combatPage.selectCharacter2ByName(character2Name);
        combatPage.clickStartCombat();
        combatPage.getWinnerName();

        CombatHistoryPage historyPage = combatPage.navigateToCombatHistory();

        assertThat(historyPage.isPageLoaded()).isTrue();
        assertThat(driver.getCurrentUrl()).endsWith("/historico-combates");
    }

}
