package br.ifsp.rpg.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BaseUITest {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected final String baseUrl = "http://localhost:5173";

    @BeforeEach
    public void setUp() {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1920,1080");
        options.addArguments("start-maximized");

        driver = new ChromeDriver(options);

        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

    }

    @AfterEach
    public void tearDown() {

        if(driver != null) {
            driver.quit();
        }

    }

}
