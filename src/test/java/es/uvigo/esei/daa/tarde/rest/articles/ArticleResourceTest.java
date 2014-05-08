package es.uvigo.esei.daa.tarde.rest.articles;

import static org.assertj.core.api.Assertions.assertThat;
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

import es.uvigo.esei.daa.tarde.daos.articles.ArticleDAO;
import es.uvigo.esei.daa.tarde.entities.articles.Article;
import es.uvigo.esei.daa.tarde.entities.articles.Book;
import es.uvigo.esei.daa.tarde.entities.articles.Comic;
import es.uvigo.esei.daa.tarde.entities.articles.Movie;

@RunWith(Parameterized.class)
public class ArticleResourceTest extends ArticleBaseResourceTest<Article, ArticleDAO> {

    @Parameters(name = "{index}: {0}")
    public static final Collection<Object[ ]> createArticleData( ) {
        return Arrays.asList(new Object[ ][ ] {
            { "batman", new Article[ ] {
                new Comic("Batman: The Dark Knight Returns",                    new LocalDate(1986,  2,  1)),
                new Comic("Batman: Year One",                                   new LocalDate(1987,  2,  1)),
                new Comic("Batman: The Long Halloween",                         new LocalDate(1996,  1, 12)),
                new Comic("Batman: Hush",                                       new LocalDate(2002,  1, 12)),
                new Comic("Batman: The Man Who Laughs",                         new LocalDate(2005,  2,  1)),
                new Comic("Batman: Dark Moon Rising",                           new LocalDate(2006,  1,  1)),
                new Movie("Batman: Mask of the Phantom",                        new LocalDate(1993, 12, 25)),
                new Movie("Batman & Mr. Freeze: SubZero",                       new LocalDate(1998,  2, 11)),
                new Movie("Batman Beyond: Return of the Joker",                 new LocalDate(2000, 12, 12)),
                new Movie("Batman: Mystery of the Batwoman",                    new LocalDate(2003, 10, 21)),
                new Movie("Batman: Unther the Red Hood",                        new LocalDate(2010,  7, 27)),
                new Movie("Batman",                                             new LocalDate(1989,  6, 19)),
                new Movie("Batman Returns",                                     new LocalDate(1992,  6, 16)),
                new  Book("Batman and Philosophy: The Dark Knight of the Soul", new LocalDate(2008,  6, 23)),
                new  Book("Batman and Psychology: A Dark and Stormy Knight",    new LocalDate(2012,  6,  1))
            }}
        });
    }

    private final String        articleListName;
    private final List<Article> articleList;

    public ArticleResourceTest(final String articleListName, final Article[ ] articleList) {
        this.articleListName = articleListName;
        this.articleList     = Arrays.asList(articleList);
        registerResourceUnderTest(new ArticleResource(mockedDAO));
    }

    @Test
    public void article_resource_is_able_to_search_by_name( ) {
        when(mockedDAO.findByName(
            articleListName, 1, ARTICLES_PER_PAGE
        )).thenReturn(articleList);

        final Response response = jerseyTest.target("articles").queryParam(
            "search", articleListName
        ).request().get();

        assertThat(response.getStatus()).isEqualTo(OK_CODE);
        assertThat(response.readEntity(
            new GenericType<List<Article>>() { }
        )).containsExactlyElementsOf(articleList);
    }

    @Test
    public void article_resource_returns_all_articles_when_searching_with_empty_name( ) {
        when(mockedDAO.findByName(
            "", 1, ARTICLES_PER_PAGE
        )).thenReturn(articleList);

        final Response response = jerseyTest.target("articles").queryParam(
            "search", ""
        ).request().get();

        assertThat(response.getStatus()).isEqualTo(OK_CODE);
        assertThat(response.readEntity(
            new GenericType<List<Article>>() { }
        )).containsExactlyElementsOf(articleList);
    }

    @Test
    public void article_resource_returns_a_server_error_code_when_dao_throws_exception_while_searching_by_name( ) {
        when(mockedDAO.findByName(
            articleListName, 1, ARTICLES_PER_PAGE
        )).thenThrow(new PersistenceException());

        final Response response = jerseyTest.target("articles").queryParam(
            "search", articleListName
        ).request().get();

        assertThat(response.getStatus()).isEqualTo(SERVER_ERROR_CODE);
    }

    @Test
    public void article_resource_returns_a_bad_request_code_when_trying_to_insert_an_article( ) {
        final Builder request = jerseyTest.target("articles").request(
            MediaType.APPLICATION_JSON
        );

        for (final Article article : articleList) {
            final Response response = request.post(Entity.entity(
                article, MediaType.APPLICATION_JSON_TYPE
            ));
            assertThat(response.getStatus()).isEqualTo(BAD_REQUEST_CODE);
        }
    }

    @Test
    public void article_resource_can_find_latest_articles( ) {
        when(mockedDAO.findLatest(ARTICLES_HOME_PAGE)).thenReturn(articleList);

        final Response response = jerseyTest.target(
            "articles/latest"
        ).request().get();

        assertThat(response.getStatus()).isEqualTo(OK_CODE);
        assertThat(response.readEntity(
            new GenericType<List<Article>>() { }
        )).containsExactlyElementsOf(articleList);
    }

    @Test
    public void article_resource_can_paginate_results( ) {
        final int numPages = articleList.size() / ARTICLES_PER_PAGE;

        for (int page = 1; page < numPages; ++page) {
            final int first = (page - 1) * ARTICLES_PER_PAGE;
            final int last  = first + ARTICLES_PER_PAGE;

            when(mockedDAO.findByName(
                articleListName, page, ARTICLES_PER_PAGE
            )).thenReturn(articleList.subList(first, last));

            final Response response = jerseyTest.target("articles").queryParam(
                "search", articleListName
            ).queryParam("page", page).request().get();

            assertThat(response.getStatus()).isEqualTo(OK_CODE);
            assertThat(response.readEntity(
                new GenericType<List<Article>>() { }
            )).hasSize(ARTICLES_PER_PAGE).containsExactlyElementsOf(
                articleList.subList(first, last)
            );
        }
    }

}
