package Risiko.domain.exceptions;

public class SpielerAnzahlException extends RuntimeException {
    public SpielerAnzahlException() {
        super("Es müssen 2 bis 6 Risiko.entities.Spieler sein!");
    }
}
