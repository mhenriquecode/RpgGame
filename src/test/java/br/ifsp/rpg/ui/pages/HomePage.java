package br.ifsp.rpg.ui.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage extends BasePage {

    @FindBy(xpath = "//a[text()='Criar Novo Personagem']")
    private WebElement createCharacterButton;

    @FindBy(className = "logout-button")
    private WebElement logoutButton;

    @FindBy(xpath = "//a[text()='Ver Personagens Criados']")
    private WebElement viewCharactersButton;

    @FindBy(xpath = "//a[text()='Iniciar Combate']")
    private WebElement startCombatButton;

    @FindBy(xpath = "//p[contains(text(), 'Bem-vindo')]")
    private WebElement welcomeMessage;
    public HomePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        wait.until(ExpectedConditions.visibilityOf(createCharacterButton));
    }

    public LoginPage logout() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutButton));
        logoutButton.click();
        return new LoginPage(driver, wait);
    }

    public boolean isPageLoaded() {
        return createCharacterButton.isDisplayed();
    }

    public CharacterFormPage navigateToCreateCharacterPage() {
        wait.until(ExpectedConditions.elementToBeClickable(createCharacterButton));
        createCharacterButton.click();
        return new CharacterFormPage(driver, wait);
    }

    public CombatPage navigateToCombatPage() {
        wait.until(ExpectedConditions.elementToBeClickable(startCombatButton));
        startCombatButton.click();
        return new CombatPage(driver, wait);
    }

    public CharacterListPage navigateToCharacterListPage() {
        wait.until(ExpectedConditions.elementToBeClickable(viewCharactersButton));
        viewCharactersButton.click();
        return new CharacterListPage(driver, wait);
    }

}