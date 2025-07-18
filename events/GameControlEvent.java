package Risiko.events;

import Risiko.domain.Welt;
import Risiko.entities.Spieler;


import java.io.Serial;

public class GameControlEvent extends GameEvent {

    @Serial
    private static final long serialVersionUID = 4833660998427328149L;

    public enum GameControlEventType {GAME_STARTED, NEXT_TURN, GAME_OVER,PLAYER_JOINED,GAME_CAN_START}

    private final GameControlEventType type;
    private final int phase;
    private final int playerCount;
    private final Welt welt;

    public GameControlEvent(Welt welt, GameControlEventType type) {
        // GAME_STARTED: Spieler beginnt
        // GAME_OVER: Spieler hat gewonnen
        // NEXT_TURN: Nächste Phase / nächster Spieler
        super(welt.aktiverSpieler());

        this.type = type;
        this.phase = welt.getPhase();
        this.playerCount = welt.getSpielerListe().size();
        this.welt = welt;
    }

    public GameControlEventType getType() {
        return type;
    }

    public int getPhase() {
        return this.phase;
    }
    public int getPlayerCount() {
        return this.playerCount;
    }
    public Welt getWelt() {
        return this.welt;
    }
}