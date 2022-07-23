package com.example.spring.security.jwtauth.demo.jwt;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class JwtUsernamePasswordAuthenticationFilter
        extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final JwtSecretKey jwtSecretKey;

    public JwtUsernamePasswordAuthenticationFilter(
            AuthenticationManager authenticationManager,
            JwtConfig jwtConfig,
            JwtSecretKey jwtSecretKey
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
        this.jwtSecretKey = jwtSecretKey;
    }


    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException {
        try {
            JwtUsernamePasswordRequest authenticationRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), JwtUsernamePasswordRequest.class);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()
            );

            Authentication authenticate = authenticationManager.authenticate(authentication);

            return authenticate;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
    ) throws IOException, ServletException {
        String access_token = JWT
                .create()
                .withSubject(authResult.getName())
                .withClaim("authorities",
                        authResult
                                .getAuthorities()
                                .stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList())
                ).withExpiresAt(new Date(System.currentTimeMillis() + 1000L * 60 * jwtConfig.getTokenExpirationAfterMinutes()))
                .withIssuedAt(new Date())
                .withIssuer(request.getRequestURL().toString())
                .sign(jwtSecretKey.getAlgorithm());

        Map<String, String> result = new HashMap<>();
        result.put("access_token", access_token);

        response.setHeader("Content-Type", "application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), result);
    }
}
