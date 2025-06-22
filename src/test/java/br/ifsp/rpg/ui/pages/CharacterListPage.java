package br.ifsp.rpg.ui.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CharacterListPage extends BasePage {

    @FindBy(id = "create-character-button")
    private WebElement createCharacterButton;

    public CharacterListPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        wait.until(ExpectedConditions.visibilityOf(createCharacterButton));
    }

    public String getHeaderText() {
        return driver.getTitle();
    }
}