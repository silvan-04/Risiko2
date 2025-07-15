package Risiko.domain.exceptions;

public class DoppelteKarteException extends Exception {
    public DoppelteKarteException() {
        super("Du hast eine oder mehrere Karten mehrfach ausgewählt!");
    }
}