package Risiko.events;

import java.io.Serial;

import Risiko.domain.Welt;
import Risiko.entities.Land;
import Risiko.entities.Spieler;

public class GameActionEvent extends GameEvent {

    @Serial
    private static final long serialVersionUID = -2391443656175761807L;

    public enum GameActionEventType { ATTACK, NEW_OWNER, BUY_ITEM, NEXT_PHASE, UPDATE, VERTEIDIGUNG }

    private final GameActionEventType type;
    private final Spieler verteidiger;
    private final int verteidigen;
    private final Welt welt;
    private final String message;
    private final String zielLand;

    public GameActionEvent(Spieler player,GameActionEventType type,Welt welt) {
        super(player);
        this.verteidiger=null;
        this.type = type;
        this.welt=welt;
        this.message=null;
        this.zielLand=null;
        this.verteidigen = 0;
    }
    //Attack konstruktor
    public GameActionEvent(Spieler player, Spieler verteidiger, String message, String zielLand, Welt welt) {
        super(player);
        this.verteidiger=verteidiger;
        this.message=message;
        this.type = GameActionEvent.GameActionEventType.ATTACK;
        this.welt=welt;
        this.zielLand=zielLand;
        this.verteidigen = 0;
    }
    //Verteidigung Konstruktor
    public GameActionEvent(Spieler player, Welt welt,int verteidigen) {
        super(player);
        this.verteidiger=null;
        this.verteidigen= verteidigen;
        this.message=null;
        this.type = GameActionEvent.GameActionEventType.VERTEIDIGUNG;
        this.welt=welt;
        this.zielLand=null;
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
    public String getMessage(){
        return message;
    }
    public String getZielLand(){
        return zielLand;
    }
    public int getVerteidigen(){
        return verteidigen;
    }
}
