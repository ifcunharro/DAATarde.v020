package es.uvigo.esei.daa.tarde.daos.articles;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import es.uvigo.esei.daa.tarde.TestUtils;
import es.uvigo.esei.daa.tarde.daos.BaseDAOTest;
import es.uvigo.esei.daa.tarde.entities.articles.Movie;

@RunWith(Parameterized.class)
public class MovieDAOTest extends BaseDAOTest {

    @Parameters(name = "{index}: {0}")
    public static Collection<Object[ ]> createMovieData( ) {
        return Arrays.asList(new Object[ ][ ] {
            { "tarantino", new Movie[ ] {
                new Movie("Reservoir Dogs",      new LocalDate(1992, 1, 1)),
                new Movie("Pulp Fiction",        new LocalDate(1994, 1, 1)),
                new Movie("Jackie Brown",        new LocalDate(1997, 1, 1)),
                new Movie("Kill Bill Vol. 1",    new LocalDate(2003, 1, 1)),
                new Movie("Kill Bill Vol. 2",    new LocalDate(2004, 1, 1)),
                new Movie("Death Proof",         new LocalDate(2007, 1, 1)),
                new Movie("Inglorious Basterds", new LocalDate(2009, 1, 1)),
                new Movie("Django Unchained",    new LocalDate(2012, 1, 1))
            }},
            { "miyazaki", new Movie[ ] {
                new Movie("Nausicaä del Valle del Viento", new LocalDate(1984, 1, 1)),
                new Movie("Mi Vecino Totoro",              new LocalDate(1988, 1, 1)),
                new Movie("La Princesa Mononoke",          new LocalDate(1997, 1, 1)),
                new Movie("El viaje de Chihiro",           new LocalDate(2001, 1, 1)),
                new Movie("El Castillo Ambulante",         new LocalDate(2004, 1, 1)),
                new Movie("Ponyo en el Acantilado",        new LocalDate(2008, 1, 1))
            }},
            { "tarkovsky", new Movie[ ] {
                new Movie("Ivanovo Detstvo", new LocalDate(1962, 1, 1)),
                new Movie("Andrei Rublev",   new LocalDate(1966, 1, 1)),
                new Movie("Solyaris",        new LocalDate(1972, 1, 1)),
                new Movie("Zerkalo",         new LocalDate(1975, 1, 1)),
                new Movie("Stalker",         new LocalDate(1979, 1, 1)),
                new Movie("Nostalghia",      new LocalDate(1983, 1, 1)),
                new Movie("Offret",          new LocalDate(1986, 1, 1))
            }},
            { "anderson", new Movie[ ] {
                new Movie("Bottle Rocket",            new LocalDate(1996, 1, 1)),
                new Movie("Rushmore",                 new LocalDate(1998, 1, 1)),
                new Movie("The Royal Tenenbaums",     new LocalDate(2001, 1, 1)),
                new Movie("Life Aquatic",             new LocalDate(2004, 1, 1)),
                new Movie("The Darjeeling Limited",   new LocalDate(2007, 1, 1)),
                new Movie("Fantastic Mr. Fox",        new LocalDate(2009, 1, 1)),
                new Movie("Moorise Kingdom",          new LocalDate(2012, 1, 1)),
                new Movie("The Grand Budapest Hotel", new LocalDate(2014, 1, 1))
            }}
        });
    }

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    private MovieDAO dao;
    private final List<Movie> movieList;

    public MovieDAOTest(final String _, final Movie [ ] movieList) {
        this.movieList = Arrays.asList(movieList);
    }

    private void unverifyMovie(final Movie m) {
        entityManager.getTransaction().begin();

        final Movie movie = entityManager.find(Movie.class, m.getId());
        movie.setVerified(false);
        entityManager.merge(movie);

        entityManager.getTransaction().commit();
    }

    @Before
    public void createMovieDAO( ) {
        dao = new MovieDAO();
    }

    @Before
    public void insertMovie( ) {
        entityManager.getTransaction().begin();
        for (final Movie m : movieList) {
            m.setVerified(true);
            if (m.getId() == null) entityManager.persist(m);
            else                   entityManager.merge(m);
        }
        entityManager.getTransaction().commit();
    }

