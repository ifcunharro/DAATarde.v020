package es.uvigo.esei.daa.tarde.rest;

import static org.assertj.core.api.Assertions.assertThat;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.junit.Test;

import es.uvigo.esei.daa.tarde.Config;

public class UtilResourceTest extends BaseResourceTest {

    public UtilResourceTest( ) {
        registerResourceUnderTest(new UtilResource());
    }

    @Test
    public void util_resource_returns_configured_articles_per_page( ) {
        final Response response = jerseyTest.target(
            "util/articles_per_page"
        ).request().get();

        assertThat(response.getStatus()).isEqualTo(OK_CODE);
        assertThat(response.readEntity(
            new GenericType<Integer>() { }
        )).isEqualTo(Config.getInteger("articles_per_page"));
    }

}
