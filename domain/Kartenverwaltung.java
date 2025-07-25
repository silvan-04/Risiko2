package Risiko.domain;

import Risiko.domain.exceptions.*;
import Risiko.entities.*;
import Risiko.persistence.FilePersistenceManager;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * erstellt, mischt und verteilt Einheits- und Missionskarten
 * verwaltet Nachzieh- und Ablagestapel, überprüft Kartenlimits, wandelt Karten-IDs um und sichert bzw. lädt den Kartenzustand über den FilePersistenceManager
 * @author Juan
 * @version 1
 */
public class Kartenverwaltung implements Serializable {
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
     * Erstellt das Einheitskarten-Deck mit je 14 Kanonen-, Soldaten- und Reiterkarten
     * und verteilt anschließend an jeden Spieler aus spielerList eine zufällige
     * Missionskarte aus neun vordefinierten Typen.
     *
     * @param spielerList Liste der Spieler, die jeweils eine Missionskarte erhalten
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

    /**
     * Zieht zufällig eine Einheitskarte und befüllt bei Leerung den Stapel aus dem Ablagestapel.
     *
     * @return gezogene Einheitskarte
     */
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

    /**
     * Gibt true zurück, wenn der Spieler 5 oder mehr Einheitskarten besitzt.
     *
     * @param spieler der zu prüfende Spieler
     * @return true, wenn das Handkartenlimit erreicht ist
     */
    public boolean handkartenlimit(Spieler spieler){
        if (spieler.getEinheitskarten().size() >= 5){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sucht beim gegebenen Spieler anhand der Land-ID die zugehörige Einheitskarte.
     *
     * @param id       die ID des Landes
     * @param spieler  der Spieler, dessen Karten durchsucht werden
     * @return         die gefundene Einheitskarte
     * @throws NotYourCardException wenn der Spieler keine Karte mit dieser ID besitzt
     */
    public Einheitskarte idToKarte(String id, Spieler spieler) throws IdException, NotYourCardException {
        for(int i =0;i<spieler.getEinheitskarten().size();i++){
            if(spieler.getEinheitskarten().get(i).getLand().getID().equals(id)){
                return spieler.getEinheitskarten().get(i);
            }
        }
        throw new NotYourCardException();
    }

    /**
     * Speichert Einheitskartenstapel, Missionskarten, Ablagestapel und
     * die Einheitskarten jedes Spielers in separaten Dateien.
     *
     * @param spielerList Liste der Spieler, deren Karten ebenfalls gesichert werden
     * @return true, wenn alle Speichervorgänge erfolgreich waren
     * @throws IOException falls ein Fehler beim Schreiben der Dateien auftritt
     */
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

    /**
     * Lädt den Einheitskartenstapel, die Missionskarten, den Ablagestapel und
     * zuletzt die Einheitskarten aller Spieler aus den entsprechenden Persistenzdateien.
     *
     * @param spielerList Liste der Spieler, deren Karten geladen werden
     * @throws KeinSpeicherstandException wenn eine der Speicherdateien nicht existiert oder ungültig ist
     * @throws IOException               bei allgemeinen Ein-/Ausgabefehlern
     */
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
        //Lädt missionen der Spieler
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
        //lädt Ablagestapel
        pm.openForReading("persistence/Speicher/Ablagestapelspeicher.txt");
        this.ablageliste.clear();
        try{
            ablageliste=pm.ladeAblageliste(lv);
        }catch(KeinSpeicherstandException e){
            pm.close();
            throw e;
        }
        pm.close();
        //Lädt Einheitskarten innerhalb der spieler
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
