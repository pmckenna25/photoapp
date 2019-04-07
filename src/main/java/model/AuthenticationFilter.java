package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.internal.util.Base64;

@Provider
public class AuthenticationFilter implements javax.ws.rs.container.ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Basic";


    public void filter(ContainerRequestContext requestContext) {
        Method method = resourceInfo.getResourceMethod();

        if( ! method.isAnnotationPresent(PermitAll.class)) {

            if(method.isAnnotationPresent(DenyAll.class)) {
                requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                        .entity("Access blocked for all users !!").build());
                return;
            }


            final MultivaluedMap<String, String> headers = requestContext.getHeaders();


            final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);


            if(authorization == null || authorization.isEmpty()) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                        .entity("You cannot access this resource").build());
                return;
            }


            final String encodedUserPassword = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");

            String usernameAndPassword = new String(Base64.decode(encodedUserPassword.getBytes()));;

            final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
            final String username = tokenizer.nextToken();
            final String password = tokenizer.nextToken();

            if(method.isAnnotationPresent(RolesAllowed.class)) {
                RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
                Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));


                try {
                    if( ! isUserAllowed(username, password, rolesSet)) {
                        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                                .entity("You cannot access this resource").build());
                        return;
                    }
                } catch (URISyntaxException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private boolean isUserAllowed(final String username, final String password, final Set<String> rolesSet) throws IOException, URISyntaxException {

        Properties properties = new Properties();
        properties.load(new FileInputStream(getProperties()));
        boolean isAllowed = false;

        if(username.equals(properties.getProperty("my.username")) && password.equals(properties.getProperty("my.password")))
        {
            String userRole = "ADMIN";

            if(rolesSet.contains(userRole))
            {
                isAllowed = true;
            }
        }
        return isAllowed;
    }

    private File getProperties() throws URISyntaxException {

        Path path = Paths.get(getClass().getClassLoader().getResource("system.properties").toURI());
        return new File(path.toString());
    }
}
