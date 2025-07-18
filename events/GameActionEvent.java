package Risiko.events;

import java.io.Serial;

import Risiko.domain.Welt;
import Risiko.entities.Spieler;

public class GameActionEvent extends GameEvent {

    @Serial
    private static final long serialVersionUID = -2391443656175761807L;

    public enum GameActionEventType { ATTACK, NEW_OWNER, BUY_ITEM }

    private final GameActionEventType type;
    private final Spieler verteidiger;
    private final Welt welt;

    public GameActionEvent(Spieler player,GameActionEventType type,Welt welt) {
        // ATTACK: Spieler wird angegriffen
        // NEW_OWNER: Spieler ist neuer Eigentümer (einer Provinz)
        // BUY_ITEM: Spieler kann Item kaufen
        super(player);
        this.verteidiger=null;
        this.type = type;
        this.welt=welt;
    }
    public GameActionEvent(Spieler player, Spieler verteidiger, GameActionEventType type, Welt welt) {
        // ATTACK: Spieler wird angegriffen
        // NEW_OWNER: Spieler ist neuer Eigentümer (einer Provinz)
        // BUY_ITEM: Spieler kann Item kaufen
        super(player);
        this.verteidiger=verteidiger;
        this.type = type;
        this.welt=welt;
    }

    public GameActionEventType getType() {
        return type;
    }
    public Spieler getVerteidiger() {
        return verteidiger;

    }
    public Welt getWelt(){
        return welt;
    }
}
