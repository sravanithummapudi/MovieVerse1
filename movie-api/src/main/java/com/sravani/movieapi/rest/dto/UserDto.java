package com.sravani.movieapi.rest.dto;

import com.sravani.movieapi.user.User;

//When fetching user details (GET /api/users/{username}), the User entity is retrieved.
//This method converts the entity into a UserDto before sending the response.
public record UserDto(Long id, String username, String name, String email, String role) {

    public static UserDto from(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}