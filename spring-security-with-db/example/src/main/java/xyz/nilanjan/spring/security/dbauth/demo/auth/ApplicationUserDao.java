package xyz.nilanjan.spring.security.dbauth.demo.auth;

import java.util.Optional;

public interface ApplicationUserDao {
    Optional<ApplicationUser> selectUserByUsername(String username);
}
