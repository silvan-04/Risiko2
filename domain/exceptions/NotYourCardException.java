package Risiko.domain.exceptions;

public class NotYourCardException extends Exception {
    public NotYourCardException() {
        super("Diese Karten sind nicht in deinem Besitz!");
    }
}