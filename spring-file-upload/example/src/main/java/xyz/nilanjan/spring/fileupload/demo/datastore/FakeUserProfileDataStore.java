package xyz.nilanjan.spring.fileupload.demo.datastore;

import org.springframework.stereotype.Repository;
import xyz.nilanjan.spring.fileupload.demo.user.UserProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class FakeUserProfileDataStore {
    private static final List<UserProfile> USER_PROFILES = new ArrayList<>();

    static {
        USER_PROFILES.add(
             new UserProfile(
                     UUID.randomUUID(),
                     "Nilanjan Deb",
                     null
             )
        );
        USER_PROFILES.add(
                new UserProfile(
                        UUID.randomUUID(),
                        "John Doe",
                        null
                )
        );
    }

    public List<UserProfile> getUserProfiles() {
        return USER_PROFILES;
    }
}
