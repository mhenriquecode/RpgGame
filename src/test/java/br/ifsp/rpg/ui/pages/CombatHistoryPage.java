package br.ifsp.rpg.ui.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CombatHistoryPage extends BasePage {

    @FindBy(xpath = "//h2[contains(text(), 'Histórico de Combates')]")
    private WebElement pageTitle;

    @FindBy(className = "history-list")
    private WebElement historyListContainer;

    @FindBy(xpath = "(//li[@class='combat-log-item'])[1]//div[strong/text()='Jogador 1:']")
    private WebElement firstEntryPlayer1Element;

    @FindBy(xpath = "(//li[@class='combat-log-item'])[1]//div[strong/text()='Jogador 2:']")
    private WebElement firstEntryPlayer2Element;

    @FindBy(xpath = "(//li[@class='combat-log-item'])[1]//div[@class='log-winner']")
    private WebElement firstEntryWinnerElement;;

    public CombatHistoryPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        wait.until(ExpectedConditions.visibilityOf(pageTitle));
    }

    public boolean isPageLoaded() {
        return pageTitle.isDisplayed();
    }

    public boolean isHistoryListVisible() {
        try {
            return historyListContainer.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getWinnerOfFirstEntry() {
        wait.until(ExpectedConditions.visibilityOf(firstEntryWinnerElement));
        String fullText = firstEntryWinnerElement.getText();

        String textAfterLabel = fullText.substring(fullText.indexOf(":") + 1);
        return textAfterLabel.split("\\(")[0].trim();
    }

    public String getPlayer1OfFirstEntry() {
        wait.until(ExpectedConditions.visibilityOf(firstEntryPlayer1Element));
        String fullText = firstEntryPlayer1Element.getText();

        String textAfterLabel = fullText.substring(fullText.indexOf(":") + 1);
        return textAfterLabel.split("\\(")[0].trim();
    }

    public String getPlayer2OfFirstEntry() {
        wait.until(ExpectedConditions.visibilityOf(firstEntryPlayer2Element));
        String fullText = firstEntryPlayer2Element.getText();

        String textAfterLabel = fullText.substring(fullText.indexOf(":") + 1);
        return textAfterLabel.split("\\(")[0].trim();
    }
}