package tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;
import tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.io.entity.UserEntity;
import tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.repo.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Repository("auth_user_db")
public class ApplicationAuthUserDaoImpl implements ApplicationAuthUserDao{
    private final UserRepository userRepository;

    @Autowired
    public ApplicationAuthUserDaoImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<ApplicationAuthUser> selectUserByEmail(String emailAddress) {
        Optional<UserEntity> userData = userRepository.getUserEntityByEmailAddress(emailAddress);

        if(userData.isPresent()) {
            Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet<>();
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

            return Optional.of(
                    new ApplicationAuthUser(
                            grantedAuthorities,
                            userData.get().getPassword(),
                            userData.get().getEmailAddress(),
                            true,
                            true,
                            true,
                            true,
                            userData.get().getUserId()
                    )
            );
        }

        return Optional.empty();
    }
}
