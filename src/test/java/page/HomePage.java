package page;

import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {


    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void accURL(String url) {
        navigateTo(url);
    }
}
