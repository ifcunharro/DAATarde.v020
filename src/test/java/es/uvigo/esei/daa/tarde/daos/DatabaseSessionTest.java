package es.uvigo.esei.daa.tarde.daos;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.junit.Test;

public class DatabaseSessionTest extends BaseDAOTest {

    @Test
    public void database_session_closes_the_entity_manager_when_autoclosing( ) {
        EntityManager manager;

        try (DatabaseSession session = DatabaseSession.withoutTransaction()) {
            manager = session.manager;
            assertThat(manager.isOpen()).isTrue();
        }

        assertThat(manager.isOpen()).isFalse();
    }

    @Test
    public void database_session_commits_or_rollbacks_transaction_when_autoclosing( ) {
        EntityTransaction transaction;

        try (DatabaseSession session = DatabaseSession.withTransaction()) {
            transaction = session.manager.getTransaction();
            assertThat(transaction.isActive()).isTrue();
        }

        assertThat(transaction.isActive()).isFalse();
    }

    @Test
    public void database_session_holds_a_reference_to_the_entity_transaction( ) {
        final DatabaseSession session = DatabaseSession.withTransaction();

        assertThat(session.transaction).isEqualTo(
            session.manager.getTransaction()
        );

        session.close();
    }

    @Test
    public void database_session_creates_a_new_entity_manager_from_configured_factory( ) {
        final DatabaseSession session = DatabaseSession.withoutTransaction();

        assertThat(session.manager.getEntityManagerFactory()).isEqualTo(
            entityManager.getEntityManagerFactory()
        );

        session.close();
    }

    @Test
    public void database_session_does_not_start_a_transaction_when_created_without_it( ) {
        final DatabaseSession session = DatabaseSession.withoutTransaction();

        assertThat(session.manager.getTransaction().isActive()).isFalse();

        session.close();
    }

    @Test
    public void database_session_starts_a_transaction_when_created_with_it( ) {
        final DatabaseSession session = DatabaseSession.withTransaction();

        assertThat(session.manager.getTransaction().isActive()).isTrue();

        session.close();
    }

}
