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

}
