package com.project.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
            configure.requestMatchers("/static/**").permitAll();
            configure.requestMatchers("/").permitAll();
            configure.requestMatchers("/user/sign-up").permitAll();
            configure.requestMatchers( "/user/id/*", "/user/name/*", "user/findId/**").permitAll();
            configure.requestMatchers("/tel/auth", "/email/auth").permitAll();
            configure.anyRequest().authenticated();
        });

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
                    .logoutSuccessUrl("/")
                    .permitAll();
        });

        http.oauth2Login(configure -> {
            configure.loginPage("/user/login")
                    .failureUrl("/user/sign-up")
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
//
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }
}
