package es.uvigo.esei.daa.tarde.rest.articles;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.common.annotations.VisibleForTesting;

import es.uvigo.esei.daa.tarde.daos.articles.ComicDAO;
import es.uvigo.esei.daa.tarde.entities.articles.Comic;

@Path("/articles/comics")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ComicResource extends GenericArticleResource<Comic> {

    public ComicResource( ) {
        super(new ComicDAO());
    }

    @VisibleForTesting
    ComicResource(final ComicDAO dao) {
        super(dao);
    }

}
