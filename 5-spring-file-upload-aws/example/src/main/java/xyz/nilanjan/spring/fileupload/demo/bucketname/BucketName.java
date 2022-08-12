package xyz.nilanjan.spring.fileupload.demo.bucketname;

public enum BucketName {
    PROFILE_IMAGE_BUCKET("aws-spring-example");

    private final String bucketName;

    BucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
