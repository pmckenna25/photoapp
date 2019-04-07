package model;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ImageThumbnail {

    private BufferedImage thumbnail;
    private AmazonS3 s3;
    private final String bucketName;
    private final String key;


    public ImageThumbnail(String image) throws IOException, URISyntaxException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(getProperties()));
        bucketName = properties.getProperty("my.bucket");
        key = properties.getProperty("my.key");
        s3 = AmazonS3ClientBuilder.defaultClient();
        this.thumbnail = retrieveImage(image);
    }

    private File getProperties() throws URISyntaxException {

        Path path = Paths.get(getClass().getClassLoader().getResource("system.properties").toURI());
        return new File(path.toString());
    }

    private BufferedImage retrieveImage(String image) throws IOException {

        S3Object s3Object = s3.getObject(bucketName, key+image);
        return ImageIO.read(s3Object.getObjectContent());
    }

    public BufferedImage getThumbnail() {
        return thumbnail;
    }
}
