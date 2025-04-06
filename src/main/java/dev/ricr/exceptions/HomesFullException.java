package dev.ricr.exceptions;

public class HomesFullException extends RuntimeException {
    public HomesFullException(String message) {
        super("Homes full: " + message);
    }
}
