package xyz.nilanjan.spring.security.basicauth.demo.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static xyz.nilanjan.spring.security.basicauth.demo.security.ApplicationUserPermission.STUDENT_READ;
import static xyz.nilanjan.spring.security.basicauth.demo.security.ApplicationUserPermission.STUDENT_WRITE;

public enum ApplicationUserRole {
    STUDENT(
            Sets.newHashSet()
    ),
    ADMIN(
            Sets.newHashSet(
                    STUDENT_READ,
                    STUDENT_WRITE
            )
    ),
    ADMIN_TRAINEE(
            Sets.newHashSet(
                    STUDENT_READ
            )
    );

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return this.permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions =
                this.getPermissions()
                        .stream()
                        .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                        .collect(Collectors.toSet());

        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
