package es.uvigo.esei.daa.tarde.daos.articles;

import es.uvigo.esei.daa.tarde.entities.articles.Article;

public class ArticleDAO extends GenericArticleDAO<Article> {

    @Override
    void insert(final Article _) {
        throw new UnsupportedOperationException(
            "Cannot insert a bare Article, use a subtype and its DAO."
        );
    }

    @Override
    void update(final Article _) {
        throw new UnsupportedOperationException(
            "Cannot update a bare Article, use a subtype and its DAO."
        );
    }

}
