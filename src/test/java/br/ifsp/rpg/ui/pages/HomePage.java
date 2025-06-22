package br.ifsp.rpg.ui.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage extends BasePage {

    @FindBy(xpath = "//a[text()='Criar Novo Personagem']")
    private WebElement createCharacterButton;

    public HomePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        wait.until(ExpectedConditions.visibilityOf(createCharacterButton));
    }

    public boolean isPageLoaded() {
        return createCharacterButton.isDisplayed();
    }

}
