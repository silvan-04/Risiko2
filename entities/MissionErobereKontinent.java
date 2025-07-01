package Risiko.entities;

import Risiko.domain.*;
import Risiko.domain.exceptions.*;
import Risiko.entities.*;
import Risiko.persistence.FilePersistenceManager;


import java.util.List;

public class MissionErobereKontinent implements Missionskarten{
    private String kontinent1Name;
    private String kontinent2Name;
    private boolean zusatzKontinent;
    private List<Spieler> kontinent1;
    private List<Spieler> kontinent2;
    private Spieler spieler;
    private Landverwaltung lv;

    public String getKontinent1Name() {
        return kontinent1Name;
    }
    public String getKontinent2Name() {
        return kontinent2Name;
    }
    public boolean isZusatzKontinent() {
        return zusatzKontinent;
    }
    public List<Spieler> getKontinent1() {
        return kontinent1;
    }
    public List<Spieler> getKontinent2() {
        return kontinent2;
    }
    public Spieler getSpieler() {
        return spieler;
    }
    public Landverwaltung getLandverwaltung() {
        return lv;
    }

    public MissionErobereKontinent(String kontinent1Name, String kontinent2Name, List<Spieler> kontinent1Liste, List<Spieler> kontinent2Liste, boolean zusatzKontinent, Spieler spieler, Landverwaltung lv) {
        this.kontinent1Name = kontinent1Name;
        this.kontinent2Name = kontinent2Name;
        this.zusatzKontinent = zusatzKontinent;
        this.kontinent1 = kontinent1Liste;
        this.kontinent2 = kontinent2Liste;
        this.spieler = spieler;
        this.lv = lv;
    }

    /**
     * zum checken, ob die vorgegeben Kontinente eingenommen wurden oder auch ob der Kontinent nach Wahl schon eingenommen wurde.
     * @return
     */
    public boolean istErfuellt() {
                for (Spieler land : kontinent1) {
                    if (!(land == spieler)) {
                        return false;
                    }
                }
                for (Spieler land : kontinent2) {
                    if (!(land == spieler)) {
                        return false;
                    }
                }
                if (!zusatzKontinent) {
                    return true;
                } else {
                    int kontinente = 0;
                    if(lv.asienBesitzer(spieler)){
                        kontinente++;
                    }
                    if (lv.afrikaBesitzer(spieler)) {
                        kontinente++;
                    }
                    if (lv.australienBesitzer(spieler)){
                        kontinente++;
                    }
                    if(lv.nordamerikaBesitzer(spieler)){
                        kontinente++;
                    }
                    if(lv.europaBesitzer(spieler)){
                        kontinente++;
                    }
                    if(lv.suedamerikaBesitzer(spieler)){
                        kontinente++;
                    }
                    if(kontinente >= 3) {
                        return true;
                    } else {
                        return false;
                    }
                }
    }

    public String beschreibung() {
        if (!zusatzKontinent) {
            return ("Erobere " + kontinent1Name + " und " + kontinent2Name + "!");
        } else {
            return ("Erobere " + kontinent1Name + ", " + kontinent2Name + " und einen weiteren beliebigen Kontinent!");
        }
    }

    public String siegerNachricht(){
        return this.spieler.getName() + " hat gewonnen! \n Er hat die Mission: \""+ this.beschreibung() + "\" erfüllt.";
    }
}
