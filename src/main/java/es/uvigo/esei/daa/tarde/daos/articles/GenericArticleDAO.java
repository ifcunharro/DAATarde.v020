package es.uvigo.esei.daa.tarde.daos.articles;

import static es.uvigo.esei.daa.tarde.daos.DatabaseSession.withTransaction;
import static es.uvigo.esei.daa.tarde.daos.DatabaseSession.withoutTransaction;

import java.util.List;

import javax.persistence.TypedQuery;

import es.uvigo.esei.daa.tarde.daos.DatabaseSession;
import es.uvigo.esei.daa.tarde.daos.GenericDAO;
import es.uvigo.esei.daa.tarde.entities.articles.Article;

public abstract class GenericArticleDAO<T extends Article> extends GenericDAO<T> {

    public long countByName(final String name) {
        try (final DatabaseSession session = withoutTransaction()) {
            return session.manager.createQuery(
                "SELECT COUNT(a.id) FROM " + getEntityName() + " a "
                    + "WHERE UPPER(a.name) LIKE :name "
                    + "AND a.isVerified = true",
                Long.class
            ).setParameter(
                "name", "%" + name.toUpperCase() + "%"
            ).getSingleResult();
        }
    }

    public List<T> findByName(
        final String name, int pageNumber, int articlesPerPage
    ) {
        try (final DatabaseSession session = withoutTransaction()) {
            final TypedQuery<T> query = session.manager.createQuery(
                "SELECT a FROM " + getEntityName() + " a "
                    + "WHERE UPPER(a.name) LIKE :name "
                    + "AND a.isVerified = true "
                    + "ORDER BY a.name ASC",
                getGenericClass()
            );

            return query.setParameter("name", "%" + name.toUpperCase() + "%")
                        .setMaxResults(articlesPerPage)
                        .setFirstResult((pageNumber - 1) * articlesPerPage)
                        .getResultList();
        }
    }

    public List<T> findLatest(final int articlesCount) {
        try (final DatabaseSession session = withoutTransaction()) {
            return session.manager.createQuery(
                "SELECT a FROM " + getEntityName() + " a "
                    + "WHERE a.isVerified = true "
                    + "ORDER BY a.id DESC",
                getGenericClass()
            ).setMaxResults(articlesCount).getResultList();
        }
    }

    public void save(final T article) {
        if (article.isPersisted()) update(article); else insert(article);
    }

    void insert(final T article) {
        try (final DatabaseSession session = withTransaction()) {
            session.manager.persist(article);
        }
    }

    void update(final T article) {
        try (final DatabaseSession session = withTransaction()) {
            session.manager.merge(article);
        }
    }

}
