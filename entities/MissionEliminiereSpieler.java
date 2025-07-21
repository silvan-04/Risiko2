package Risiko.entities;

import Risiko.domain.*;
import Risiko.domain.exceptions.*;
import Risiko.persistence.FilePersistenceManager;

import java.io.Serializable;


public class MissionEliminiereSpieler implements Missionskarten, Serializable {
    private Spieler spieler;
    private Spieler zielspieler;

    public MissionEliminiereSpieler(Spieler spieler,Spieler zielspieler) {
        this.spieler = spieler;
        this.zielspieler = zielspieler;
    }

    public Spieler getSpieler() {
        return spieler;
    }
    public Spieler getZielspieler() {
         return zielspieler;
    }

    /**
     * Zum überprüfen, ob der ZielSpieler noch lebendig ist.
     * @return
     */
    public boolean istErfuellt() {
        if(!zielspieler.getLebendig()){
            return true;
        } else {
            return false;
        }
    }

    public String beschreibung() {
        return ("Eliminiere Spieler " + zielspieler.getName() + "!");
    }

    public String siegerNachricht(){
        return this.spieler.getName() + " hat gewonnen! \n Er hat die Mission: \""+ this.beschreibung() + "\" erfüllt.";
    }

}