package com.example.spring.security.jwtauth.demo.security;

import com.example.spring.security.jwtauth.demo.jwt.JwtConfig;
import com.example.spring.security.jwtauth.demo.jwt.JwtSecretKey;
import com.example.spring.security.jwtauth.demo.jwt.JwtTokenVerifier;
import com.example.spring.security.jwtauth.demo.jwt.JwtUsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig
        extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;
    private final JwtSecretKey jwtSecretKey;

    @Autowired
    public ApplicationSecurityConfig(
            PasswordEncoder passwordEncoder,
            JwtConfig jwtConfig,
            JwtSecretKey jwtSecretKey
    ) {
        this.passwordEncoder = passwordEncoder;
        this.jwtConfig = jwtConfig;
        this.jwtSecretKey = jwtSecretKey;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtUsernamePasswordAuthenticationFilter(authenticationManager(), jwtConfig, jwtSecretKey))
                .addFilterAfter(new JwtTokenVerifier(jwtConfig, jwtSecretKey), JwtUsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/", "index", "/css/**", "/js/**").permitAll()
                .antMatchers("/api/v1/students/**").hasRole(ApplicationUserRole.STUDENT.name())
                .anyRequest()
                .authenticated();

    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails studentUser = User
                .builder()
                .username("student")
                .password(passwordEncoder.encode("password"))
                .authorities(ApplicationUserRole.STUDENT.getGrantedAuthorities())
                .build();

        UserDetails adminUser = User
                .builder()
                .username("admin")
                .password(passwordEncoder.encode("password"))
                .authorities(ApplicationUserRole.ADMIN.getGrantedAuthorities())
                .build();

        UserDetails adminTraineeUser = User
                .builder()
                .username("admin_trainee")
                .password(passwordEncoder.encode("password"))
                .authorities(ApplicationUserRole.ADMIN_TRAINEE.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(
                studentUser,
                adminUser,
                adminTraineeUser
        );
    }
}
