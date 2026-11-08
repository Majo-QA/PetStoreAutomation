package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RegisterPage extends BasePage {

    // Localizadores: indican a Selenium qué elemento buscar en la página.
    // By.name("...") busca el elemento por su atributo name del HTML.
    private By userIdField = By.name("username");
    private By newPasswordField = By.name("password");
    private By repeatPasswordField = By.name("repeatPassword");
    private By firstNameField = By.name("firstName");
    private By lastNameField = By.name("lastName");
    private By emailField = By.name("email");
    private By phoneField = By.name("phone");
    private By address1Field = By.name("address1");
    private By address2Field = By.name("address2");
    private By cityField = By.name("city");
    private By stateField = By.name("state");
    private By zipField = By.name("zip");
    private By countryField = By.name("country");
    private By languagePreferenceField = By.name("languagePreference");
    private By favouriteCategoryField = By.name("favouriteCategoryId");
    private By enableMyListCheckbox = By.name("listOption");
    private By enableMyBannerCheckbox = By.name("bannerOption");
    private By saveAccountButton = By.name("newAccount");

    // Constructor: recibe el driver y se lo pasa a BasePage.
    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    // Verifica que estamos en la página de registro, revisando que el campo User ID esté visible.
    public boolean isLoaded() {
        return isDisplayed(userIdField);
    }

    public void escribirEnCampo(String campo, String valor) {
        switch (campo) {
            case "User ID":
                type(userIdField, valor);
                break;
            case "New password":
                type(newPasswordField, valor);
                break;
            case "Repeat password":
                type(repeatPasswordField, valor);
                break;
            case "First name":
                type(firstNameField, valor);
                break;
            case "Last name":
                type(lastNameField, valor);
                break;
            case "Email":
                type(emailField, valor);
                break;
            case "Phone":
                type(phoneField, valor);
                break;
            case "Address 1":
                type(address1Field, valor);
                break;
            case "Address 2":
                type(address2Field, valor);
                break;
            case "City":
                type(cityField, valor);
                break;
            case "State":
                type(stateField, valor);
                break;
            case "Zip":
                type(zipField, valor);
                break;
            case "Country":
                type(countryField, valor);
                break;
            default:
                throw new IllegalArgumentException("Campo no reconocido: " + campo);
        }
    }
}
