package br.ifsp.rpg.ui.pages;

import org.openqa.selenium.By;
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


    public CombatPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        wait.until(ExpectedConditions.visibilityOf(player1SelectElement));
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


    public void selectCharacter1ByName(String characterName) {
        selectCharacterByPartialText(player1SelectElement, characterName);
    }

    public void selectCharacter2ByName(String characterName) {
        selectCharacterByPartialText(player2SelectElement, characterName);
    }


    public void clickStartCombat() {
        wait.until(ExpectedConditions.elementToBeClickable(startCombatButton));
        startCombatButton.click();
    }

    public boolean isCombatResultVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOf(combatResultContainer));
            return combatResultContainer.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getWinnerName() {
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(60));
        longWait.until(ExpectedConditions.visibilityOf(winnerNameElement));
        return winnerNameElement.getText();
    }
}