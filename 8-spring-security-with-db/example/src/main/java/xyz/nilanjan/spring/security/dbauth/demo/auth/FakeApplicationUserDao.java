package xyz.nilanjan.spring.security.dbauth.demo.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import xyz.nilanjan.spring.security.dbauth.demo.security.ApplicationUserRole;

import java.util.List;
import java.util.Optional;

@Repository("fake")
public class FakeApplicationUserDao implements ApplicationUserDao {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public FakeApplicationUserDao(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<ApplicationUser> selectUserByUsername(String username) {
        return getApplicationUsers()
                .stream()
                .filter(applicationUser -> applicationUser.getUsername().equals(username))
                .findFirst();
    }

    private List<ApplicationUser> getApplicationUsers() {
        List<ApplicationUser> applicationUsers = List.of(
                new ApplicationUser(
                        ApplicationUserRole.STUDENT.getGrantedAuthorities(),
                        passwordEncoder.encode("password"),
                        "student",
                        true,
                        true,
                        true,
                        true
                ),
                new ApplicationUser(
                        ApplicationUserRole.ADMIN.getGrantedAuthorities(),
                        passwordEncoder.encode("password"),
                        "admin",
                        true,
                        true,
                        true,
                        true
                ),
                new ApplicationUser(
                        ApplicationUserRole.ADMIN_TRAINEE.getGrantedAuthorities(),
                        passwordEncoder.encode("password"),
                        "admin_trainee",
                        true,
                        true,
                        true,
                        true
                )
        );

        return applicationUsers;
    }
}
