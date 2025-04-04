package com.sravani.movieapi.security.oauth2;

import com.sravani.movieapi.security.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;


@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

// Supports OAuth2 login and ensures users are redirected properly.and creates jwt token


    private final TokenProvider tokenProvider;
 //   redirectUri defines the frontend URL where the user should be redirected after login.
    @Value("${app.oauth2.redirectUri}")
    private String redirectUri;
    @Autowired
    public CustomAuthenticationSuccessHandler(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        handle(request, response, authentication);//Calls handle() to process the successful login.
        super.clearAuthenticationAttributes(request);//Clears any leftover authentication attributes from the session.
    }
    //Determines the target redirect URL (uses redirectUri if set).
    //Generates a JWT token using TokenProvider.generate(authentication).
    //Appends the token as a query parameter to the redirect URL.
    //Redirects the user to the frontend application with the token.

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl = redirectUri.isEmpty() ?
                determineTargetUrl(request, response, authentication) : redirectUri;

        String token = tokenProvider.generate(authentication);
        targetUrl = UriComponentsBuilder.fromUriString(targetUrl).queryParam("token", token).build().toUriString();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
