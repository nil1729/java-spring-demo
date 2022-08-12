package xyz.nilanjan.spring.fileupload.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.nilanjan.spring.fileupload.demo.filestore.FileStore;

import java.io.IOException;
import java.util.*;

import static org.apache.http.entity.ContentType.*;
import static xyz.nilanjan.spring.fileupload.demo.bucketname.BucketName.PROFILE_IMAGE_BUCKET;

@Service
public class UserProfileService {
    private final UserProfileDataAccessService userProfileDataAccessService;
    private final FileStore fileStore;

    @Autowired
    public UserProfileService(
            UserProfileDataAccessService userProfileDataAccessService,
            FileStore fileStore
    ) {
        this.userProfileDataAccessService = userProfileDataAccessService;
        this.fileStore = fileStore;
    }

    public List<UserProfile> getUserProfiles() {
        return userProfileDataAccessService.getUserProfile();
    }

    public void uploadProfileImage(UUID userProfileId, MultipartFile uploadedFile) {
        // 1. Check if image is not empty
        emptyFileCheck(uploadedFile);

        // 2. If file is an image
        imageFileCheck(uploadedFile);

        // 3. Check user exists on our database
        UserProfile user = getUserProfileById(userProfileId);

        // 4. Grab some metadata from file if any
        Map<String, String> metadata = extractMetadata(uploadedFile);

        // 5. Store the image in s3 and Update Database
        String path = String.format("%s/%s", PROFILE_IMAGE_BUCKET.getBucketName(), user.getUserProfileId());
        String filename = String.format("%s-%s", uploadedFile.getOriginalFilename(), UUID.randomUUID());
        try {
            fileStore.saveFile(path, filename, uploadedFile.getInputStream(), Optional.of(metadata));
            if(user.getUserProfileImageLink().isPresent()) {
                fileStore.removeFile(path, user.getUserProfileImageLink().get());
            }
            user.setUserProfileImageLink(filename);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] downloadProfileImage(UUID userProfileId) {
        UserProfile user = getUserProfileById(userProfileId);
        String path = String.format("%s/%s", PROFILE_IMAGE_BUCKET.getBucketName(), user.getUserProfileId());

        return user
                .getUserProfileImageLink()
                .map(key-> fileStore.downloadFile(path, key))
                .orElse(new byte[0]);
    }

    private void imageFileCheck(MultipartFile file) {
        if(!Arrays.asList(
                IMAGE_PNG.getMimeType(),
                IMAGE_JPEG.getMimeType(),
                IMAGE_WEBP.getMimeType()
        ).contains(file.getContentType())){
            throw new IllegalStateException(String.format("File must be an image [%s]", file.getContentType()));
        }
    }

    private void emptyFileCheck(MultipartFile file) {
        if(file.isEmpty()) {
            throw new IllegalStateException(String.format("Cannot upload empty file [%s]", file.getSize()));
        }
    }

    private UserProfile getUserProfileById(UUID userProfileId) {
        return this.getUserProfiles()
                .stream()
                .filter(userprofile -> userprofile.getUserProfileId().equals(userProfileId))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(String.format("User profile with id %s not found", userProfileId))
                );
    }

    private Map<String, String> extractMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        return metadata;
    }
}
