package hook;

import driver.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Hook {

    @Before
    public void setup() {
        DriverManager.getDriver();
    }

    @After
    public void tearDown(Scenario scenario) {
        // Toma una foto de la pantalla en el estado final del escenario
        byte[] captura = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);

        // 1. Adjunta la captura al reporte HTML de Cucumber
        scenario.attach(captura, "image/png", "Estado final");

        // 2. Guarda una copia en la carpeta evidencias/ de la raíz del proyecto
        try {
            Path carpeta = Paths.get("evidencias");
            Files.createDirectories(carpeta);
            String nombre = scenario.getName().replaceAll("[^a-zA-Z0-9-]", "_") + ".png";
            Files.write(carpeta.resolve(nombre), captura);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar la captura", e);
        }

        DriverManager.quitDriver();
    }
}
