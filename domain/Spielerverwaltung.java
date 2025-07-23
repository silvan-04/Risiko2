package Risiko.domain;

import Risiko.domain.exceptions.*;
import Risiko.entities.*;
import Risiko.persistence.FilePersistenceManager;


import java.io.*;
import java.net.URL;
import java.util.*;

public class Spielerverwaltung implements Serializable {
    private List<Spieler> spielerListe= new ArrayList();
    private FilePersistenceManager pm = new FilePersistenceManager();
    private List<String> charBilder = new ArrayList<>(Arrays.asList("Charaktere/affe.png", "Charaktere/elefant.png", "Charaktere/hase.png", "Charaktere/katze.png", "Charaktere/lama.png", "Charaktere/Maus.png", "Charaktere/taube.png", "Charaktere/tiger.png", "Charaktere/waschbär.png","Charaktere/ziege.png"));

    /**
     * Gibt die aktuelle Liste aller Spieler im Spiel zurück.
     *
     * @return Liste der registrierten Spieler
     */
    public List<Spieler> getSpielerListe() {
        return spielerListe;
    }

    /**
     * Fügt einen neuen Spieler mit zufälligem Bild hinzu und wirft bei Namenskonflikt eine DoppelterNameException.
     * Im Offline-Modus werden zuvor Spielerliste und ID-Zähler zurückgesetzt.
     *
     * @param name   gewünschter Spielername
     * @param online true=Online-Modus, false=Offline-Modus
     * @throws DoppelterNameException bei doppeltem Namen
     */
    public void spielerHinzufuegen(String name,boolean online)throws DoppelterNameException {
        if(!online) {
            for (Spieler spieler : spielerListe) {
                if (Objects.equals(spieler.getName(), name)) {
                    spielerListe.clear();
                    Spieler.setIdZaehler(0);
                    throw new DoppelterNameException();
                }
            }
            Random rand = new Random();
            int bild = rand.nextInt(charBilder.size());
            spielerListe.add(new Spieler(name, charBilder.get(bild)));
            charBilder.remove(bild);
        }else{
            for (Spieler spieler : spielerListe) {
                if (Objects.equals(spieler.getName(), name)) {
                    throw new DoppelterNameException();
                }
            }
            Random rand = new Random();
            int bild = rand.nextInt(charBilder.size());
            spielerListe.add(new Spieler(name, charBilder.get(bild)));
            charBilder.remove(bild);
        }
    }

    /**
     * Gibt zurück, ob der Spieler mit der angegebenen ID aktuell am Zug ist.
     *
     * @param spieler ID bzw. ID des Spielers
     * @return true, wenn dieser Spieler am Zug ist
     */
    public boolean getAmZug(int spieler){
        return spielerListe.get(spieler).getIstAmZug();
    }

    /**
     * Setzt, ob der Spieler mit dem angegebenen ID am Zug ist.
     *
     * @param spieler ID des Spielers in der Liste
     * @param istAmZug true, wenn dieser Spieler am Zug sein soll
     */

    public void setAmZug(int spieler, boolean istAmZug){
        spielerListe.get(spieler).setIstAmZug(istAmZug);
    }

    /**
     * Prüft, ob der Spieler mit dem angegebenen ID noch im Spiel ist.
     *
     * @param spieler ID des zu prüfenden Spielers in der Spielerliste
     * @return true, wenn der Spieler lebendig (nicht eliminiert) ist
     */
    public boolean getLebendig(int spieler){
        return spielerListe.get(spieler).getLebendig();
    }

    /**
     * Setzt den Lebensstatus des Spielers mit dem angegebenen ID.
     *
     * @param spieler     Index des Spielers in der Spielerliste
     * @param istLebendig true, wenn der Spieler lebendig sein soll
     */
    public void setLebendig(int spieler, boolean istLebendig){
        spielerListe.get(spieler).setLebendig(istLebendig);
    }

    /**
     * Findet den aktuellen Zugspieler, deaktiviert ihn und gibt den Zug
     * an den nächsten lebendigen Spieler in der Liste weiter.
     */
    public void naechsterZug(){
        for(int i=0; i<spielerListe.size();i++){
            if(spielerListe.get(i).getIstAmZug()){
               int j = i;
               do {
                   spielerListe.get(j).setIstAmZug(false);
                   spielerListe.get((++j)%spielerListe.size()).setIstAmZug(true);
               }
               while(!spielerListe.get(i).getLebendig());
               break;
            }
        }
    }


    /**
     * Gibt den Spieler zurück, der derzeit am Zug ist.
     *
     * @return der aktive Spieler oder null, falls kein Spieler markiert ist
     */
    public Spieler aktiverSpieler(){
        for(int i=0;i<this.spielerListe.size();i++){
            if(this.spielerListe.get(i).getIstAmZug()){
                return spielerListe.get(i);
            }
        }
        return null;
    }

