package Risiko.domain.exceptions;

public class DoppelterNameException extends Exception {
    public DoppelterNameException() {
        super("Der Name ist schon vergeben");
    }
}
