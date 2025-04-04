package com.sravani.movieapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    private final UserDetailsService userDetailsService;

    public TokenAuthenticationFilter(UserDetailsService userDetailsService, TokenProvider tokenProvider) {
        this.userDetailsService = userDetailsService;
        this.tokenProvider = tokenProvider;
    }

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            //Extracts the JWT token from the Authorization header.
            getJwtFromRequest(request)
                    .flatMap(tokenProvider::validateTokenAndGetJws)//Uses tokenProvider to validate the JWT and extract the payload (Jws<Claims> object).
                    .ifPresent(jws -> {
                        //Validates the token and extracts the username.
                        String username = jws.getPayload().getSubject();//Gets the username from the JWT payload.
                        //Loads the UserDetails from the database.
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        //Creates an authentication object with user details and authorities.
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        //Sets authentication in the SecurityContextHolder.
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    });
        } catch (Exception e) {
            log.error("Cannot set user authentication", e);
        }
        chain.doFilter(request, response);//Passes the request to the next filter.
    }
//Extracts the token from the Authorization header (Bearer <TOKEN>).
    private Optional<String> getJwtFromRequest(HttpServletRequest request) {
        //Retrieves the Authorization header from the HTTP request.
        String tokenHeader = request.getHeader(TOKEN_HEADER);
        if (StringUtils.hasText(tokenHeader) && tokenHeader.startsWith(TOKEN_PREFIX)) {
            //Ensures the header is not empty and starts with "Bearer ".
            return Optional.of(tokenHeader.replace(TOKEN_PREFIX, ""));
            //Removes the "Bearer " prefix and returns the JWT token.
        }
        return Optional.empty();
        //Returns an empty Optional if the header is missing or invalid.
    }

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
}
