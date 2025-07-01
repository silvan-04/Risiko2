package Risiko.domain.exceptions;

public class NotYourLandException extends Exception {
    public NotYourLandException() {
        super("Dieses Land ist nicht in deinem Besitz!");
    }
}
