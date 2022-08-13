package tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {
    private final ApplicationAuthUserDao applicationAuthUserDao;

    @Autowired
    public ApplicationUserDetailsService(
            @Qualifier("auth_user_db") ApplicationAuthUserDao applicationAuthUserDao
    ) {
        this.applicationAuthUserDao = applicationAuthUserDao;
    }


    @Override
    public UserDetails loadUserByUsername(String emailAddress)
            throws UsernameNotFoundException {
        return applicationAuthUserDao
                .selectUserByEmail(emailAddress)
                .orElseThrow(() ->
                    new RuntimeException(
                            String.format("Email address [%s] not registered wth us", emailAddress)
                    )
                );
    }
}
