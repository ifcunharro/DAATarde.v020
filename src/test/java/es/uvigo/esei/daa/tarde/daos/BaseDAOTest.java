package es.uvigo.esei.daa.tarde.daos;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;

public abstract class BaseDAOTest {

    protected static EntityManager entityManager;

    @Before
    public void createEntityManager( ) {
        PersistenceFactory.createFactory();
        entityManager = PersistenceFactory.createEntityManager();
    }

    @After
    public void closeEntityManager( ) {
        entityManager.close();
        PersistenceFactory.closeFactory();
    }

}
