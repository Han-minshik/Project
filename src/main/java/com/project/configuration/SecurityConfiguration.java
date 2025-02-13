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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired private UserDetailsService userDetailsService;
    @Autowired private DataSource dataSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(configure -> {
            // ✅ 관리자 페이지 보호: "ADMIN" 역할 필요
            configure.requestMatchers("/admin/**").hasRole("ADMIN");

            // ✅ 공개 접근 허용 경로
            configure.requestMatchers(
                    "/static/**", "/", "/book/**", "/content/**",
                    "/mail/**", "/user/email/**", "/user/email/auth/**",
                    "/complain", "/user/join", "/discussion/category", "/discussion/category/search",
                    "/user/complain", "/user/find-id", "/user/findId/**", "/user/find-id",
                    "/user/id/**", "/user/info", "/user/info-revise", "/user/login", "/user/pw-auth",
                    "/user/resetPw/", "/user/resetPw/password", "/user/tel/", "/user/tel/auth",
                    "/reset-pw", "/reset-pw-2"
            ).permitAll();

            // ✅ 나머지 모든 요청은 인증 필요
            configure.anyRequest().authenticated();
        });

        // 🔹 HTTPS 강제 적용 (Spring Security 6 이상)
        http.requiresChannel(channel ->
                channel.anyRequest().requiresSecure()
        );

        // 🔹 CORS 설정 허용
        http.cors(Customizer.withDefaults());

        // 🔹 CSRF 보호 비활성화 (API 호출 시 필요)
        http.csrf(configure ->
                configure.ignoringRequestMatchers(
                        new AntPathRequestMatcher("/api/**"),
                        new AntPathRequestMatcher("/user/email/**"),
                        new AntPathRequestMatcher("/user/tel/**")
                )
        );

        // 🔹 일반 로그인 설정
        http.userDetailsService(userDetailsService)
                .formLogin(Customizer.withDefaults());

        http.formLogin(configure -> {
            configure.loginPage("/user/login")
                    .loginProcessingUrl("/user/login")
                    .usernameParameter("id")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/", true)
                    .permitAll();
        });

        // 🔹 로그아웃 설정
        http.logout(configure -> {
            configure.logoutUrl("/user/logout")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .logoutSuccessUrl("/");
        });

        // 🔹 OAuth2 로그인 설정 (이전 페이지로 리디렉션)
        http.oauth2Login(configure -> {
            configure.loginPage("/user/login")
                    .failureUrl("/user/join")
                    .defaultSuccessUrl("/", true)
                    .permitAll();
        });

        // 🔹 Remember-Me 기능 설정 (자동 로그인)
        http.rememberMe(configure -> {
            configure.userDetailsService(userDetailsService)
                    .tokenRepository(persistentTokenRepository())
                    .tokenValiditySeconds(60 * 60 * 24 * 7); // 7일 유지
        });

        return http.build();
    }

    // 🔹 패스워드 인코더 설정
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 🔹 Remember-Me 기능을 위한 토큰 저장소 설정
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }
}
