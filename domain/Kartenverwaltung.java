package Risiko.domain;

import Risiko.domain.exceptions.*;
import Risiko.entities.*;
import Risiko.persistence.FilePersistenceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Klasse zur Verwaltung von Einheitskarten
 *
 * @author Juan
 * @version 1
 */
public class Kartenverwaltung {
    private List<Einheitskarte> einheitskarten = new ArrayList<>();
    private List<Missionskarten> missionskarten = new ArrayList<>();
    private List<Einheitskarte> ablageliste = new ArrayList<>();
    private Landverwaltung lv;
    private FilePersistenceManager pm =new FilePersistenceManager();

    public Kartenverwaltung(Landverwaltung lv) {
        this.lv = lv;
    }

    //getter
    public List<Einheitskarte> getEinheitskarten() { return einheitskarten;}
    public List<Missionskarten> getMissionskarten() { return missionskarten;}
    public List<Einheitskarte> getablageliste() { return ablageliste;}

    /**
     *
     * @param spielerList
     */
    public void kartenErstellung(List<Spieler> spielerList){
        Random r = new Random();
        for(int i=0; i<14; i++) {
            einheitskarten.add(new Einheitskarte(lv.getLandListe().get(i),  "Kanone"));
        }
        for(int i=14; i<28; i++) {
            einheitskarten.add(new Einheitskarte(lv.getLandListe().get(i),  "Soldat"));
        }
        for(int i=28; i<42; i++) {
            einheitskarten.add(new Einheitskarte(lv.getLandListe().get(i),  "Reiter"));
        }
        List<Integer> möglichkeiten = new ArrayList<>();
        for(int i=0; i<9; i++){
            möglichkeiten.add(i);
        }
        List<Integer> vorherigesZiel = new ArrayList<>();
        for(Spieler spieler : spielerList) {
            int möglichkeitenZahl = r.nextInt(möglichkeiten.size());
            int mission = möglichkeiten.get(möglichkeitenZahl);
            if (mission != 8) {
                möglichkeiten.remove(möglichkeitenZahl);
            }
            switch (mission) {
                case 0 ->{
                    missionskarten.add(new MissionErobereKontinent("Nordamerika", "Afrika", lv.getNordamerika(), lv.getAfrika(), false, spieler, this.lv));
                }
                case 1 ->{
                    missionskarten.add(new MissionErobereKontinent("Nordamerika", "Australien", lv.getNordamerika(), lv.getAustralien(), false, spieler, this.lv));
                }
                case 2 ->{
                    missionskarten.add(new MissionErobereKontinent("Asien", "Afrika", lv.getAsien(), lv.getAfrika(), false, spieler, this.lv));
                }
                case 3 ->{
                    missionskarten.add(new MissionErobereKontinent("Asien", "Südamerika", lv.getAsien(), lv.getSuedamerika(), false, spieler, this.lv));
                }
                case 4 ->{
                    missionskarten.add(new MissionErobereKontinent("Europa", "Südamerika", lv.getEuropa(), lv.getSuedamerika(), true, spieler, this.lv));
                }
                case 5 ->{
                    missionskarten.add(new MissionErobereKontinent("Europa", "Australien", lv.getEuropa(), lv.getAustralien(), true, spieler, this.lv));
                }
                case 6 ->{
                    missionskarten.add(new MissionErobereLänder(false, spieler, this.lv));
                }
                case 7 ->{
                    missionskarten.add(new MissionErobereLänder(true, spieler, this.lv));
                }
                case 8 -> {
                    boolean richtig = false;
                    do {
                        int zielSpieler = r.nextInt(spielerList.size());
                        vorherigesZiel.add(zielSpieler);
                        if (vorherigesZiel.contains(zielSpieler)) {
                            if (zielSpieler != spieler.getId()) {
                                missionskarten.add(new MissionEliminiereSpieler(spieler, spielerList.get(zielSpieler)));
                                richtig = true;
                            }
                        }
                    }while (!richtig) ;
                }
            }
        }




    }

    public Einheitskarte getZufälligeKarte(){
        Random r = new Random();
        int kartenIndex = r.nextInt(einheitskarten.size());
        Einheitskarte temp = einheitskarten.get(kartenIndex);
        einheitskarten.remove(kartenIndex);
        if(einheitskarten.isEmpty()){
            einheitskarten = ablageliste;
            ablageliste.clear();
        }
        return temp;
    }

    public boolean handkartenlimit(Spieler spieler){
        if (spieler.getEinheitskarten().size() >= 5){
            return true;
        } else {
            return false;
        }
    }

    public Einheitskarte idToKarte(String id, Spieler spieler) throws IdException, NotYourCardException {
        for(int i =0;i<spieler.getEinheitskarten().size();i++){
            if(spieler.getEinheitskarten().get(i).getLand().getID().equals(id)){
                return spieler.getEinheitskarten().get(i);
            }
        }
        throw new NotYourCardException();
    }

    public boolean speichereKarten(List<Spieler>spielerList) throws IOException {
        boolean gespeichert=true;
        //Einheitskartenstapel speichern
        pm.openForWriting("persistence/Speicher/Einheitskartenspeicher.txt");
        if(!pm.speichereEinheitskarten(this.einheitskarten)){
            gespeichert=false;
        }
        pm.close();
        //Missionskarten speichern
        pm.openForWriting("persistence/Speicher/Missionspeicher.txt");
        for(Missionskarten missionskarten:missionskarten){
            if(!pm.speichereMissionen(missionskarten)){
                gespeichert=false;
            }
        }
        pm.close();
        //Ablagestapel speichern
        pm.openForWriting("persistence/Speicher/Ablagestapelspeicher.txt");
        if(!pm.speichereAblageliste(ablageliste)){
            gespeichert=false;
        }
        pm.close();
        //Einheitskarten in Spieler speichern.
        pm.openForWriting("persistence/Speicher/SpielerEinheitskarten.txt");
        for(Spieler spieler:spielerList){
            if(!pm.speichereSpielerKarten(spieler)){
                gespeichert=false;
            }
        }
        pm.close();
        return gespeichert;
    }
    public void ladeKarten(List<Spieler>spielerList) throws KeinSpeicherstandException, IOException {
       // Lade Einheitskartenstapel
        pm.openForReading("persistence/Speicher/Einheitskartenspeicher.txt");
        this.einheitskarten.clear();
        try{
            einheitskarten=pm.ladeEinheitskarten(lv);
        }catch(KeinSpeicherstandException e){
            pm.close();
            throw e;
        }
        //Läd missionen der Spieler
        pm.openForReading("persistence/Speicher/Missionspeicher.txt");
        this.missionskarten.clear();
        try {
            for (int i = 0; i < spielerList.size(); i++) {
                missionskarten.add(pm.ladeMissionen(spielerList, lv));
            }
        }catch(KeinSpeicherstandException e){
            pm.close();
            throw e;
        }
        pm.close();
        //läd Ablagestapel
        pm.openForReading("persistence/Speicher/Ablagestapelspeicher.txt");
        this.ablageliste.clear();
        try{
            ablageliste=pm.ladeAblageliste(lv);
        }catch(KeinSpeicherstandException e){
            pm.close();
            throw e;
        }
        pm.close();
        //Läd Einheitskarten innerhalb der spieler
        pm.openForReading("persistence/Speicher/SpielerEinheitskarten.txt");
        try {
            for (Spieler spieler : spielerList) {
                spieler.setEinheitsKartenListe(pm.ladeSpielerKarten(lv));
            }
        }catch(KeinSpeicherstandException e){
            pm.close();
            throw e;
        }
        pm.close();
    }

}
