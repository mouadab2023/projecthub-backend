package org.example.projecthubbackend.exceptions;

public class MissingRefreshTokenException extends RuntimeException {
    public MissingRefreshTokenException() {super();}
    public MissingRefreshTokenException(final String message) {super(message);}
}
