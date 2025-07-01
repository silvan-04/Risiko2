package Risiko.domain.exceptions;

public class IdException extends RuntimeException {
    public IdException() {
        super("ID existiert nicht!");
    }
}
