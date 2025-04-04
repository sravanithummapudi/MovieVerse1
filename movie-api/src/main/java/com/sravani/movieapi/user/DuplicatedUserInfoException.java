package com.sravani.movieapi.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
//HttpStatus.CONFLICT is typically used when there is a resource conflict (e.g., duplicate entries in a database).
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicatedUserInfoException extends RuntimeException {
    //super(message) calls the constructor of the parent class (RuntimeException) and passes the message parameter to it.
    public DuplicatedUserInfoException(String message) {
        super(message);
    }
}
