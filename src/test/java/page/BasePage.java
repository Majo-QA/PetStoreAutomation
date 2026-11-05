package page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void navigateTo(String url) {
        driver.get(url);
    }

    public WebElement find(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void click(By locator) {
        find(locator).click();
    }

    public void type(By locator, String text) {
        WebElement element = find(locator);
        element.clear();
        element.sendKeys(text);
    }

    public String getText(By locator) {
        return find(locator).getText();
    }

    public boolean isDisplayed(By locator) {
        try {
            return find(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
