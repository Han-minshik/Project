package com.project.configuration;

import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
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
            configure.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll();

            // ✅ 로그인, 로그아웃 관련 경로를 최상단에 배치
            configure.requestMatchers("/user/login", "/user/login/**", "/user/logout", "/oauth2/**", "/login/oauth2/**", "/user/login?**", "/user/login?error=true").permitAll();

            // ✅ 정적 리소스 허용
            configure.requestMatchers("/static/**", "/img/**", "/css/**", "/js/**", "/", "/main/home", "/book/**", "/content/**").permitAll();

            // ✅ 관리자 페이지 보호
            configure.requestMatchers("/admin/**").hasRole("ADMIN");

            // ✅ 메일 및 기타 공개 접근 가능 경로
            configure.requestMatchers("/mail/**", "/user/email/**", "/user/email/auth/**").permitAll();
            configure.requestMatchers("/complain", "/user/join", "/discussion/category", "/discussion/category/search",
                    "/user/complain", "/user/find-id", "/user/findId/**", "/user/find-id",
                    "/user/id/**", "/user/info", "/user/info-revise", "/user/pw-auth",
                    "/user/resetPw/", "/user/resetPw/password", "/user/tel/", "/user/tel/auth", "/reset-pw", "/reset-pw-2",
                    "/layout/**", "/public/**", "/main/home").permitAll();

            // ✅ 그 외 모든 요청은 인증 필요
            configure.anyRequest().authenticated();
        });

        // ✅ HTTP 기본 인증 비활성화 및 CSRF, CORS 설정
        http.httpBasic(AbstractHttpConfigurer::disable)
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configure(http))
                .csrf(AbstractHttpConfigurer::disable);

        // ✅ 세션 유지 설정 (필요 시 `ALWAYS`로 변경하여 세션 유지 테스트 가능)
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  // 기본값 유지
        );

        // ✅ 일반 로그인 설정
        http.formLogin(configure -> {
            configure.loginPage("/user/login")
                    .permitAll()
                    .loginProcessingUrl("/user/login")
                    .usernameParameter("id")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/", false)  // 🔹 로그인 후 원래 요청한 페이지로 이동
                    .failureUrl("/user/login?error=true");
        });

        // ✅ OAuth2 로그인 설정
        http.oauth2Login(configure -> {
            configure.loginPage("/user/login")
                    .permitAll()
                    .defaultSuccessUrl("/", false)  // 🔹 로그인 후 원래 요청한 페이지로 이동
                    .failureUrl("/user/login?error=true");
        });

        // ✅ 로그아웃 설정
        http.logout(configure -> {
            configure.logoutUrl("/user/logout")
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .logoutSuccessUrl("/");
        });

        // ✅ Remember Me 설정
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
