package Risiko.entities;

import Risiko.domain.*;
import Risiko.domain.exceptions.*;
import Risiko.entities.*;
import Risiko.persistence.FilePersistenceManager;


import java.io.Serializable;
import java.util.List;

public class MissionErobereLänder implements Missionskarten, Serializable {
    boolean nurLänder;
    Spieler spieler;
    Landverwaltung lv;

    public MissionErobereLänder(boolean nurLänder, Spieler spieler, Landverwaltung lv) {
        this.nurLänder = nurLänder;
        this.spieler = spieler;
        this.lv = lv;
    }

    public boolean getNurLänder(){
        return nurLänder;
    }
    public Spieler getSpieler(){
        return spieler;
    }

    /**
     * Zum überprüfen, ob man die Mission mit den 24 oder 18 Ländern mit 2 Einheiten erfüllen muss und erfüllt hat
     * @return
     */
    public boolean istErfuellt() {
        if(nurLänder){
            if(spieler.getAnzahlLaender() >= 24){
                return true;
            } else {
                return false;
            }
        } else {
            if(spieler.getAnzahlLaender() >= 18){
                List<Land> spielerLänder = lv.landAusgabeVonSpieler(spieler);
                for (Land land: spielerLänder) {
                    if(land.getArmee() < 2) {
                        return false;
                    }
                }
                return true;
            } else{
                return false;
            }
        }
    }

    public String beschreibung() {
        if(nurLänder){
            return "Erobere 24 Länder!";
        } else {
            return "Erobere 18 Länder und setze in jedes Land mindestens 2 Einheiten!";
        }
    }
    public String siegerNachricht(){
        return this.spieler.getName() + " hat gewonnen!!!!!!! \n Er hat die Mission: \""+ this.beschreibung() + "\" erfüllt.";
    }
}
