package es.uvigo.esei.daa.tarde.rest.articles;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.common.annotations.VisibleForTesting;

import es.uvigo.esei.daa.tarde.daos.articles.MovieDAO;
import es.uvigo.esei.daa.tarde.entities.articles.Movie;

@Path("/articles/movies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieResource extends GenericArticleResource<Movie> {

    public MovieResource( ) {
        super(new MovieDAO());
    }

    @VisibleForTesting
    MovieResource(final MovieDAO dao) {
        super(dao);
    }

}
