package model;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import sun.awt.image.BufferedImageDevice;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageThumbnail {

    private BufferedImage thumbnail;
    private AmazonS3 s3;
    private final String bucketName = "bvdevthumbnailbucket";


    public ImageThumbnail(String image) throws IOException {
        s3 = AmazonS3ClientBuilder.defaultClient();
        this.thumbnail = retrieveImage(image);
    }

    private BufferedImage retrieveImage(String image) throws IOException {

        S3Object s3Object = s3.getObject(bucketName, "images/"+image);
        BufferedImage inputImage = ImageIO.read(s3Object.getObjectContent());
        return inputImage;
    }

    public BufferedImage getThumbnail() {
        return thumbnail;
    }
}
