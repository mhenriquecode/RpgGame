package br.ifsp.rpg.ui.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CharacterFormPage extends BasePage {

    @FindBy(id = "name")
    private WebElement nameInput;

    @FindBy(id = "race")
    private WebElement raceSelect;

    @FindBy(id = "classType")
    private WebElement classTypeSelect;

    @FindBy(id = "weapon")
    private WebElement weaponSelect;

    @FindBy(css = "button[type='submit']")
    private WebElement submitButton;

    @FindBy(className = "success-message")
    private WebElement successMessage;

    @FindBy(className = "error-message")
    private WebElement errorMessage;

    public CharacterFormPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        wait.until(ExpectedConditions.visibilityOf(nameInput));
    }

    public void fillCharacterForm(String name, String race, String classType, String weapon) {
        wait.until(ExpectedConditions.attributeToBe(nameInput, "value", ""));
        nameInput.sendKeys(name);

        Select raceDropdown = new Select(raceSelect);
        raceDropdown.selectByValue(race);

        Select classTypeDropdown = new Select(classTypeSelect);
        classTypeDropdown.selectByValue(classType);

        Select weaponDropdown = new Select(weaponSelect);
        weaponDropdown.selectByValue(weapon);
    }

    public void submit() {
        submitButton.click();
    }

    public String getErrorMessage() {
        wait.until(ExpectedConditions.visibilityOf(errorMessage));
        return errorMessage.getText();
    }

    public void createCharacter(String name, String race, String classType, String weapon) {
        this.fillCharacterForm(name, race, classType, weapon);
        this.submit();
        wait.until(ExpectedConditions.visibilityOf(successMessage));
    }
}