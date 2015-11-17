package rest.lifestyle;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("rest")
public class MyApplicationConfig extends ResourceConfig {
    public MyApplicationConfig () {
        packages("rest.lifestyle");
    }
}