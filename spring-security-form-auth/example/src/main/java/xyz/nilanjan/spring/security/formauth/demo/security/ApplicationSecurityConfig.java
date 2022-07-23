package xyz.nilanjan.spring.security.formauth.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig
        extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/auth/login/**").permitAll()
                .antMatchers("/", "index", "/css/**", "/js/**").permitAll()
                .antMatchers("/api/v1/students/**").hasRole(ApplicationUserRole.STUDENT.name())
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/auth/login")
                    .defaultSuccessUrl("/secret", true)
                    .usernameParameter("my-username")
                    .passwordParameter("my-password")
                .and()
                .rememberMe()
                    .rememberMeParameter("my-remember-me")
                    .tokenValiditySeconds((int)TimeUnit.DAYS.toSeconds(45))
                .and()
                .logout()
                    .logoutUrl("/auth/logout")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "GET"))
                    .clearAuthentication(true)
                    .deleteCookies("JSESSIONID", "remember-me")
                    .invalidateHttpSession(true)
                    .logoutSuccessUrl("/");
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails nilUser = User
                .builder()
                .username("student")
                .password(passwordEncoder.encode("password"))
                .authorities(ApplicationUserRole.STUDENT.getGrantedAuthorities())
                .build();

        UserDetails adminUser = User
                .builder()
                .username("admin")
                .password(passwordEncoder.encode("password"))
                .authorities(ApplicationUserRole.STUDENT.getGrantedAuthorities())
                .build();

        UserDetails adminTraineeUser = User
                .builder()
                .username("admin_trainee")
                .password(passwordEncoder.encode("password"))
                .authorities(ApplicationUserRole.STUDENT.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(
                nilUser,
                adminTraineeUser,
                adminUser
        );
    }
}
