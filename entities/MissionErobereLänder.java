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

    /**
     * Erstellt eine Mission, bei der der Spieler eine bestimmte Anzahl Länder erobern muss:
     * entweder 24 Länder oder 18 Länder mit jeweils mindestens 2 Einheiten.
     *
     * @param nurLänder  true für die 24-Länder-Mission, false für die 18-Länder-Mission mit 2 min Einheiten
     * @param spieler    der Spieler, dem diese Mission zugewiesen wird
     * @param lv         die Landverwaltung zum Überprüfen der Besitzverhältnisse und Armeen
     */
    public MissionErobereLänder(boolean nurLänder, Spieler spieler, Landverwaltung lv) {
        this.nurLänder = nurLänder;
        this.spieler = spieler;
        this.lv = lv;
    }

    //getter
    public boolean getNurLänder(){
        return nurLänder;
    }
    public Spieler getSpieler(){
        return spieler;
    }

    /**
     * Prüft, ob die Länder-Eroberungsmission erfüllt ist:
     * – Bei nurLänder == true: der Spieler besitzt mindestens 24 Länder.
     * – Sonst: der Spieler besitzt mindestens 18 Länder und hat in jedem davon min 2 Einheiten.
     *
     * @return true, wenn die Missionsbedingungen erfüllt sind, sonst false
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

    /**
     * Liefert die Missionsbeschreibung als Text.
     *
     * @return "Erobere 24 Länder!" für die 24-Länder-Mission;
     *         andernfalls "Erobere 18 Länder und setze in jedes Land mindestens 2 Einheiten!"
     */
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
