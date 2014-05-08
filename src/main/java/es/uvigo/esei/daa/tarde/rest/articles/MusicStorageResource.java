package es.uvigo.esei.daa.tarde.rest.articles;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.common.annotations.VisibleForTesting;

import es.uvigo.esei.daa.tarde.daos.articles.MusicStorageDAO;
import es.uvigo.esei.daa.tarde.entities.articles.MusicStorage;

@Path("/articles/music")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MusicStorageResource extends GenericArticleResource<MusicStorage> {

    public MusicStorageResource( ) {
        super(new MusicStorageDAO());
    }

    @VisibleForTesting
    MusicStorageResource(final MusicStorageDAO dao) {
        super(dao);
    }

}
