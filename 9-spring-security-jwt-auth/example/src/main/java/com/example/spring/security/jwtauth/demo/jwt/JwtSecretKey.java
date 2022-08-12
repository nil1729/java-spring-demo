package com.example.spring.security.jwtauth.demo.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtSecretKey {
    private final JwtConfig jwtConfig;

    public JwtSecretKey(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public Algorithm getAlgorithm() {
        return Algorithm.HMAC256(jwtConfig.getSecretKey().getBytes());
    }

}
