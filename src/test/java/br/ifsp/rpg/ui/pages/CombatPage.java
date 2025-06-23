package br.ifsp.rpg.ui.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CombatPage extends BasePage {

    @FindBy(id = "player1")
    private WebElement player1SelectElement;

    @FindBy(id = "player2")
    private WebElement player2SelectElement;

    @FindBy(className = "start-combat-button")
    private WebElement startCombatButton;

    @FindBy(className = "combat-result")
    private WebElement combatResultContainer;

    @FindBy(xpath = "//div[@class='combat-result']//strong")
    private WebElement winnerNameElement;

    @FindBy(className = "back-home-button")
    private WebElement backToHomeButton;

    @FindBy(className = "history-button")
    private WebElement viewCombatHistoryButton;

    public CombatPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        wait.until(ExpectedConditions.visibilityOf(player1SelectElement));
    }

    public void selectCharacter1ByName(String characterName) {
        selectCharacterByPartialText(player1SelectElement, characterName);
    }

    public void selectCharacter2ByName(String characterName) {
        wait.until(ExpectedConditions.elementToBeClickable(player2SelectElement));
        selectCharacterByPartialText(player2SelectElement, characterName);
    }

    private void selectCharacterByPartialText(WebElement selectElement, String partialText) {
        wait.until(ExpectedConditions.elementToBeClickable(selectElement));
        Select select = new Select(selectElement);
        for (WebElement option : select.getOptions()) {
            if (option.getText().contains(partialText)) {
                select.selectByVisibleText(option.getText());
                return;
            }
        }
        throw new org.openqa.selenium.NoSuchElementException("Não foi possível encontrar a opção com o texto: " + partialText);
    }

    public void clickStartCombat() {
        wait.until(ExpectedConditions.elementToBeClickable(startCombatButton));
        startCombatButton.click();
    }

    public String getWinnerName() {
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(60));
        longWait.until(ExpectedConditions.visibilityOf(winnerNameElement));
        return winnerNameElement.getText();
    }

    public HomePage navigateBackToHome() {
        wait.until(ExpectedConditions.elementToBeClickable(backToHomeButton));
        backToHomeButton.click();
        return new HomePage(driver, wait);
    }

    public CombatHistoryPage navigateToCombatHistory() {
        wait.until(ExpectedConditions.elementToBeClickable(viewCombatHistoryButton));
        viewCombatHistoryButton.click();
        wait.until(ExpectedConditions.urlContains("/historico-combates"));

        return new CombatHistoryPage(driver, wait);
    }
}