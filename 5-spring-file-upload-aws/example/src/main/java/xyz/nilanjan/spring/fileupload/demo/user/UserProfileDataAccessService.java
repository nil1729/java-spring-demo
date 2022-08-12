package xyz.nilanjan.spring.fileupload.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import xyz.nilanjan.spring.fileupload.demo.datastore.FakeUserProfileDataStore;

import java.util.List;

@Repository
public class UserProfileDataAccessService {
    private final FakeUserProfileDataStore fakeUserProfileDataStore;

    @Autowired
    public UserProfileDataAccessService(FakeUserProfileDataStore fakeUserProfileDataStore) {
        this.fakeUserProfileDataStore = fakeUserProfileDataStore;
    }

    public List<UserProfile> getUserProfile() {
        return fakeUserProfileDataStore.getUserProfiles();
    }
}
