package es.uvigo.esei.daa.tarde;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ConfigTest {

    private Properties configuration;

    @Before
    public void loadConfigurationFile( ) throws IOException {
        configuration = new Properties();
        configuration.load(getClass().getClassLoader().getResourceAsStream(
            "configuration.properties"
        ));
    }

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void config_class_can_read_strings_from_configuration_file( ) {
        final String stringKey = "testString";

        final String returnedValue = Config.getString(stringKey);
        final String actualValue   = configuration.getProperty(stringKey);
        assertThat(returnedValue).isEqualTo(actualValue);
    }

    @Test
    public void config_class_should_throw_an_exception_when_reading_a_nonexistent_string( ) {
        final String nonExistentKey = "THIS_IS_MY_NONEXISTENT_KEY";

        thrown.expect(RuntimeException.class);
        thrown.expectMessage(
            "'" + nonExistentKey + "' key not found in configuration file."
        );

        Config.getString(nonExistentKey);
    }

    @Test
    public void config_class_can_read_integers_from_configuration_file( ) {
        final String integerKey = "testInteger";

        final int returnedValue = Config.getInteger(integerKey);
        final int actualValue   = Integer.parseInt(
            configuration.getProperty(integerKey)
        );
        assertThat(returnedValue).isEqualTo(actualValue);
    }

    @Test
    public void config_class_should_throw_an_exception_when_reading_a_nonexistent_integer( ) {
        final String nonExistentKey = "THIS_IS_MY_NONEXISTENT_KEY";

        thrown.expect(RuntimeException.class);
        thrown.expectMessage(
            "'" + nonExistentKey + "' key not found in configuration file."
        );

        Config.getInteger(nonExistentKey);
    }

    @Test
    public void config_class_should_throw_an_exception_when_reading_an_integer_with_invalid_value( ) {
        final String noIntKey = "testString";

        thrown.expect(RuntimeException.class);
        thrown.expectMessage(
            "Invalid number for key " + noIntKey + " in configuration file."
        );

        Config.getInteger(noIntKey);
    }

}
