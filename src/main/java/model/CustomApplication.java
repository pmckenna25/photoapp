package model;

import org.glassfish.jersey.server.ResourceConfig;

public class CustomApplication extends ResourceConfig {

    public CustomApplication() {
        register(AuthenticationFilter.class);
    }
}

