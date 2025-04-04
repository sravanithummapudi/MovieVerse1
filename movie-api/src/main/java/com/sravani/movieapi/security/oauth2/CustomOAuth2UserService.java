package com.sravani.movieapi.security.oauth2;

import com.sravani.movieapi.user.User;
import com.sravani.movieapi.security.CustomUserDetails;
import com.sravani.movieapi.security.SecurityConfig;
import com.sravani.movieapi.user.UserService;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;
    private final List<OAuth2UserInfoExtractor> oAuth2UserInfoExtractors;

    public CustomOAuth2UserService(UserService userService, List<OAuth2UserInfoExtractor> oAuth2UserInfoExtractors) {
        this.userService = userService;
        this.oAuth2UserInfoExtractors = oAuth2UserInfoExtractors;
    }

    //Validates the provider (e.g., GitHub, Google).
    //Extracts user info dynamically.
    //Ensures users exist in the database (upsertUser).
// loads user details from an OAuth2 provider (GitHub, Google, etc.), extracts relevant user information, and ensures the user exists in the system.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Optional<OAuth2UserInfoExtractor> oAuth2UserInfoExtractorOptional = oAuth2UserInfoExtractors.stream()
                .filter(oAuth2UserInfoExtractor -> oAuth2UserInfoExtractor.accepts(userRequest))
                .findFirst();
        if (oAuth2UserInfoExtractorOptional.isEmpty()) {
            throw new InternalAuthenticationServiceException("The OAuth2 provider is not supported yet");
        }

        CustomUserDetails customUserDetails = oAuth2UserInfoExtractorOptional.get().extractUserInfo(oAuth2User);
        User user = upsertUser(customUserDetails);
        customUserDetails.setId(user.getId());
        return customUserDetails;
    }

    // Creates new users for first-time logins.
    //Updates user details for returning users.
    //Ensures consistent user records.

    private User upsertUser(CustomUserDetails customUserDetails) {
        Optional<User> userOptional = userService.getUserByUsername(customUserDetails.getUsername());
        User user;
        if (userOptional.isEmpty()) {
            user = new User();
            user.setUsername(customUserDetails.getUsername());
            user.setName(customUserDetails.getName());
            user.setEmail(customUserDetails.getEmail());
            user.setImageUrl(customUserDetails.getAvatarUrl());
            user.setProvider(customUserDetails.getProvider());
            user.setRole(SecurityConfig.USER);
        } else {
            user = userOptional.get();
            user.setEmail(customUserDetails.getEmail());
            user.setImageUrl(customUserDetails.getAvatarUrl());
        }
        return userService.saveUser(user);
    }
}
