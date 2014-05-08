package es.uvigo.esei.daa.tarde.rest.articles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.PersistenceException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


import es.uvigo.esei.daa.tarde.daos.articles.BookDAO;
import es.uvigo.esei.daa.tarde.entities.articles.Book;

@RunWith(Parameterized.class)
public class BookResourceTest extends ArticleBaseResourceTest<Book, BookDAO> {

    @Parameters(name = "{index}: {0}")
    public static final Collection<Object[ ]> createBookData( ) {
        return Arrays.asList(new Object[ ][ ] {
            { "haskell", new Book[ ] {
                new Book("Learn You a Haskell For Great Good",          new LocalDate(2011,  4, 18)),
                new Book("Real World Haskell",                          new LocalDate(2008, 12,  5)),
                new Book("Beginning Haskell: A Project-Based Approach", new LocalDate(2014,  1, 22))
            }},
            { "scala", new Book[ ] {
                new Book("Programming in Scala",                                 new LocalDate(2011,  1, 10)),
                new Book("Functional Programming Patterns in Scala and Clojure", new LocalDate(2013, 11,  2)),
                new Book("Scala Cookbook",                                       new LocalDate(2013,  8, 23)),
                new Book("Testing in Scala",                                     new LocalDate(2013,  2, 13))
            }},
            { "java", new Book[ ] {
                new Book("Effective Java: Second Edition",   new LocalDate(2008, 5,  8)),
                new Book("Java Concurrency in Practice",     new LocalDate(2006, 5,  9)),
                new Book("The Well-Grounded Java Developer", new LocalDate(2012, 7, 21))
            }},
            { "LOLCODE", new Book[ ] { }}
        });
    }

    private final String     bookListName;
    private final List<Book> bookList;

    public BookResourceTest(final String bookListName, final Book [ ] bookList) {
        this.bookListName = bookListName;
        this.bookList     = Arrays.asList(bookList);
        registerResourceUnderTest(new BookResource(mockedDAO));
    }

    @Test
    public void book_resource_is_able_to_search_by_name( ) {
        when(mockedDAO.findByName(
            bookListName, 1, ARTICLES_PER_PAGE
        )).thenReturn(bookList);

        final Response response = jerseyTest.target("articles/books").queryParam(
            "search", bookListName
        ).request().get();

        assertThat(response.getStatus()).isEqualTo(OK_CODE);
        assertThat(response.readEntity(
            new GenericType<List<Book>>() { }
        )).containsExactlyElementsOf(bookList);
    }

    @Test
    public void book_resource_returns_all_books_when_searching_with_empty_name( ) {
        when(mockedDAO.findByName(
            "", 1, ARTICLES_PER_PAGE
        )).thenReturn(bookList);

        final Response response = jerseyTest.target("articles/books").queryParam(
            "search", ""
        ).request().get();

        assertThat(response.getStatus()).isEqualTo(OK_CODE);
        assertThat(response.readEntity(
            new GenericType<List<Book>>() { }
        )).containsExactlyElementsOf(bookList);
    }

    @Test
    public void book_resource_returns_a_server_error_code_when_dao_throws_exception_while_searching_by_name( ) {
        when(mockedDAO.findByName(
            bookListName, 1, ARTICLES_PER_PAGE
        )).thenThrow(new PersistenceException());

        final Response response = jerseyTest.target("articles/books").queryParam(
            "search", bookListName
        ).request().get();

        assertThat(response.getStatus()).isEqualTo(SERVER_ERROR_CODE);
    }

    @Test
    public void book_resource_is_able_to_insert_books( ) {
        final Builder request = jerseyTest.target("articles/books").request(
            MediaType.APPLICATION_JSON
        );

        for (final Book book : bookList) {
            final Response response = request.post(Entity.entity(
                book, MediaType.APPLICATION_JSON_TYPE
            ));

            assertThat(response.getStatus()).isEqualTo(OK_CODE);
            verify(mockedDAO).save(book);
        }
    }

    @Test
    public void book_resource_returns_a_server_error_code_when_dao_throws_exception_while_inserting_a_book( ) {
        final Builder request = jerseyTest.target("articles/books").request(
            MediaType.APPLICATION_JSON
        );

        for (final Book book : bookList) {
            doThrow(new PersistenceException()).when(mockedDAO).save(book);

            final Response response = request.post(Entity.entity(
                book, MediaType.APPLICATION_JSON_TYPE
            ));

            assertThat(response.getStatus()).isEqualTo(SERVER_ERROR_CODE);
        }
    }

    @Test
    public void book_resource_can_find_latest_books( ) {
        when(mockedDAO.findLatest(ARTICLES_HOME_PAGE)).thenReturn(bookList);

        final Response response = jerseyTest.target(
            "articles/books/latest"
        ).request().get();

        assertThat(response.getStatus()).isEqualTo(OK_CODE);
        assertThat(response.readEntity(
            new GenericType<List<Book>>() { }
        )).containsExactlyElementsOf(bookList);
    }

    @Test
    public void book_resource_can_paginate_results( ) {
        final int numPages = bookList.size() / ARTICLES_PER_PAGE;

        for (int page = 1; page < numPages; ++page) {
            final int first = (page - 1) * ARTICLES_PER_PAGE;
            final int last  = first + ARTICLES_PER_PAGE;

            when(mockedDAO.findByName(
                bookListName, page, ARTICLES_PER_PAGE
            )).thenReturn(bookList.subList(first, last));

            final Response response = jerseyTest.target(
                "articles/books"
            ).queryParam(
                "search", bookListName
            ).queryParam("page", page).request().get();

            assertThat(response.getStatus()).isEqualTo(OK_CODE);
            assertThat(response.readEntity(
                new GenericType<List<Book>>() { }
            )).hasSize(ARTICLES_PER_PAGE).containsExactlyElementsOf(
                bookList.subList(first, last)
            );
        }
    }

}
