package com.example.spring.security.jwtauth.demo.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.base.Strings;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtTokenVerifier
        extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;
    private final JwtSecretKey jwtSecretKey;

    public JwtTokenVerifier(JwtConfig jwtConfig, JwtSecretKey jwtSecretKey) {
        this.jwtConfig = jwtConfig;
        this.jwtSecretKey = jwtSecretKey;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader());

        if (Strings.isNullOrEmpty(authorizationHeader) ||
                !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String accessToken = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "");
            JWTVerifier jwtVerifier = JWT.require(jwtSecretKey.getAlgorithm()).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(accessToken);

            String username = decodedJWT.getSubject();
            List<String> authorities = decodedJWT.getClaim("authorities").asList(String.class);
            Set<SimpleGrantedAuthority> grantedAuthorities = authorities
                    .stream()
                    .map(authority -> new SimpleGrantedAuthority(authority))
                    .collect(Collectors.toSet());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    grantedAuthorities
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (JWTVerificationException e) {
            throw new RuntimeException(e);
        }
    }
}
