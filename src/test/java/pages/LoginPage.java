package pages;

import interfaces.ButtonClick;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage implements ButtonClick {

    private By registerNowButton = By.xpath("//*[@id=\"Catalog\"]/a");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void darClick() {
        click(registerNowButton);
    }
}
