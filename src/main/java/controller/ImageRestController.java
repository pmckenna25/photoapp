package controller;

import model.ImageThumbnail;
import javax.imageio.ImageIO;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
    public Response getThumbnail(@PathParam("image") String image) throws IOException {

        ImageThumbnail resizeImage = new ImageThumbnail(image);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(convertToBufferedImage(resizeImage.getThumbnail()),"png", outputStream);
        return Response
                .ok(outputStream.toByteArray())
                .build();
    }

    public RenderedImage convertToBufferedImage(Image image) {
        BufferedImage newImage = new BufferedImage(
                image.getWidth(null), image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }
}
