package es.uvigo.esei.daa.tarde.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

final class JerseyTestBuilder {

    private final Set<Object> resources = new HashSet<>();

    public JerseyTestBuilder addResources(final Set<Object> resources) {
        this.resources.addAll(resources);
        return this;
    }

    public JerseyTest build( ) {
        return new JerseyTest() {
            @Override
            protected void configureClient(final ClientConfig config) {
                super.configureClient(config);

                config.register(JacksonJsonProvider.class);
                config.property(
                    "com.sun.jersey.api.json.POJOMappingFeature",
                    Boolean.TRUE
                );
            }

            @Override
            protected Application configure( ) {
                final ResourceConfig config = new ResourceConfig();

                config.registerInstances(resources);
                config.register(JacksonJsonProvider.class);
                config.property(
                    "com.sun.jersey.api.json.POJOMappingFeature",
                    Boolean.TRUE
                );

                return config;
            }
        };
    }

}
