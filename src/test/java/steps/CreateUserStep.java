package steps;

import driver.DriverManager;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import page.HomePage;

public class CreateUserStep {

    HomePage homePage = new HomePage(DriverManager.getDriver());


    @Given("ingresa en esta URL {string}")
    public void accederURL(String url) {
        homePage.accURL(url);

    }

    @And("da click en {string}")
    public void daClickEn(String arg0) {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("ingresa a la página de registro")
    public void ingresaALaPáginaDeRegistro() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("completa el formulario con los siguientes datos:")
    public void completaElFormularioConLosSiguientesDatos() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @And("deja las preferencias de perfil con sus valores por defecto")
    public void dejaLasPreferenciasDePerfilConSusValoresPorDefecto() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("el sistema redirige a la página de inicio {string}")
    public void elSistemaRedirigeALaPáginaDeInicio(String arg0) {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @And("la página de inicio muestra el texto {string}")
    public void laPáginaDeInicioMuestraElTexto(String arg0) {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @And("ingresa el User ID registrado")
    public void ingresaElUserIDRegistrado() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @And("ingresa la contraseña registrada")
    public void ingresaLaContraseñaRegistrada() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @And("envía el formulario de inicio de sesión")
    public void envíaElFormularioDeInicioDeSesión() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
}
