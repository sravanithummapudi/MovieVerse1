package com.sravani.movieapi.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
// automatically gives 404 error when user not found
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    //super(message) calls the constructor of the parent class (RuntimeException) and passes the message parameter to it.
    public UserNotFoundException(String message) {
        super(message);
    }
}
