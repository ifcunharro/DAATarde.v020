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
import es.uvigo.esei.daa.tarde.entities.articles.MusicStorage;

@RunWith(Parameterized.class)
public class MusicStorageDAOTest extends BaseDAOTest {

    @Parameters(name = "{index}: {0}")
    public static Collection<Object[ ]> createMusicData( ) {
        return Arrays.asList(new Object[ ][ ] {
            { "which one's pink?", new MusicStorage[ ] {
                new MusicStorage("Meddle",                    new LocalDate(1971, 11, 13)),
                new MusicStorage("The Dark Side of the Moon", new LocalDate(1973,  3, 24)),
                new MusicStorage("Wish You Were Here",        new LocalDate(1975,  9, 15)),
                new MusicStorage("Animals",                   new LocalDate(1977,  1, 23)),
                new MusicStorage("The Wall",                  new LocalDate(1979, 11, 30))
            }},
            { "metallica", new MusicStorage[ ] {
                new MusicStorage("Kill 'Em All",       new LocalDate(1983, 7, 25)),
                new MusicStorage("Ride the Lightning", new LocalDate(1984, 7, 27)),
                new MusicStorage("Master of Puppets",  new LocalDate(1986, 2, 24))
            }},
            { "ulver", new MusicStorage[ ] {
                new MusicStorage("Bergtatt",           new LocalDate(1995,  2,  1)),
                new MusicStorage("Kveldssanger ",      new LocalDate(1996,  1,  1)),
                new MusicStorage("Nattens madrigal ",  new LocalDate(1997,  3,  1)),
                new MusicStorage("Perdition City",     new LocalDate(2000,  3, 26)),
                new MusicStorage("Blood Inside ",      new LocalDate(2005,  7, 12)),
                new MusicStorage("Shadows of the Sun", new LocalDate(2007, 10,  1)),
                new MusicStorage("Wars of the Roses",  new LocalDate(2011,  4, 11)),
                new MusicStorage("Childhood's End",    new LocalDate(2012,  5, 28))
            }},
            { "kendrick", new MusicStorage[ ] {
                new MusicStorage("Section.80",              new LocalDate(2011, 7,  2)),
                new MusicStorage("good kid, m.A.A.d city ", new LocalDate(2012, 9, 22))
            }}
        });
    }

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    private MusicStorageDAO dao;
    private final List<MusicStorage> musicList;

    public MusicStorageDAOTest(
        final String _, final MusicStorage[ ] musicList
            ) {
        this.musicList = Arrays.asList(musicList);
    }

    private void unverifyMusic(final MusicStorage m) {
        entityManager.getTransaction().begin();

        final MusicStorage music = entityManager.find(
            MusicStorage.class, m.getId()
        );
        music.setVerified(false);
        entityManager.merge(music);

        entityManager.getTransaction().commit();
    }

    @Before
    public void createMusicStorageDAO( ) {
        dao = new MusicStorageDAO();
    }

    @Before
    public void insertMusicStorage( ) {
        entityManager.getTransaction().begin();
        for (final MusicStorage m : musicList) {
            m.setVerified(true);
            if (m.getId() == null) entityManager.persist(m);
            else                   entityManager.merge(m);
        }
        entityManager.getTransaction().commit();
    }

    @Test
    public void music_storage_dao_can_find_music_storages_by_exact_title( ) {
        for (final MusicStorage music : musicList) {
            final List<MusicStorage> found = dao.findByName(
                music.getName(), 1, musicList.size()
            );
            assertThat(found).contains(music);
        }
    }

    @Test
    public void music_storage_dao_can_find_music_storages_by_approximate_title( ) {
        for (final MusicStorage music : musicList) {
            final String word = music.getName().split("\\s+")[0];
            final List<MusicStorage> found = dao.findByName(
                word, 1, musicList.size()
            );
            assertThat(found).contains(music);
        }
    }

    @Test
    public void music_storage_dao_finds_music_storages_ignoring_case( ) {
        for (final MusicStorage music : musicList) {
            final String word = music.getName().split("\\s+")[0];

            final List<MusicStorage> upper = dao.findByName(
                word.toUpperCase(), 1, musicList.size()
            );
            assertThat(upper).contains(music);

            final List<MusicStorage> lower = dao.findByName(
                word.toLowerCase(), 1, musicList.size()
            );
            assertThat(lower).contains(music);

            final List<MusicStorage> rand  = dao.findByName(
                TestUtils.randomizeCase(word), 1, musicList.size()
            );
            assertThat(rand).contains(music);
        }
    }

    @Test
    public void music_storage_dao_should_return_all_music_storages_when_searching_with_empty_title( ) {
        final List<MusicStorage> empty = dao.findByName(
            "", 1, musicList.size()
        );
        assertThat(empty).containsAll(musicList);
    }

    @Test
    public void music_storage_dao_should_ignore_non_verified_music_storages_when_searching_by_name( ) {
        for (final MusicStorage music : musicList) {
            unverifyMusic(music);

            final List<MusicStorage> found = dao.findByName(
                music.getName(), 1, musicList.size()
            );
            assertThat(found).doesNotContain(music);
        }
    }

    @Test
    public void music_storage_dao_can_insert_music_storages( ) {
        for (final MusicStorage music : musicList) {
            final MusicStorage inserted = new MusicStorage(
                music.getName(), music.getDate()
                    );
            dao.save(inserted);

            final Long id = inserted.getId();
            assertThat(id).isNotNull();

            final MusicStorage found = entityManager.find(
                MusicStorage.class, id
                    );
            assertThat(found).isEqualTo(inserted);
        }
    }

    @Test
    public void music_storage_dao_can_update_music_storages( ) {
        for (final MusicStorage m : musicList) {
            final MusicStorage music = entityManager.find(
                MusicStorage.class, m.getId()
            );
            music.setVerified(!music.isVerified());

            dao.save(music);

            final MusicStorage found = entityManager.find(
                MusicStorage.class, music.getId()
            );
            assertThat(found.isVerified()).isEqualTo(music.isVerified());
        }
    }

    @Test
    public void music_storage_dao_should_throw_an_exception_when_inserting_an_already_inserted_music_storage( ) {
        thrown.expect(PersistenceException.class);
        dao.insert(musicList.get(0));
    }

    @Test
    public void music_storage_dao_can_find_latest_music_storages( ) {
        assertThat(dao.findLatest(musicList.size())).containsAll(musicList);
    }

    @Test
    public void music_storage_dao_can_count_results_when_searching_with_empty_name( ) {
        assertThat(dao.countByName("")).isEqualTo(musicList.size());
    }

    @Test
    public void music_storage_dao_can_count_results_when_searching_with_a_name( ) {
        for (final MusicStorage music : musicList) {
            final String word  = music.getName().split("\\s+")[0];

            long counter = 0;
            for (final MusicStorage m : musicList) {
                if (StringUtils.containsIgnoreCase(m.getName(), word))
                    counter++;
            }

            assertThat(dao.countByName(word)).isEqualTo(counter);
        }
    }

    @Test
    public void music_storage_dao_can_paginate_results_when_searching_by_name( ) {
        final List<MusicStorage> foundMusic = new ArrayList<>();

        for (int page = 1; page <= musicList.size(); ++page) {
            foundMusic.addAll(dao.findByName("", page, 1));
        }

        assertThat(foundMusic).hasSameSizeAs(musicList);
        assertThat(foundMusic).containsAll(musicList);
    }

}
