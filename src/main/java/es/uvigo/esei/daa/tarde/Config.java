package es.uvigo.esei.daa.tarde;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Config {

    private static final Properties CONFIG = new Properties();

    static {
        final ClassLoader loader = Config.class.getClassLoader();
        try (final InputStream input = loader.getResourceAsStream(
            "configuration.properties"
        )) {

            CONFIG.load(input);

        } catch (final IOException ioe) {
            ioe.printStackTrace();
            throw new RuntimeException(
                "Error while loading configuration file."
            );
        }
    }

    public static String getString(final String key) {
        if (CONFIG.containsKey(key))
            return CONFIG.getProperty(key);

        throw new RuntimeException(
            "'" + key + "' key not found in configuration file."
        );
    }

    public static int getInteger(final String key) {
        try {
            return Integer.parseInt(getString(key));
        } catch (final NumberFormatException nfe) {
            nfe.printStackTrace();
            throw new RuntimeException(
                "Invalid number for key " + key + " in configuration file."
            );
        }
    }

}
