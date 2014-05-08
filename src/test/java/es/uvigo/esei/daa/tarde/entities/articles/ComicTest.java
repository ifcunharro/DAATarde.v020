package es.uvigo.esei.daa.tarde.entities.articles;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.joda.time.LocalDate;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ComicTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void comic_constructor_should_throw_exception_when_initialized_with_null_name( ) {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Article's name cannot be null");

        new Comic(null, "description", new LocalDate(),"serie","collection","editorial", new byte[ ] { 0 });
    }

    @Test
    public void comic_constructor_should_throw_exception_when_initialized_with_null_description( ) {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Article's description cannot be null");

        new Comic("name", null, new LocalDate(),"serie","collection","editorial", new byte[ ] { 0 });
    }

    @Test
    public void comic_constructor_should_throw_exception_when_initialized_with_null_date( ) {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Article's date cannot be null");

        new Comic("name", "description", null,"serie","collection","editorial", new byte[ ] { 0 });
    }

    @Test
    public void comic_constructor_should_throw_exception_when_initialized_with_null_picture( ) {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Article's picture cannot be null");

        new Comic("name", "description", new LocalDate(),"serie","collection","editorial", null);
    }

    @Test
    public void comic_equality_contract_is_valid( ) {
        EqualsVerifier.forClass(Comic.class).withPrefabValues(
            LocalDate.class,
            new LocalDate(2014, 3, 26),
            new LocalDate(2014, 3, 25)
        ).verify();
    }

}