    /**
     * Prüft, ob der Spieler drei gültige Einheitskarten zum Einlösen hat:
     * entweder drei gleiche Symbole oder genau eine Kanone, einen Soldaten und einen Reiter.
     *
     * @param spieler der Spieler, dessen Karten geprüft werden
     * @param karte1  erste Karte
     * @param karte2  zweite Karte
     * @param karte3  dritte Karte
     * @return true, wenn die Kartenkombination gültig ist
     * @throws SymbolException          wenn die Kartensymbole nicht passen
     * @throws NotYourCardException     wenn eine Karte nicht zum Spieler gehört
     * @throws DoppelteKarteException   wenn Karten doppelt gewählt wurden
     */
    public boolean kartenCheck(Spieler spieler, Einheitskarte karte1, Einheitskarte karte2, Einheitskarte karte3) throws SymbolException, NotYourCardException, DoppelteKarteException{
        if(karte1 != karte2 && karte1 != karte3 && karte2 != karte3) {
            boolean karte1Bool=false;
            boolean karte2Bool=false;
            boolean karte3Bool=false;
            boolean kartenBesitz=false;
            for(Einheitskarte karte: spieler.getEinheitskarten()){
                if(karte.getLandToString().equals(karte1.getLandToString())){
                    karte1Bool=true;
                }
                if(karte.getLandToString().equals(karte2.getLandToString())){
                    karte2Bool=true;
                }
                if(karte.getLandToString().equals(karte3.getLandToString())){
                    karte3Bool=true;
                }
                if(karte1Bool && karte2Bool && karte3Bool){
                    kartenBesitz = true;
                    break;
                }
            }
            if (kartenBesitz) {
                if (karte1.getSymbol().equals("Reiter") && karte2.getSymbol().equals("Reiter") && karte3.getSymbol().equals("Reiter")) {
                    return true;
                } else if (karte1.getSymbol().equals("Soldat") && karte2.getSymbol().equals("Soldat") && karte3.getSymbol().equals("Soldat")) {
                    return true;
                } else if (karte1.getSymbol().equals("Kanone") && karte2.getSymbol().equals("Kanone") && karte3.getSymbol().equals("Kanone")) {
                    return true;
                } else if (karte1.getSymbol().equals("Reiter") && karte2.getSymbol().equals("Kanone") && karte3.getSymbol().equals("Soldat")) {
                    return true;
                }else if (karte1.getSymbol().equals("Reiter") && karte2.getSymbol().equals("Soldat") && karte3.getSymbol().equals("Kanone")) {
                    return true;
                }else if (karte1.getSymbol().equals("Kanone") && karte2.getSymbol().equals("Reiter") && karte3.getSymbol().equals("Soldat")) {
                    return true;
                }else if (karte1.getSymbol().equals("Kanone") && karte2.getSymbol().equals("Soldat") && karte3.getSymbol().equals("Reiter")) {
                    return true;
                }else if (karte1.getSymbol().equals("Soldat") && karte2.getSymbol().equals("Kanone") && karte3.getSymbol().equals("Reiter")) {
                    return true;
                }else if (karte1.getSymbol().equals("Soldat") && karte2.getSymbol().equals("Reiter") && karte3.getSymbol().equals("Kanone")) {
                    return true;
                } else {
                    throw new SymbolException();
                }
            } else {
                throw new NotYourCardException();
            }
        } else {
            throw new DoppelteKarteException();
        }
    }


    /**
     * Ermittelt die Start-Einheiten des aktiven Spielers anhand von Länderzahl und Kontinentbonus.
     *
     * @param lv Landverwaltung für die Kontinentprüfungen
     * @return Anzahl der neu zu verteilenden Einheiten
     */
    public int armeeVerteilung(Landverwaltung lv){
        int rückgabe =0;
        Spieler spieler = aktiverSpieler();
        lv.kontinentAktualisieren();
        if(spieler.getAnzahlLaender()<12){
            rückgabe=rückgabe+3;
        }else {
            rückgabe =rückgabe +spieler.getAnzahlLaender()/3;
        }
        if(lv.asienBesitzer(spieler) ){
            rückgabe = rückgabe+7;
        }
        if(lv.nordamerikaBesitzer(spieler)){

            rückgabe = rückgabe+5;
        }
        if(lv.europaBesitzer(spieler)){
            rückgabe = rückgabe+5;
        }
        if(lv.afrikaBesitzer(spieler)){
            rückgabe = rückgabe+3;
        }
        if(lv.suedamerikaBesitzer(spieler)){
            rückgabe = rückgabe+2;
        }
        if(lv.australienBesitzer(spieler)){
            rückgabe = rückgabe+2;
        }
        return rückgabe;
    }

    /**
     * Setzt lebendig=false für alle Spieler, die kein Land mehr besitzen.
     */
    public void loserCheck(){
        for(Spieler spieler:spielerListe){
            if(spieler.getAnzahlLaender()==0){
                spieler.setLebendig(false);
            }
        }
    }

    /**
     * Speichert alle Spieler in der Datei.
     *
     * @return true, wenn alle Spieler erfolgreich gespeichert wurden
     * @throws IOException  bei einem Fehler beim Schreiben
     */
    public boolean speichereSpieler() throws IOException{
        pm.openForWriting("persistence/Speicher/Spielerspeicher.txt");
        boolean gespeichert=true;
        for(Spieler spieler:spielerListe){
            if(!(this.pm.speichereSpieler(spieler))){
                gespeichert=false;
            }
        }
        pm.close();
        return gespeichert;
    }

    /**
     * Lädt alle Spieler aus der Datei, initialisiert dabei den Spieler-ID-Zähler und befüllt die Spielerliste.
     *
     * @throws IOException                   bei Ein-/Ausgabefehlern während des Lesens
     * @throws KeinSpeicherstandException    wenn kein gültiger Speicherstand vorliegt
     */
    public void ladeSpieler() throws IOException, KeinSpeicherstandException {
        pm.openForReading("persistence/Speicher/Spielerspeicher.txt");
        spielerListe.clear();
        try {
            Spieler spieler1 = this.pm.ladeSpieler(); // Ein Spieler laden, damit id zaeler von klasse Spieler gesettet wird.
            this.spielerListe.add(spieler1);
            for (int i = 1; i < Spieler.getIdZaehler(); i++) {
                this.spielerListe.add(this.pm.ladeSpieler());
            }
        }catch (KeinSpeicherstandException e){
            pm.close();
            throw e;
        }
        pm.close();
    }
}
