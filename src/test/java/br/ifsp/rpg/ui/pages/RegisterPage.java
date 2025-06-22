package br.ifsp.rpg.ui.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RegisterPage extends BasePage {

    @FindBy(id = "register-name")
    private WebElement nameInput;

    @FindBy(id = "register-lastname")
    private WebElement lastnameInput;

    @FindBy(id = "register-email")
    private WebElement emailInput;

    @FindBy(id = "register-password")
    private WebElement passwordInput;

    @FindBy(id = "register-confirm-password")
    private WebElement confirmPasswordInput;

    @FindBy(css = "form button[type='submit']")
    private WebElement registerButton;


    public RegisterPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        wait.until(ExpectedConditions.visibilityOf(nameInput));
    }

    public void registerUser(String name, String lastname, String email, String password, String confirmPassword) {
        nameInput.sendKeys(name);
        lastnameInput.sendKeys(lastname);
        emailInput.sendKeys(email);
        passwordInput.sendKeys(password);
        confirmPasswordInput.sendKeys(confirmPassword);

        wait.until(ExpectedConditions.elementToBeClickable(registerButton));
        registerButton.click();
    }
}