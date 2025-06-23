package br.ifsp.rpg.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CombatPage extends BasePage {

    @FindBy(id = "player1")
    private WebElement player1SelectElement;

    @FindBy(id = "player2")
    private WebElement player2SelectElement;

    @FindBy(className = "start-combat-button")
    private WebElement startCombatButton;

    @FindBy(className = "combat-result")
    private WebElement combatResultContainer;

    @FindBy(xpath = "//div[@class='combat-result']//p[contains(., 'Vencedor:')]/strong")
    private WebElement winnerTextElement;


    public CombatPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        wait.until(ExpectedConditions.visibilityOf(player1SelectElement));
    }


    public void selectCharacter1ByIndex(int index) {
        Select select = new Select(player1SelectElement);
        select.selectByIndex(index);
    }


    public void selectCharacter2ByIndex(int index) {
        Select select = new Select(player2SelectElement);
        select.selectByIndex(index);
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
        wait.until(ExpectedConditions.visibilityOf(winnerTextElement));
        WebElement parentParagraph = winnerTextElement.findElement(By.xpath("./.."));
        return parentParagraph.getText().replace("🎉 O vencedor é:", "").replace("🎉", "").trim();
    }
}