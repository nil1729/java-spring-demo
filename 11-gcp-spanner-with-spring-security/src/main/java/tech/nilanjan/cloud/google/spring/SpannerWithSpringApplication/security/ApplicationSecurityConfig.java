package tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.auth.ApplicationUserDetailsService;
import tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.security.jwt.JwtAlgorithm;
import tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.security.jwt.JwtConfig;
import tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.security.jwt.JwtTokenVerifier;
import tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.service.UserService;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig
        extends WebSecurityConfigurerAdapter {

    private final ApplicationUserDetailsService applicationUserDetailsService;
    private final JwtConfig jwtConfig;
    private final JwtAlgorithm jwtAlgorithm;
    private final UserService userService;

    public ApplicationSecurityConfig(
            ApplicationUserDetailsService applicationUserDetailsService,
            JwtConfig jwtConfig,
            JwtAlgorithm jwtAlgorithm,
            UserService userService
    ) {
        this.applicationUserDetailsService = applicationUserDetailsService;
        this.jwtConfig = jwtConfig;
        this.jwtAlgorithm = jwtAlgorithm;
        this.userService = userService;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .httpBasic().disable()
                .cors()
                .and()
                .authorizeHttpRequests()
                .antMatchers(HttpMethod.POST, "/auth/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .userDetailsService(applicationUserDetailsService)
                .exceptionHandling()
                .accessDeniedHandler(
                        (request, response, accessDeniedException) -> {
                            Map<String, String> result = new HashMap<>();
                            result.put("message", "You are unauthorized to access the resource");

                            response.setStatus(401);
                            response.setHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                            new ObjectMapper().writeValue(response.getOutputStream(), result);
                        }
                )
                .authenticationEntryPoint(
                        (request, response, authException) -> {
                            Map<String, String> result = new HashMap<>();
                            result.put("message", "Authentication failed due to bad credentials");

                            response.setStatus(400);
                            response.setHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                            new ObjectMapper().writeValue(response.getOutputStream(), result);
                        }
                )
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(new JwtTokenVerifier(jwtConfig, jwtAlgorithm, userService), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
