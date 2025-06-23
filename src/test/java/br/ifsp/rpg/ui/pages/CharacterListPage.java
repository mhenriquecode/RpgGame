package br.ifsp.rpg.ui.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CharacterListPage extends BasePage {

    @FindBy(css = "a.characters-create-button")
    private WebElement criarNovoPersonagemLink;

    public CharacterListPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        wait.until(ExpectedConditions.visibilityOf(criarNovoPersonagemLink));
    }

    public void clicarCriarNovoPersonagem() {
        wait.until(ExpectedConditions.visibilityOf(criarNovoPersonagemLink));
        criarNovoPersonagemLink.click();
    }

    public String getHeaderText() {
        return driver.getTitle();
    }
}