package com.project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@ToString
public class UserDTO implements UserDetails, OAuth2User {
    @Length(min = 4, max = 15)
    @Pattern(regexp = "^[a-z][0-9a-zA-Z]*$")
    private String id;
    private String name;
    @Pattern(regexp = "^[0-9a-zA-Z~@#$%^&*()_=+.-]{4,10}")
    private String password;
    private String ci;
    @Pattern(regexp = "^(010|011|017|019|018)-[0-9]{3,4}-[0-9]{4}$")
    private String tel;
    @Email
    private String email;
    private LocalDateTime joinDate;
    private int point;
    private LocalDateTime rentalDate;
    private byte[] profileImage;
    private LocalDateTime updatedAt;
    private String role;

    private int bookIsbn;

    @Override
    public String getName() {
        return this.id;
    }
    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("TEMP"));
    }
    @Override
    public String getUsername() {
        return this.id;
    }
}
