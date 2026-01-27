package org.example.projecthubbackend.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        super("Unauthorized: invalid or expired refresh token");
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}
