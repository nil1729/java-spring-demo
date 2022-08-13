package tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.auth;

import java.util.Optional;

public interface ApplicationAuthUserDao {
    Optional<ApplicationAuthUser> selectUserByEmail(String emailAddress);
}
