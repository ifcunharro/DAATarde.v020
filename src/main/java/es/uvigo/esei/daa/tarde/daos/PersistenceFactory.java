package es.uvigo.esei.daa.tarde.daos;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import es.uvigo.esei.daa.tarde.Config;

public final class PersistenceFactory  {

    private static EntityManagerFactory factory;

    public static void createFactory( ) {
        factory = Persistence.createEntityManagerFactory(
            Config.getString("persistence_unit")
        );
    }

    public static void closeFactory( ) {
        if (factory != null && factory.isOpen()) factory.close();
    }

    public static EntityManager createEntityManager( ) {
        if (factory == null) throw new IllegalStateException(
            "EntityManagerFactory has not yet been created."
        );

        return factory.createEntityManager();
    }

}
