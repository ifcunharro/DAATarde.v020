package es.uvigo.esei.daa.tarde.rest.articles;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.common.annotations.VisibleForTesting;

import es.uvigo.esei.daa.tarde.daos.articles.ArticleDAO;
import es.uvigo.esei.daa.tarde.entities.articles.Article;

@Path("/articles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ArticleResource  extends GenericArticleResource<Article> {

    public ArticleResource() {
        super(new ArticleDAO());
    }

    @VisibleForTesting
    ArticleResource(final ArticleDAO dao) {
        super(dao);
    }
    
    @Override
    public final Response insert(final Article _) {
        return Response.status(Status.BAD_REQUEST).build();
    }

}
