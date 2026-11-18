package steps;

import static org.assertj.core.api.Assertions.assertThat;

import driver.DriverManager;
import io.cucumber.datatable.DataTable;

import java.util.List;
import java.util.Map;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import pages.HomePage;
import pages.LoginPage;
import pages.RegisterPage;
import utils.DatosGenerados;
import utils.RegistroUsuariosCsv;

public class CreateUserStep {

    HomePage homePage = new HomePage(DriverManager.getDriver());
    LoginPage loginPage = new LoginPage(DriverManager.getDriver());
    RegisterPage registerPage = new RegisterPage(DriverManager.getDriver());
    String userId;
    String email;

    @Given("ingresa en esta URL {string}")
    public void accederURL(String url) {
        homePage.accURL(url);

    }

    @And("da click en {string}")
    public void daClickEn(String boton) {
        switch (boton) {
            case "Sign In":
                homePage.darClick();
                break;
            case "Register Now":
                loginPage.darClick();
                break;
            case "Save Account Information":
                registerPage.guardarCuenta();
                break;
        }

    }

    @Then("ingresa a la página de registro")
    public void ingresaALaPáginaDeRegistro() {
        assertThat(registerPage.isLoaded()).isTrue();
    }


    @When("completa el formulario con los siguientes datos:")
    public void completaElFormularioConLosSiguientesDatos(DataTable tabla) {
        List<Map<String, String>> filas = tabla.asMaps(String.class, String.class);

        for (Map<String, String> fila : filas) {
            String campo = fila.get("Campo");
            String valor = fila.get("Valor");

            if (campo.equals("User ID")) {
                valor = registerPage.userCreator();
                this.userId = valor;
            } else if (campo.equals("Email")) {
                valor = registerPage.emailCreator();
                this.email = valor;
            }

            registerPage.escribirEnCampo(campo, valor);
        }
    }

    @And("deja las preferencias de perfil con sus valores por defecto")
    public void dejaLasPreferenciasDePerfilConSusValoresPorDefecto() {
        assertThat(registerPage.sonPreferenciasPorDefecto()).isTrue();
    }

    @Then("el sistema redirige a la página de inicio {string}")
    public void elSistemaRedirigeALaPáginaDeInicio(String rutaEsperada) {
        String palabraClave = rutaEsperada.replace("/", "");
        assertThat(homePage.esperarUrlQueContenga(palabraClave)).isTrue();
        RegistroUsuariosCsv.guardar(this.userId, this.email);
    }

    @And("la página de inicio muestra el texto {string}")
    public void laPáginaDeInicioMuestraElTexto(String textoEsperado) {
        assertThat(homePage.esVisibleDogs()).isTrue();
    }

    @And("ingresa el User ID registrado")
    public void ingresaElUserIDRegistrado() {
        loginPage.ingresarUsername(this.userId);
    }

    @And("ingresa la contraseña registrada")
    public void ingresaLaContraseñaRegistrada() {
        loginPage.ingresarPassword("P@ssw0rd2026!+");
    }

    @And("envía el formulario de inicio de sesión")
    public void envíaElFormularioDeInicioDeSesión() {
        loginPage.enviarFormulario();
    }
}
