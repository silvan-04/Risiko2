package Risiko.entities;

import Risiko.domain.*;
import Risiko.domain.exceptions.*;
import Risiko.persistence.FilePersistenceManager;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Spieler implements Serializable {
    private static int idZaehler = 0;
    private int id;
    private String name;
    private int anzahlLaender;
    private int einheitenRunde;
    private String farbe;
    private String charBild;
    private boolean landErobert;
    private boolean istAmZug;
    private boolean lebendig;
    private List<Einheitskarte> einheitsKartenListe = new ArrayList<Einheitskarte>();
    private List<String> farben = new ArrayList<>(Arrays.asList("#1B3B6F","#8E2835","#728E4B","#5C215C","#BF5C00","#6F4E37"));

    public Spieler(String name) {
        this.name = name;
    }
    //Konstruktoren
    public Spieler(String name, String charBild) {
        this.id = idZaehler++;
        this.name = name;
        this.anzahlLaender = 0;
        this.einheitenRunde = 0;
        this.landErobert = false;
        this.istAmZug = false;
        this.lebendig = true;
        this.farbe = farben.get(this.id);
        this.charBild = charBild;
    }
    public Spieler(int id, String name, int anzahlLaender,int einheitenRunde ,boolean istAmZug, boolean lebendig, String charBild) {
        this.id = id;
        this.name = name;
        this.anzahlLaender = anzahlLaender;
        this.einheitenRunde = einheitenRunde;
        this.landErobert = false;
        this.istAmZug = istAmZug;
        this.lebendig = lebendig;
        this.farbe = farben.get(this.id);
        this.charBild = charBild;
    }
    //Getter
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getAnzahlLaender() {
        return anzahlLaender;
    }
    public int getEinheitenRunde() {
        return einheitenRunde;
    }
    public String getFarbe() {
        return farbe;
    }
    public String getCharBild() {
        return charBild;
    }
    public static int getIdZaehler(){
        return idZaehler;
    }
    public boolean isLandErobert () { return landErobert ; }
    public boolean getIstAmZug() {
        return istAmZug;
    }
    public boolean getLebendig() {
        return lebendig;
    }
    public List<Einheitskarte> getEinheitskarten() { return einheitsKartenListe; }
    //Setter
    public void setIstAmZug(boolean istAmZug) {
        this.istAmZug = istAmZug;
    }
    public void setAnzahlLaender(int anzahlLaender) {
        this.anzahlLaender = anzahlLaender;
    }
    public void setEinheitenRunde(int einheitenRunde) {this.einheitenRunde = einheitenRunde;}
    public void setLebendig(boolean lebendig) {
        this.lebendig = lebendig;
    }
    public void setLandErobert(boolean landErobert) { this.landErobert = landErobert; }
    public static void setIdZaehler(int idZaehler) {
        Spieler.idZaehler = idZaehler;
    }
    public void addEinheitskarten(Einheitskarte neueKarte){
        einheitsKartenListe.add(neueKarte);
    }
    public void setEinheitsKartenListe(List<Einheitskarte>  einheitsKartenListe) {
        this.einheitsKartenListe = einheitsKartenListe;
    }
    //toString-Methode
    public String toString() {
        return (id +1 )+ ".Spieler: " + name + " hat "+ anzahlLaender+" Länder und " + ((istAmZug) ? "ist am Zug.": "ist nicht am Zug.") +" Ist"+(this.lebendig?" noch im Spiel!":" ist ausgeschieden!");
    }
}

