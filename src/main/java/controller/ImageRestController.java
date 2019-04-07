package controller;

import model.ImageThumbnail;
import javax.imageio.ImageIO;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

@Path("image")
public class ImageRestController {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getMessage(){

        return "Johnny 5 is Alive!\n";
    }

    @GET
    @Path("/thumbnail/{image}")
    @Produces({"image/png", "image/jpg"})
    public Response getThumbnail(@PathParam("image") String image) throws IOException, URISyntaxException {

        ImageThumbnail resizeImage = new ImageThumbnail(image);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(resizeImage.getThumbnail(),"png", outputStream);
        return Response
                .ok(outputStream.toByteArray())
                .build();
    }
}
