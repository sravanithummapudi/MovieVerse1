package com.sravani.movieapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class TokenProvider {
   //Helps debug authentication and token-related issues.
    //Logs errors like invalid JWTs, expired tokens, or signature mismatches.
    private static final Logger log = LoggerFactory.getLogger(TokenProvider.class);
    //jwtSecret is a secret key used to sign and verify JSON Web Tokens (JWTs)
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration.minutes}")
    private Long jwtExpirationMinutes;

    public String generate(Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        // Convert jwtSecret (from application.properties) into a byte array.need to conver to binary so that signing algo can work accordingly to it.
        byte[] signingKey = jwtSecret.getBytes();

        Instant now = Instant.now();
          // create jwt using jwt builder()
        return Jwts.builder()
                .header().add("typ", TOKEN_TYPE) // Set header type as "JWT"
                .and()
                .signWith(Keys.hmacShaKeyFor(signingKey), Jwts.SIG.HS512)  // Sign token using HMAC SHA-512
                .issuedAt(Date.from(now)) // Set issue time
                .expiration(Date.from(now.plusSeconds(60 * jwtExpirationMinutes))) // Set expiration time
                .id(UUID.randomUUID().toString()) // Unique token ID
                .issuer(TOKEN_ISSUER)  // Set issuer
                .audience().add(TOKEN_AUDIENCE) // Set audience
                .and()
                .subject(user.getUsername()) // Set subject (username)
                .claim("rol", roles) // Store user roles
                .claim("name", user.getName()) // Store user fullname
                .claim("preferred_username", user.getUsername())
                .claim("email", user.getEmail()) // Store email
                .compact(); // Generate token as a compact string
    }




    public Optional<Jws<Claims>> validateTokenAndGetJws(String token) {
        try {
            byte[] signingKey = jwtSecret.getBytes();

            //Parse the token and validate it:

           // Ensures signature is correct.

           // Extracts user claims (username, roles, email, etc.).

            Jws<Claims> jws = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(signingKey)) // Validate token signature
                    .build()
                    .parseSignedClaims(token);// // Parse token and extract claims

            return Optional.of(jws);
            //Token has expired.
        } catch (ExpiredJwtException exception) {
            log.error("Request to parse expired JWT : {} failed : {}", token, exception.getMessage());
            //Invalid token format.
        } catch (UnsupportedJwtException exception) {
            log.error("Request to parse unsupported JWT : {} failed : {}", token, exception.getMessage());
            // Incorrect structure.
        } catch (MalformedJwtException exception) {
            log.error("Request to parse invalid JWT : {} failed : {}", token, exception.getMessage());
            // Invalid signature.
        } catch (SignatureException exception) {
            log.error("Request to parse JWT with invalid signature : {} failed : {}", token, exception.getMessage());
            // Token is empty.
        } catch (IllegalArgumentException exception) {
            log.error("Request to parse empty or null JWT : {} failed : {}", token, exception.getMessage());
        }
        return Optional.empty();
    }

    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "order-api";
    public static final String TOKEN_AUDIENCE = "order-app";
}
