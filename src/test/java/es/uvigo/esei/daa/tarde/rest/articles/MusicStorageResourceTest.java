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

import es.uvigo.esei.daa.tarde.daos.articles.MusicStorageDAO;
import es.uvigo.esei.daa.tarde.entities.articles.MusicStorage;

@RunWith(Parameterized.class)
public class MusicStorageResourceTest extends ArticleBaseResourceTest<MusicStorage, MusicStorageDAO> {

    @Parameters(name = "{index}: {0}")
    public static Collection<Object[ ]> createMusicData( ) {
        return Arrays.asList(new Object[ ][ ] {
            { "love", new MusicStorage[ ] {
                new MusicStorage("Loveless",                            new LocalDate(1991, 11,  4)),
                new MusicStorage("Layla and Other Assorted Love Songs", new LocalDate(1970, 11,  1)),
                new MusicStorage("To Bring You My Love",                new LocalDate(1995,  2, 27)),
                new MusicStorage("A Love Supreme",                      new LocalDate(1965,  2,  1)),
                new MusicStorage("Songs of Love and Hate",              new LocalDate(1971,  3, 17))
            }},
            { "zeppelin", new MusicStorage[ ] {
                new MusicStorage("Led Zeppelin",     new LocalDate(1969,  1, 12)),
                new MusicStorage("Led Zeppelin II",  new LocalDate(1969, 10, 22)),
                new MusicStorage("Led Zeppelin III", new LocalDate(1970, 10,  5)),
                new MusicStorage("Led Zeppelin IV",  new LocalDate(1971, 11,  8))
            }},
            { "bieber", new MusicStorage[ ] { }}
        });
    }

    private final String             musicListName;
    private final List<MusicStorage> musicList;

    public MusicStorageResourceTest(final String musicListName, final MusicStorage [ ] musicList) {
        this.musicListName = musicListName;
        this.musicList     = Arrays.asList(musicList);
        registerResourceUnderTest(new MusicStorageResource(mockedDAO));
    }

    @Test
    public void music_storage_resource_is_able_to_search_by_name( ) {
        when(mockedDAO.findByName(
            musicListName, 1, ARTICLES_PER_PAGE
        )).thenReturn(musicList);

        final Response response = jerseyTest.target("articles/music").queryParam(
            "search", musicListName
        ).request().get();

        assertThat(response.getStatus()).isEqualTo(OK_CODE);
        assertThat(response.readEntity(
            new GenericType<List<MusicStorage>>() { }
        )).containsExactlyElementsOf(musicList);
    }

    @Test
    public void music_storage_resource_returns_all_music_storages_when_searching_with_empty_name( ) {
        when(mockedDAO.findByName(
            "", 1, ARTICLES_PER_PAGE
        )).thenReturn(musicList);

        final Response response = jerseyTest.target("articles/music").queryParam(
            "search", ""
        ).request().get();

        assertThat(response.getStatus()).isEqualTo(OK_CODE);
        assertThat(response.readEntity(
            new GenericType<List<MusicStorage>>() { }
        )).containsExactlyElementsOf(musicList);
    }

    @Test
    public void music_storage_resource_returns_a_server_error_code_when_dao_throws_exception_while_searching_by_name( ) {
        when(mockedDAO.findByName(
            musicListName, 1, ARTICLES_PER_PAGE
        )).thenThrow(new PersistenceException());

        final Response response = jerseyTest.target("articles/music").queryParam(
            "search", musicListName
        ).request().get();

        assertThat(response.getStatus()).isEqualTo(SERVER_ERROR_CODE);
    }

    @Test
    public void music_storage_resource_is_able_to_insert_music_storages( ) {
        final Builder request = jerseyTest.target("articles/music").request(
            MediaType.APPLICATION_JSON
        );

        for (final MusicStorage music : musicList) {
            final Response response = request.post(Entity.entity(
                music, MediaType.APPLICATION_JSON_TYPE
            ));

            assertThat(response.getStatus()).isEqualTo(OK_CODE);
            verify(mockedDAO).save(music);
        }
    }

    @Test
    public void music_storage_resource_returns_a_server_error_code_when_dao_throws_exception_while_inserting_a_music_storage( ) {
        final Builder request = jerseyTest.target("articles/music").request(
            MediaType.APPLICATION_JSON
        );

        for (final MusicStorage music : musicList) {
            doThrow(new PersistenceException()).when(mockedDAO).save(music);

            final Response response = request.post(Entity.entity(
                music, MediaType.APPLICATION_JSON_TYPE
            ));

            assertThat(response.getStatus()).isEqualTo(SERVER_ERROR_CODE);
        }
    }

    @Test
    public void music_storage_resource_can_find_latest_music_storages( ) {
        when(mockedDAO.findLatest(ARTICLES_HOME_PAGE)).thenReturn(musicList);

        final Response response = jerseyTest.target(
            "articles/music/latest"
        ).request().get();

        assertThat(response.getStatus()).isEqualTo(OK_CODE);
        assertThat(response.readEntity(
            new GenericType<List<MusicStorage>>() { }
        )).containsExactlyElementsOf(musicList);
    }

    @Test
    public void book_resource_can_paginate_results( ) {
        final int numPages = musicList.size() / ARTICLES_PER_PAGE;

        for (int page = 1; page < numPages; ++page) {
            final int first = (page - 1) * ARTICLES_PER_PAGE;
            final int last  = first + ARTICLES_PER_PAGE;

            when(mockedDAO.findByName(
                musicListName, page, ARTICLES_PER_PAGE
            )).thenReturn(musicList.subList(first, last));

            final Response response = jerseyTest.target(
                "articles/music"
            ).queryParam(
                "search", musicListName
            ).queryParam("page", page).request().get();

            assertThat(response.getStatus()).isEqualTo(OK_CODE);
            assertThat(response.readEntity(
                new GenericType<List<MusicStorage>>() { }
            )).hasSize(ARTICLES_PER_PAGE).containsExactlyElementsOf(
                musicList.subList(first, last)
            );
        }
    }

}
