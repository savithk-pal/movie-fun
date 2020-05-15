package org.superbiz.moviefun.blobstore;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;

import java.util.Optional;

public class S3Store implements BlobStore {

    private String photoBucket;
    private AmazonS3Client amazonS3Client;

    public S3Store(AmazonS3Client amazonS3Client, String photoBucket) {
        this.photoBucket = photoBucket;
        this.amazonS3Client = amazonS3Client;
    }

    @Override
    public void put(Blob blob) throws Exception {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(blob.contentType);
        amazonS3Client.putObject(this.photoBucket, blob.name, blob.inputStream, objectMetadata);

    }

    @Override
    public Optional<Blob> get(String name) throws Exception {
        try {
            S3Object s3Object = amazonS3Client.getObject(this.photoBucket, name);
            if(s3Object != null) {
                ObjectMetadata objectMetadata = s3Object.getObjectMetadata();
                Blob found = new Blob(name, s3Object.getObjectContent(), objectMetadata.getContentType());
                return Optional.of(found);
            }
            return Optional.empty();
        } catch (AmazonS3Exception amazonS3Exception) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteAll() {

    }
}
