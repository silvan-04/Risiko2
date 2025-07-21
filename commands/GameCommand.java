package Risiko.commands;

import Risiko.entities.Spieler;

import java.io.Serial;
import java.io.Serializable;

public class GameCommand implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final GameCommandType type;
    private final int payload;
    private final Spieler spieler;

    public GameCommand(GameCommandType type) {
        this.type = type;
        this.payload = 0;
        this.spieler = null;
    }

    public GameCommand(GameCommandType type, int payload,Spieler spieler) {
        this.type = type;
        this.payload = payload;
        this.spieler=spieler;
    }

    public GameCommandType getType() {
        return type;
    }

    public int getPayload() {
        return payload;
    }
    public Spieler getSpieler() {
        return spieler;
    }
}

