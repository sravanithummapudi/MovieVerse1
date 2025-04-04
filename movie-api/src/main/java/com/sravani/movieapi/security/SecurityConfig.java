package com.sravani.movieapi.security;

import com.sravani.movieapi.security.oauth2.CustomAuthenticationSuccessHandler;
import com.sravani.movieapi.security.oauth2.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class SecurityConfig {

    public SecurityConfig(CustomOAuth2UserService customOauth2UserService, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler, TokenAuthenticationFilter tokenAuthenticationFilter) {
        this.customOauth2UserService = customOauth2UserService;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
        this.tokenAuthenticationFilter = tokenAuthenticationFilter;
    }

    private final CustomOAuth2UserService customOauth2UserService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final TokenAuthenticationFilter tokenAuthenticationFilter;

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    //Defines Role-Based Access Control
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(HttpMethod.GET, "/api/movies", "/api/movies/**").hasAnyAuthority(ADMIN, USER)
                        .requestMatchers(HttpMethod.GET, "/api/users/me").hasAnyAuthority(ADMIN, USER)
                        .requestMatchers("/api/movies", "/api/movies/**").hasAnyAuthority(ADMIN)
                        .requestMatchers("/api/users", "/api/users/**").hasAnyAuthority(ADMIN)
                        //Public routes like /public/**, /auth/**, /oauth2/** are accessible by anyone (permitAll()).
                        .requestMatchers("/public/**", "/auth/**", "/oauth2/**").permitAll()
                        //Swagger endpoints (/swagger-ui/**, /v3/api-docs/**) are accessible by anyone.
                        .requestMatchers("/", "/error", "/csrf", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(oauth2Login -> oauth2Login
                        //Uses CustomOAuth2UserService to load user details.
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(customOauth2UserService))
                        //Uses CustomAuthenticationSuccessHandler to handle OAuth login success.
                        .successHandler(customAuthenticationSuccessHandler))
                .logout(l -> l.logoutSuccessUrl("/").permitAll())
                //TokenAuthenticationFilter is added before UsernamePasswordAuthenticationFilter to handle token-based authentication.
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                //Stateless session (SessionCreationPolicy.STATELESS) ensures every request is authenticated via JWT.
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //If unauthorized access occurs, it returns 401 Unauthorized
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                //Since JWT is stateless, CSRF is disabled
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
//It uses BCrypt by default, but supports multiple encodings ({bcrypt}, {noop}, {pbkdf2}, etc.).
    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";
}
