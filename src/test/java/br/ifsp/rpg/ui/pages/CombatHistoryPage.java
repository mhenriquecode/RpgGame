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
}