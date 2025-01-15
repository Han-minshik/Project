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
    @NotBlank
    @Pattern(regexp = "^[0-9a-zA-Z~@#$%^&*()_=+.-]{4,10}")
    private String password;
    private String ci;
    @Pattern(regexp = "^(010|011|017|019|018)-[0-9]{3,4}-[0-9]{4}$")
    private String tel;
    @Email
    private String email;
    private LocalDateTime joinDate;
    private Integer point;
    private LocalDateTime rentalDate;
    private byte[] profileImage;
    private LocalDateTime updatedAt;
    private String role;

    private String bookIsbn;
    private String nickname;


    private List<SnsInfoDTO> snsInfo; // 이 유저가 로그인 할 때 사용한 SNS 데이터

    private List<SnsInfoDTO> snsInfo;

    public void setTel(String tel) {
        this.tel = tel.replace(",", "-");
    }

    public void setEmail(String email) {
        this.email = email.replace(",", "@");
    }

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
    // SecurityConfig 작성할 때 oauth2 작성했을 경우 삭제 (override getPassword)
    @Override
    public String getPassword() {
        return this.password;
    }
}
