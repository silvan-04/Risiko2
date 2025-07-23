package Risiko.entities;

import Risiko.domain.*;
import Risiko.domain.exceptions.*;
import Risiko.entities.*;
import Risiko.persistence.FilePersistenceManager;


import java.io.Serializable;
import java.util.List;

public class MissionErobereKontinent implements Missionskarten, Serializable {
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

    /**
     * Erzeugt eine Mission, in der der Spieler zwei vorgegebene Kontinente
     * (und optional einen dritten beliebigen) vollständig erobern muss.
     *
     * @param kontinent1Name   Name des ersten zu erobernden Kontinents
     * @param kontinent2Name   Name des zweiten zu erobernden Kontinents
     * @param kontinent1Liste  aktuelle Besitzverhältnisse im ersten Kontinent
     * @param kontinent2Liste  aktuelle Besitzverhältnisse im zweiten Kontinent
     * @param zusatzKontinent  true, wenn zusätzlich ein beliebiger dritter Kontinent erobert werden soll
     * @param spieler          der Spieler, der diese Mission erhält
     * @param lv               die Landverwaltung zum Abrufen weiterer Kontinentsdaten
     */
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
     * Prüft, ob der Spieler beide vorgegebenen Kontinente vollständig besitzt.
     * Ist zusatzKontinent true, gilt die Mission erst als erfüllt, wenn zusätzlich
     * ein beliebiger dritter Kontinent eingenommen wurde (insgesamt drei Kontinente).
     *
     * @return true, wenn die Missionsbedingungen erfüllt sind, sonst false
     */
    public boolean istErfuellt() {
                kontinent1 = lv.nameToList(kontinent1Name);
                kontinent2 = lv.nameToList(kontinent2Name);
                for (Spieler land : kontinent1) {
                    if (!(land.getName().equals(spieler.getName()))) {
                        return false;
                    }
                }
                for (Spieler land : kontinent2) {
                    if (!(land.getName().equals(spieler.getName()))) {
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

    /**
     * Liefert die Missionsbeschreibung:
     * -> Ohne Zusatzkontinent: „Erobere Kontinent1 und Kontinent2!“
     * -> Mit Zusatzkontinent: „Erobere Kontinent1, Kontinent2 und einen weiteren beliebigen Kontinent!“
     *
     * @return Beschreibungstext der Mission
     */
    public String beschreibung() {
        if (!zusatzKontinent) {
            return ("Erobere " + kontinent1Name + " und " + kontinent2Name + "!");
        } else {
            return ("Erobere " + kontinent1Name + ", " + kontinent2Name + " und einen weiteren beliebigen Kontinent!");
        }
    }

    /**
     * Gibt die Siegesnachricht zurück, wenn der Spieler die Mission erfolgreich abgeschlossen hat.
     *
     * @return Text mit Spielername und Missionsbeschreibung
     */
    public String siegerNachricht(){
        return this.spieler.getName() + " hat gewonnen! \nEr hat die Mission: \""+ this.beschreibung() + "\" erfüllt.";
    }
}
