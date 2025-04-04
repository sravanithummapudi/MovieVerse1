package com.sravani.movieapi.security;

import com.sravani.movieapi.security.oauth2.OAuth2Provider;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Data
//UserDetails → Used for JWT-based authentication (username & password).
//OAuth2User → Used for OAuth2 authentication (Google, Facebook, etc.).
public class CustomUserDetails implements OAuth2User, UserDetails {

    private Long id;

    private String username;
    private String password;
    private String name;
    private String email;
    private String avatarUrl;
    // Stores profile picture (useful for OAuth2 users).
    private OAuth2Provider provider;
    // Stores which OAuth provider (Google, GitHub, etc.).
    private Collection<? extends GrantedAuthority> authorities;
    //Holds user roles/permissions (e.g., ROLE_ADMIN, ROLE_USER).
    private Map<String, Object> attributes;
    //Stores OAuth2 user details (retrieved from providers like Google)

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public OAuth2Provider getProvider() {
        return provider;
    }

    public void setProvider(OAuth2Provider provider) {
        this.provider = provider;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }
    //Required for OAuth2 authentication.
    //Stores OAuth provider-specific user details.

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
