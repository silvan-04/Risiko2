package Risiko.domain.exceptions;

public class NachbarException extends Exception {
    public NachbarException() {
        super("Dieses Land ist kein Nachbarland.");
    }
}
