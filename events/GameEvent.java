package Risiko.events;

import java.io.Serial;
import java.io.Serializable;
import Risiko.entities.Spieler;

public abstract class GameEvent implements Serializable {

    @Serial
    private static final long serialVersionUID = -5234940566773150168L;

    private final Spieler spieler;

    public GameEvent(Spieler Spieler) {
        super();
        this.spieler = Spieler;
    }

    public Spieler getSpieler() {
        return spieler;
    }
}

