package br.ifsp.rpg.ui.pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends BasePage {

    @FindBy(id = "login-email")
    private WebElement emailInput;

    @FindBy(id = "login-password")
    private WebElement passwordInput;

    @Getter
    @FindBy(css = "form button[type='submit']")
    private WebElement loginButton;

    @FindBy(className = "link-button")
    private WebElement registerLinkButton;

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        wait.until(ExpectedConditions.visibilityOf(emailInput));
    }

    public HomePage login(String email, String password) {
        emailInput.sendKeys(email);
        passwordInput.sendKeys(password);
        loginButton.click();
        return new HomePage(driver, wait);
    }

    public RegisterPage navigateToRegisterPage() {
        registerLinkButton.click();
        return new RegisterPage(driver, wait);
    }

    public String getErrorMessage() {
        WebElement messageElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.className("error-message"))
        );
        return messageElement.getText();
    }

}