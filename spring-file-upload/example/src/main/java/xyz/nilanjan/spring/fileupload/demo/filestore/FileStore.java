package xyz.nilanjan.spring.fileupload.demo.filestore;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Service
public class FileStore {
    private final AmazonS3 s3;

    @Autowired
    public FileStore(AmazonS3 s3) {
        this.s3 = s3;
    }

    public void saveFile(
            String path,
            String filename,
            InputStream fileInputStream,
            Optional<Map<String, String>> optionalMetadata
    ) {
        ObjectMetadata objectMetadata = new ObjectMetadata();

        optionalMetadata.ifPresent(map -> {
            if (!map.isEmpty()) {
                map.forEach(objectMetadata::addUserMetadata);
            }
        });

        try {
            s3.putObject(
                    path,
                    filename,
                    fileInputStream,
                    objectMetadata
            );
        } catch (SdkClientException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeFile(String path, String filename) {
        try {
            s3.deleteObject(path, filename);
        } catch (SdkClientException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] downloadFile(String path, String filename) {
        try {
            S3Object s3Object =  s3.getObject(path, filename);
            S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
            return IOUtils.toByteArray(s3ObjectInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
