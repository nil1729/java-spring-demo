package tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.security.jwt;

import com.auth0.jwt.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.auth.ApplicationAuthUser;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    private final JwtConfig jwtConfig;
    private final JwtAlgorithm jwtAlgorithm;

    @Autowired
    public JwtUtil(JwtConfig jwtConfig, JwtAlgorithm jwtAlgorithm) {
        this.jwtConfig = jwtConfig;
        this.jwtAlgorithm = jwtAlgorithm;
    }

    public String generateAccessToken(Authentication authResult, HttpServletRequest request) {
        ApplicationAuthUser applicationAuthUser = (ApplicationAuthUser) authResult.getPrincipal();

        return JWT
                .create()
                .withSubject(applicationAuthUser.getUserId())
                .withClaim("permissions",
                        authResult
                                .getAuthorities()
                                .stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList())
                )
                .withExpiresAt(new Date(
                        System.currentTimeMillis() + 1000L * 60 * jwtConfig.getTokenExpirationAfterMinutes()
                ))
                .withIssuedAt(new Date())
                .withIssuer(request.getRequestURL().toString())
                .sign(jwtAlgorithm.getAlgorithm());
    }
}