    @Test
    public void movie_dao_can_find_movies_by_exact_title( ) {
        for (final Movie movie : movieList) {
            final List<Movie> found = dao.findByName(
                movie.getName(), 1, movieList.size()
            );
            assertThat(found).contains(movie);
        }
    }

    @Test
    public void movie_dao_can_find_movies_by_approximate_title( ) {
        for (final Movie movie : movieList) {
            final String word = movie.getName().split("\\s+")[0];
            final List<Movie> found = dao.findByName(
                word, 1, movieList.size()
            );
            assertThat(found).contains(movie);
        }
    }

    @Test
    public void movie_dao_finds_movies_ignoring_case( ) {
        for (final Movie movie : movieList) {
            final String word = movie.getName().split("\\s+")[0];

            final List<Movie> upper = dao.findByName(
                word.toUpperCase(), 1, movieList.size()
            );
            assertThat(upper).contains(movie);

            final List<Movie> lower = dao.findByName(
                word.toLowerCase(), 1, movieList.size()
            );
            assertThat(lower).contains(movie);

            final List<Movie> rand  = dao.findByName(
                TestUtils.randomizeCase(word), 1, movieList.size()
            );
            assertThat(rand).contains(movie);
        }
    }

    @Test
    public void movie_dao_should_return_all_movies_when_searching_with_empty_title( ) {
        final List<Movie> empty = dao.findByName("", 1, movieList.size());
        assertThat(empty).containsAll(movieList);
    }

    @Test
    public void movie_dao_should_ignore_non_verified_movies_when_searching_by_name( ) {
        for (final Movie movie : movieList) {
            unverifyMovie(movie);

            final List<Movie> found = dao.findByName(
                movie.getName(), 1, movieList.size()
            );
            assertThat(found).doesNotContain(movie);
        }
    }

    @Test
    public void movie_dao_can_insert_movies( ) {
        for (final Movie movie : movieList) {
            final Movie inserted = new Movie(movie.getName(), movie.getDate());
            dao.save(inserted);

            final Long id = inserted.getId();
            assertThat(id).isNotNull();

            final Movie found = entityManager.find(Movie.class, id);
            assertThat(found).isEqualTo(inserted);
        }
    }

    @Test
    public void movie_dao_can_update_movies( ) {
        for (final Movie m : movieList) {
            final Movie movie = entityManager.find(Movie.class, m.getId());
            movie.setVerified(!movie.isVerified());

            dao.save(movie);

            final Movie found = entityManager.find(Movie.class, movie.getId());
            assertThat(found.isVerified()).isEqualTo(movie.isVerified());
        }
    }

    @Test
    public void movie_dao_should_throw_an_exception_when_inserting_an_already_inserted_movie( ) {
        thrown.expect(PersistenceException.class);
        dao.insert(movieList.get(0));
    }

    @Test
    public void movie_dao_can_find_latest_movies( ) {
        assertThat(dao.findLatest(movieList.size())).containsAll(movieList);
    }

    @Test
    public void movie_dao_can_count_results_when_searching_with_empty_name( ) {
        assertThat(dao.countByName("")).isEqualTo(movieList.size());
    }

    @Test
    public void movie_dao_can_count_results_when_searching_with_a_name( ) {
        for (final Movie movie : movieList) {
            final String word  = movie.getName().split("\\s+")[0];

            long counter = 0;
            for (final Movie m : movieList) {
                if (StringUtils.containsIgnoreCase(m.getName(), word))
                    counter++;
            }

            assertThat(dao.countByName(word)).isEqualTo(counter);
        }
    }

    @Test
    public void movie_dao_can_paginate_results_when_searching_by_name( ) {
        final List<Movie> foundMovies = new ArrayList<>();

        for (int page = 1; page <= movieList.size(); ++page) {
            foundMovies.addAll(dao.findByName("", page, 1));
        }

        assertThat(foundMovies).hasSameSizeAs(movieList);
        assertThat(foundMovies).containsAll(movieList);
    }

}
