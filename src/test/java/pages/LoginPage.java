package pages;

import interfaces.ButtonClick;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage implements ButtonClick {

    private By registerNowButton = By.xpath("//*[@id=\"Catalog\"]/a");

    private By usernameField = By.name("username");
    private By passwordField = By.name("password");
    private By loginButton = By.name("signon");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void darClick() {
        click(registerNowButton);
    }

    public void ingresarUsername(String usuario) {
        type(usernameField, usuario);
    }
    public void ingresarPassword(String contrasena) {
        type(passwordField, contrasena);
    }
    public void enviarFormulario() {
        click(loginButton);
    }
}
