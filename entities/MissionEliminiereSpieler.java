package Risiko.entities;

import Risiko.domain.*;
import Risiko.domain.exceptions.*;
import Risiko.persistence.FilePersistenceManager;

import java.io.Serializable;


public class MissionEliminiereSpieler implements Missionskarten, Serializable {
    private Spieler spieler;
    private Spieler zielspieler;

    /**
     * Erstellt eine Eliminationsmission für den angegebenen Spieler gegen einen Zielgegner.
     *
     * @param spieler     der Spieler, der die Mission erhält
     * @param zielspieler der Gegner, der eliminiert werden muss
     */
    public MissionEliminiereSpieler(Spieler spieler,Spieler zielspieler) {
        this.spieler = spieler;
        this.zielspieler = zielspieler;
    }

    //getter
    public Spieler getSpieler() {
        return spieler;
    }
    public Spieler getZielspieler() {
         return zielspieler;
    }

    /**
     * Prüft, ob die Eliminationsmission erfüllt ist (Zielspieler ausgeschieden).
     *
     * @return true, wenn der Zielspieler nicht mehr lebendig ist
     */
    public boolean istErfuellt() {
        if(!zielspieler.getLebendig()){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gibt die Missionsbeschreibung zurück, welche den Spieler auffordert, den Zielspieler zu eliminieren.
     *
     * @return String im Format "Eliminiere Spieler {Name}!"
     */
    public String beschreibung() {
        return ("Eliminiere Spieler " + zielspieler.getName() + "!");
    }

    /**
     * Siegesnachricht, wenn der Spieler die Eliminationsmission abgeschlossen hat.
     *
     * @return Nachricht im Format "{Spielername} hat gewonnen! Er hat die Mission: "{Beschreibung}" erfüllt."
     */
    public String siegerNachricht(){
        return this.spieler.getName() + " hat gewonnen! \n Er hat die Mission: \""+ this.beschreibung() + "\" erfüllt.";
    }

}