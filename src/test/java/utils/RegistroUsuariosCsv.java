package utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class RegistroUsuariosCsv {

    // Ruta relativa: el archivo se crea en la raíz del proyecto
    private static final Path ARCHIVO = Paths.get("usuarios_registrados.csv");

    public static void guardar(String userId, String email) {
        try {
            // Si el archivo no existe, se crea con la cabecera
            if (!Files.exists(ARCHIVO)) {
                Files.write(ARCHIVO, "userId,email\n".getBytes(StandardCharsets.UTF_8));
            }
            String linea = userId + "," + email + "\n";
            Files.write(ARCHIVO, linea.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar el usuario en el CSV", e);
        }
    }
}