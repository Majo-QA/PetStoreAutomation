package pages;

import interfaces.ButtonClick;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage implements ButtonClick {

    private final  By signInButton = By.xpath("//*[@id=\"MenuContent\"]/a[2]");
    private final By dogsLink = By.xpath("//*[@id=\"SidebarContent\"]/a[contains(@href,'categoryId=DOGS')]");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void accURL(String url) {
        navigateTo(url);
    }

    @Override
    public void darClick() {
        click(signInButton);
    }

    public boolean esVisibleDogs() {
        return isDisplayed(dogsLink);
    }
}

