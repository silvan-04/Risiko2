package Risiko.domain.exceptions;

public class NoHostileNeighborException extends RuntimeException {
    public NoHostileNeighborException() {
        super("Dieses Risiko.entities.Land hat keinen feindlichen Nachbar!");
    }
}
