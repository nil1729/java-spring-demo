package tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class ApplicationAuthUser implements UserDetails {
    private final Collection<? extends GrantedAuthority> grantedAuthorities;
    private final String password;
    private final String emailAddress;
    private final Boolean isAccountNonExpired;
    private final Boolean isAccountNonLocked;
    private final Boolean isCredentialsNonExpired;
    private final Boolean isEnabled;
    private final String userId;

    public ApplicationAuthUser(
            Collection<? extends GrantedAuthority> grantedAuthorities,
            String password,
            String emailAddress,
            Boolean isAccountNonExpired,
            Boolean isAccountNonLocked,
            Boolean isCredentialsNonExpired,
            Boolean isEnabled,
            String userId
    ) {
        this.grantedAuthorities = grantedAuthorities;
        this.password = password;
        this.emailAddress = emailAddress;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
        this.userId = userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return emailAddress;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public String getUserId() {
        return userId;
    }
}
