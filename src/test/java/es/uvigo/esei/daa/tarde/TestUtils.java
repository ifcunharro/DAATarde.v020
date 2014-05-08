package es.uvigo.esei.daa.tarde;

import java.util.Random;

public final class TestUtils {

    public static final String randomizeCase(final String str) {
        final Random random = new Random();

        final StringBuilder builder = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (random.nextBoolean())
                builder.append(Character.toUpperCase(c));
            else
                builder.append(Character.toLowerCase(c));
        }

        return builder.toString();
    }

}
