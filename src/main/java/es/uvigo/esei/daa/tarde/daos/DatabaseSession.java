package es.uvigo.esei.daa.tarde.daos;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public final class DatabaseSession implements AutoCloseable {

    public final EntityManager     manager;
    public final EntityTransaction transaction;

    public static DatabaseSession withoutTransaction( ) {
        return new DatabaseSession();
    }

    public static DatabaseSession withTransaction( ) {
        final DatabaseSession session = withoutTransaction();
        session.transaction.begin();
        return session;
    }

    private DatabaseSession( ) {
        manager     = PersistenceFactory.createEntityManager();
        transaction = manager.getTransaction();
    }

    @Override
    public void close( ) {
        try {
            if (transaction.isActive()) transaction.commit();
        } finally {
            if (transaction.isActive()) transaction.rollback();
            if (manager.isOpen())       manager.close();
        }
    }

}
