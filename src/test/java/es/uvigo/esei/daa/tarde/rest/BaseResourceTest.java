package es.uvigo.esei.daa.tarde.rest;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.OK;

import java.util.HashSet;
import java.util.Set;

import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;

public abstract class BaseResourceTest {

    protected static final int BAD_REQUEST_CODE  = BAD_REQUEST.getStatusCode();
    protected static final int OK_CODE           = OK.getStatusCode();
    protected static final int SERVER_ERROR_CODE = INTERNAL_SERVER_ERROR.getStatusCode();

    protected JerseyTest  jerseyTest;
    private   Set<Object> resources = new HashSet<>();

    protected final void registerResourceUnderTest(final Object obj) {
        resources.add(obj);
    }

    @Before
    public final void setUpJerseyTest( ) throws Exception {
        jerseyTest = new JerseyTestBuilder().addResources(resources).build();
        jerseyTest.setUp();
    }

    @After
    public final void tearDownJerseyTest( ) throws Exception {
        jerseyTest.tearDown();
    }

}
