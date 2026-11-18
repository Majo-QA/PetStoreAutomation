package utils;

import org.apache.commons.lang3.RandomStringUtils;

public class DatosGenerados {

    // User ID: "U" + 9 caracteres alfanuméricos aleatorios
    public static String generarUserId() {
        return "U" + RandomStringUtils.randomAlphanumeric(9);
    }

    // Email: qa_user_ + timestamp + @testpetstore.com
    public static String generarEmail() {
        return "qa_user_" + System.currentTimeMillis() + "@testpetstore.com";
    }

}
