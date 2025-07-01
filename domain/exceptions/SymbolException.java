package Risiko.domain.exceptions;

public class SymbolException extends Exception {
    public SymbolException() {
        super("Diese Karten haben nicht die richtigen Symbole um eingelöst zu werden!");
    }
}
