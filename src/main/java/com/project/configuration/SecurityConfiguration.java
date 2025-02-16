package com.project.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired private UserDetailsService userDetailsService;
    @Autowired private DataSource dataSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(configure -> {
//            configure.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll();
            configure
                // ✅ 공개 접근 허용 경로
                .requestMatchers(
                    "/book/book-category", "/book/book-category/search",
                    "/discussion/category", "/discussion/category/search",
                    "/user/join", "/user/find-id", "/user/login",
                    "/user/reset-pw", "/user/reset-pw-2",
                    "/user/email/auth", "/user/tel/auth",
                    "/complain", "/complain/detail/**", "/complain/add",
                    "/imp_init",
                    "/user/id/**", "/user/findId/**",
                    "/book/**",
                    "/content/**"
                ).permitAll()
                .requestMatchers(
                    "/complain",
                    "/user/join",
                    "/discussion/category",
                    "/discussion/category/search",
                    "/user/complain","/user/find-id", "/user/findId/**", "/user/find-id",
                    "/user/id/**", "/user/info", "/user/info-revise", "/user/login", "/user/pw-auth",
                    "/user/resetPw/", "/user/resetPw/password", "/user/tel/", "/user/tel/auth", "/reset-pw", "/reset-pw-2",
                        "/book/{bookIsbn}", "/discussion/{discussionId}", "/complain/detail/{no}", "/user/id/{userId}", "/user/findId/{email}",
                        "/user/resetPw/password",
                        "/book/{bookIsbn}/review", "/discussion/category/search", "/book/book-category/search", "/discussion/{discussionId}/search",
                        "/discussion/{discussionId}/comment"
                ).permitAll()
                // ✅ 관리자 페이지 보호: "ADMIN" 역할이 있어야만 접근 가능
                .requestMatchers(
                    "/admin/**"
                ).hasRole("ADMIN")
                // ✅ 이메일 인증 경로
                .requestMatchers(
                    "/mail/**",
                    "/user/email/**",
                    "/user/email/auth/**"
                ).permitAll()
                // ✅ 리소스 및 전체 경로
                .requestMatchers(
                    // "/css/**", "/js/**", "/img/**", "/files/**",
                    "/static/**",
                    "/css/**",
                    "/img/**",
                    "/js/**",
                    "/public/**",
                    "/**"
                ).permitAll()
                // ✅ 그 외 모든 요청은 인증 필요
                .anyRequest().authenticated();
        });

        http.userDetailsService(userDetailsService)
                .formLogin(Customizer.withDefaults());

        http.formLogin(configure -> {
            configure.loginPage("/user/login")
                    .loginProcessingUrl("/user/login")
                    .usernameParameter("id")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/")
                    .permitAll();
        });

        http.logout(configure -> {
            configure.logoutUrl("/user/logout")
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .logoutSuccessUrl("/");
        });

        http.oauth2Login(configure -> {
            configure.loginPage("/user/login")
                    .failureUrl("/user/join")
                    .defaultSuccessUrl("/")
                    .permitAll();
        });

        http.rememberMe(configure -> {
            configure.userDetailsService(userDetailsService)
                    .tokenRepository(persistentTokenRepository())
                    .tokenValiditySeconds(60 * 10);
        });

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }
}
