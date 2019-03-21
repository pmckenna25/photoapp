package model;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class ImageThumbnail {

    private Image thumbnail;
    private AmazonS3 s3;
    private final String bucketName = "bvdevphotobucket";


    public ImageThumbnail(String image) throws IOException {
        s3 = AmazonS3ClientBuilder.defaultClient();
        this.thumbnail = retrieveImage(image);
    }

    private Image retrieveImage(String image) throws IOException {

        S3Object s3Object = s3.getObject(bucketName, "images/"+image);
        Image inputImage = ImageIO.read(s3Object.getObjectContent());
        inputImage = inputImage.getScaledInstance(100,100, Image.SCALE_DEFAULT);
        return inputImage;
    }

    public Image getThumbnail() {
        return thumbnail;
    }
}
