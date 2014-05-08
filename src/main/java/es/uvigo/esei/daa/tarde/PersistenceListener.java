package es.uvigo.esei.daa.tarde;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import es.uvigo.esei.daa.tarde.daos.PersistenceFactory;

@WebListener
public final class PersistenceListener implements ServletContextListener {

    @Override
    public void contextInitialized(final ServletContextEvent _) {
        PersistenceFactory.createFactory();
    }

    @Override
    public void contextDestroyed(final ServletContextEvent _) {
        PersistenceFactory.closeFactory();
    }

}
