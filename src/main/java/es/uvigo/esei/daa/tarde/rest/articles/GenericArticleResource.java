package es.uvigo.esei.daa.tarde.rest.articles;

import java.util.List;

import javax.persistence.PersistenceException;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import es.uvigo.esei.daa.tarde.Config;
import es.uvigo.esei.daa.tarde.daos.articles.GenericArticleDAO;
import es.uvigo.esei.daa.tarde.entities.articles.Article;

public abstract class GenericArticleResource<T extends Article> {

    protected final GenericArticleDAO<T> dao;

    protected GenericArticleResource(final GenericArticleDAO<T> dao) {
        this.dao = dao;
    }

    /*
     * Supongase @Path("/articles") en la clase concreta, entonces:
     *
     * - HTTP GET a "/articles" retorna primera pagina del listado
     *   completo de articulos (realiza busqueda vacia).
     *
     * - HTTP GET a "/articles?page=7" retorna la pagina 7 del
     *   listado completo de articulos (reliza busqueda vacia).
     *
     * - HTTP GET a "/articles?search=xxx" retorna primera pagina del
     *   listado de articulos coincidente con busqueda por nombre
     *   "xxx".
     *
     * - HTTP GET a "/articles?search=xxx&page=7" retorna la pagina 7
     *   del listado de articulos coincidentes con busqueda por
     *   nombre "xxx".
     *
     * - HTTP GET a "/articles?count=true" retorna numero absoluto de
     *   articulos existentes (realiza COUNT sobre busqueda vacia).
     *
     * - HTTP GET a "/articles?search=xxx&count=true" retorna numero
     *   de articulos coincidentes con busqueda por nombre "xxx"
     *   (realiza COUNT sobre busqueda por nombre "xxx").
     *
     */
    @GET
    public Response list(
        @QueryParam("search") @DefaultValue("")      final String name,
        @QueryParam("count")  @DefaultValue("false") final boolean count,
        @QueryParam("page")   @DefaultValue("1")     final int pageNumber
    ) {
        try {

            return Response.ok().entity(
                count ? countByName(name) : findByName(name, pageNumber)
            ).build();

        } catch (final IllegalArgumentException iae) {
            iae.printStackTrace();
            return Response.status(Status.BAD_REQUEST).build();
        } catch (final Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    protected GenericEntity<Long> countByName(final String name) {
        return new GenericEntity<Long>(dao.countByName(name)) { };
    }

    @SuppressWarnings("unchecked")
    protected GenericEntity<List<Article>> findByName(
        final String name, final int pageNumber
    ) {
        return new GenericEntity<List<Article>>((List<Article>) dao.findByName(
            name, pageNumber, Config.getInteger("articles_per_page")
        )) { };
    }

    @GET
    @Path("latest")
    @SuppressWarnings("unchecked")
    public Response findLatest() {
        try {

            return Response.ok().entity(new GenericEntity<List<Article>>(
                (List<Article>) dao.findLatest(
                    Config.getInteger("articles_home_page")
                )
            ) { }).build();

        } catch (final Exception _) {
            return Response.serverError().build();
        }
    }

    @POST
    public Response insert(final T article) {
        if (article.isPersisted())
            return Response.status(Status.BAD_REQUEST).build();

        try {

            dao.save(article);
            return Response.ok(article.getId()).build();

        } catch (final PersistenceException pe) {
            pe.printStackTrace();
            return Response.serverError().build();
        }
    }

}
