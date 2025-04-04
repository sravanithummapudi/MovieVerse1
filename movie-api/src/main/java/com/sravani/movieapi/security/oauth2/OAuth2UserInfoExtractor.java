package com.sravani.movieapi.security.oauth2;

import com.sravani.movieapi.security.CustomUserDetails;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2UserInfoExtractor {
    //extracting user information from an OAuth2 provider (Google, GitHub, etc.) and converting it into a CustomUserDetails object.

    CustomUserDetails extractUserInfo(OAuth2User oAuth2User);
//Checks whether this extractor can handle the given OAuth2UserRequest.
    boolean accepts(OAuth2UserRequest userRequest);
}
