package Risiko.domain.exceptions;

public class YourLandException extends RuntimeException {
    public YourLandException() {
        super("Dieses Land ist in deinem Besitz!");
    }
}
