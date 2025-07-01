package Risiko.domain.exceptions;

public class KeinSpeicherstandException extends Exception {
    public KeinSpeicherstandException() {
        super("Es ist kein Speicherstand vorhanden!");
    }
}
