package dev.ricr.exceptions;

public class HomeNotFoundException extends RuntimeException {
    public HomeNotFoundException(String message) {
        super("Home not found: " + message);
    }
}
