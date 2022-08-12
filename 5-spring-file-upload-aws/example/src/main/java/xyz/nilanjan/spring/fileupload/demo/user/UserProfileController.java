package xyz.nilanjan.spring.fileupload.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user-profile")
public class UserProfileController {
    private final UserProfileService userProfileService;

    @Autowired
    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping
    public List<UserProfile> getUserProfiles() {
        return userProfileService.getUserProfiles();
    }

    @PostMapping(
            path = "{userProfileId}/image/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> uploadProfileImage(
            @PathVariable("userProfileId") UUID userProfileId,
            @RequestParam("user-profile-image")MultipartFile uploadedFile
    ) {
        userProfileService.uploadProfileImage(userProfileId, uploadedFile);
        return ResponseEntity.ok().body("File uploaded");
    }

    @GetMapping("{userProfileId}/image/download")
    public byte[] downloadProfileImage(@PathVariable("userProfileId") UUID userProfileId) {
        return userProfileService.downloadProfileImage(userProfileId);
    }
}
