package es.uvigo.esei.daa.tarde.entities.articles;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.joda.time.LocalDate;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class BookTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void book_constructor_should_throw_exception_when_initialized_with_null_name( ) {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Article's name cannot be null");

        new Book(null, "description", new LocalDate(), "author", "editorial", new byte[ ] { 0 });
    }

    @Test
    public void book_constructor_should_throw_exception_when_initialized_with_null_description( ) {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Article's description cannot be null");

        new Book("name", null, new LocalDate(), "author", "editorial", new byte[ ] { 0 });
    }

    @Test
    public void book_constructor_should_throw_exception_when_initialized_with_null_date( ) {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Article's date cannot be null");

        new Book("name", "description", null, "author", "editorial", new byte[ ] { 0 });
    }

    @Test
    public void book_constructor_should_throw_exception_when_initialized_with_null_picture( ) {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Article's picture cannot be null");

        new Book("name", "description", new LocalDate(), "author", "editorial", null);
    }

    @Test
    public void book_equality_contract_is_valid( ) {
        EqualsVerifier.forClass(Book.class).withPrefabValues(
            LocalDate.class,
            new LocalDate(2014, 3, 26),
            new LocalDate(2014, 3, 25)
        ).verify();
    }

}
