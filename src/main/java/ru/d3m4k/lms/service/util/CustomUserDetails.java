package ru.d3m4k.lms.service.util;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
public class CustomUserDetails implements UserDetails {
    private String email;
    private String username;
    private String password;
    private LocalDateTime createdAt;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(String email, String username, String password, LocalDateTime createdAt, Collection<? extends GrantedAuthority> authorities) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.createdAt = createdAt;
        this.authorities = authorities;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
