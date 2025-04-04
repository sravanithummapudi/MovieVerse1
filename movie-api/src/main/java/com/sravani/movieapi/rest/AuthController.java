package com.sravani.movieapi.rest;

import com.sravani.movieapi.user.DuplicatedUserInfoException;
import com.sravani.movieapi.user.User;
import com.sravani.movieapi.rest.dto.AuthResponse;
import com.sravani.movieapi.rest.dto.LoginRequest;
import com.sravani.movieapi.rest.dto.SignUpRequest;
import com.sravani.movieapi.security.SecurityConfig;
import com.sravani.movieapi.security.TokenProvider;
import com.sravani.movieapi.security.oauth2.OAuth2Provider;
import com.sravani.movieapi.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
//It organizes authentication-related APIs under the /auth path.
@RequestMapping("/auth")
public class AuthController {



    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenProvider tokenProvider) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }
      //Receives a LoginRequest (JSON with username & password).
    //Authenticates the user,Generates a JWT token using authenticateAndGetToken().
      //Returns the token inside an AuthResponse.
    @PostMapping("/authenticate")
    public AuthResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        String token = authenticateAndGetToken(loginRequest.username(), loginRequest.password());
        return new AuthResponse(token);
    }

    @ResponseStatus(HttpStatus.CREATED) //Returns 201 Created if successful.
    @PostMapping("/signup")
    public AuthResponse signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userService.hasUserWithUsername(signUpRequest.username())) {
            //If username or email already exists, throws DuplicatedUserInfoException.
            throw new DuplicatedUserInfoException(String.format("Username %s already been used", signUpRequest.username()));
        }
        if (userService.hasUserWithEmail(signUpRequest.email())) {
            throw new DuplicatedUserInfoException(String.format("Email %s already been used", signUpRequest.email()));
        }

        //Converts the request into a User object (mapSignUpRequestToUser()).
        //Saves the user in the database.

        userService.saveUser(mapSignUpRequestToUser(signUpRequest));
        // Automatically logs in the new user and returns a JWT token.
        String token = authenticateAndGetToken(signUpRequest.username(), signUpRequest.password());
        return new AuthResponse(token);
    }

    //Authenticates the user using AuthenticationManager
    //Generates a JWT token using TokenProvider

    private String authenticateAndGetToken(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return tokenProvider.generate(authentication);
    }

   // Creates a User object from SignUpRequest.Encodes the password before saving.Sets default role to USER.
    private User mapSignUpRequestToUser(SignUpRequest signUpRequest) {
        User user = new User();
        user.setUsername(signUpRequest.username());
        user.setPassword(passwordEncoder.encode(signUpRequest.password()));
        user.setName(signUpRequest.name());
        user.setEmail(signUpRequest.email());
        user.setRole(SecurityConfig.USER);
        user.setProvider(OAuth2Provider.LOCAL);
        return user;
    }
}
