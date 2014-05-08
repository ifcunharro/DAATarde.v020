package es.uvigo.esei.daa.tarde.rest.articles;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.common.annotations.VisibleForTesting;

import es.uvigo.esei.daa.tarde.daos.articles.BookDAO;
import es.uvigo.esei.daa.tarde.entities.articles.Book;

@Path("/articles/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource extends GenericArticleResource<Book> {

    public BookResource( ) {
        super(new BookDAO());
    }

    @VisibleForTesting
    BookResource(final BookDAO dao) {
        super(dao);
    }

}
