package app;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Launcher {

    public static void main(String[] args){

        Server server = new Server(9015);

        ServletContextHandler ctx = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);

        ctx.setContextPath("/");
        server.setHandler(ctx);

        ServletHolder serHol = ctx.addServlet(ServletContainer.class, "/*");
        serHol.setInitParameter("jersey.config.server.provider.packages","controller");
        serHol.setInitParameter("javax.ws.rs.Application","model.CustomApplication");

        try{

            server.start();
            server.join();
        }catch(Exception ex){
            Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
        }finally{

            server.destroy();
        }
    }
}

