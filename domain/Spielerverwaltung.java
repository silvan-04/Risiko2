package Risiko.domain;

import Risiko.domain.exceptions.*;
import Risiko.entities.*;
import Risiko.persistence.FilePersistenceManager;


import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * Klasse zur Verwaltung von Spieler
 *
 * @author silvan
 * @version 1
 */
public class Spielerverwaltung implements Serializable {
    private List<Spieler> spielerListe= new ArrayList();
    private FilePersistenceManager pm = new FilePersistenceManager();
    private List<String> charBilder = new ArrayList<>(Arrays.asList("Charaktere/affe.png", "Charaktere/elefant.png", "Charaktere/hase.png", "Charaktere/katze.png", "Charaktere/lama.png", "Charaktere/Maus.png", "Charaktere/taube.png", "Charaktere/tiger.png", "Charaktere/waschbär.png","Charaktere/ziege.png"));
    /**
     * Methode um auf die Spielerliste zu zugreifen
     * @return Liste
     */
    public List<Spieler> getSpielerListe() {
        return spielerListe;
    }

    /**
     * methode um Spieler zu erstellen und in die Liste hinzuzufügen
     * @param name
     */
    public void spielerHinzufuegen(String name)throws DoppelterNameException {
        for(Spieler spieler : spielerListe ){
            if (Objects.equals(spieler.getName(), name)) {
                spielerListe.clear();
                Spieler.setIdZaehler(0);
                throw new DoppelterNameException();
            }
        }
        Random rand = new Random();
        int bild = rand.nextInt(charBilder.size());
        spielerListe.add(new Spieler(name,charBilder.get(bild)));
        charBilder.remove(bild);

    }

    /**
     * Methode um abzufragen ob Spieler am Zug ist.
     * @param spieler (id zahl)
     * @return boolean
     */
    public boolean getAmZug(int spieler){
        return spielerListe.get(spieler).getIstAmZug();
    }

    /**
     * Methode um istAmZug von Spieler zu ändern.
     * @param spieler (id zahl)
     * @param istAmZug (gewünschter boolean)
     */
    public void setAmZug(int spieler, boolean istAmZug){
        spielerListe.get(spieler).setIstAmZug(istAmZug);
    }

    /**
     * Methode um abzufragen ob Spieler am Leben ist.
     * @param spieler
     * @return boolean
     */
    public boolean getLebendig(int spieler){
        return spielerListe.get(spieler).getLebendig();
    }

    /**
     * Methode lebendig boolean von Spieler zu setten.
     * @param spieler
     * @param istLebendig
     */
    public void setLebendig(int spieler, boolean istLebendig){
        spielerListe.get(spieler).setLebendig(istLebendig);
    }

    /**
     * Methode um istAmZug zu rotieren. Nächster spieler, welcher lebendig ist, ist am Zug.
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
     * Methode um Spieler zu ermittlen welcher am zug ist.
     * @return Spieler
     */
    public Spieler aktiverSpieler(){
        for(int i=0;i<this.spielerListe.size();i++){
            if(this.spielerListe.get(i).getIstAmZug()){
                return spielerListe.get(i);
            }
        }
        return null;
    }

    public boolean kartenCheck(Spieler spieler, Einheitskarte karte1, Einheitskarte karte2, Einheitskarte karte3) throws SymbolException, NotYourCardException{
        if(spieler.getEinheitskarten().contains(karte1) && spieler.getEinheitskarten().contains(karte2) && spieler.getEinheitskarten().contains(karte3)){
            if(karte1.getSymbol().equals("Reiter") && karte2.getSymbol().equals("Reiter") && karte3.getSymbol().equals("Reiter")){
                return true;
            } else if (karte1.getSymbol().equals("Soldat") && karte2.getSymbol().equals("Soldat") && karte3.getSymbol().equals("Soldat")) {
                return true;
            } else if (karte1.getSymbol().equals("Kanone") && karte2.getSymbol().equals("Kanone") && karte3.getSymbol().equals("Kanone")) {
                return true;
            } else if (karte1.getSymbol().equals("Reiter") && karte2.getSymbol().equals("Kanone") && karte3.getSymbol().equals("Soldat")) {
                return true;
            } else {
                throw new SymbolException();
            }
        } else {
            throw new NotYourCardException();
        }
    }


    /**
     * Methode um Einheiten von aktiven Spieler am Anfang der Runde zu berechnen , gibt diese als int zurück
     * @param lv (landverwaltung)
     * @return int
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
     * Check für alle Spieler, ob ein Spieler kein Land mehr hat, wenn ja setzt er lebendig auf false.
     */
    public void loserCheck(){
        for(Spieler spieler:spielerListe){
            if(spieler.getAnzahlLaender()==0){
                spieler.setLebendig(false);
            }
        }
    }

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
    public void ladeSpieler() throws IOException, KeinSpeicherstandException {
        pm.openForReading("persistence/Speicher/Spielerspeicher.txt");
        spielerListe.clear();
        try {
            Spieler spieler1 = this.pm.ladeSpieler(); // Ein Spieler laden, damit idzaeler von klasse Spieler gesettet wird.
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
