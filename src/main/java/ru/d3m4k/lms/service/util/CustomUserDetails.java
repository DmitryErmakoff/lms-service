package ru.d3m4k.lms.service.util;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
public class CustomUserDetails implements UserDetails {
    private Long id;
    private String username;
    private String firstName;
    private String surname;
    private String middleName;
    private String email;
    private String password;
    private LocalDateTime createdAt;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(
            String email,
            String login,
            String password,
            LocalDateTime createdAt,
            Collection<? extends GrantedAuthority> authorities,
            String firstName,
            String surname,
            String middleName,
            Long id
    ) {
        this.email = email;
        this.username = login;
        this.password = password;
        this.createdAt = createdAt;
        this.authorities = authorities;
        this.firstName = firstName;
        this.surname = surname;
        this.middleName = middleName;
        this.id = id;
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
