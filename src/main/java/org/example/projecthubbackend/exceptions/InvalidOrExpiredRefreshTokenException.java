package org.example.projecthubbackend.exceptions;


public class InvalidOrExpiredRefreshTokenException extends RuntimeException {
    public InvalidOrExpiredRefreshTokenException() {super();}
    public InvalidOrExpiredRefreshTokenException(final String message) {super(message);}
}
