package Risiko.domain;

import Risiko.domain.exceptions.*;
import Risiko.entities.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Welt {
    private Spielerverwaltung sv;
    private Landverwaltung lv;
    private Kartenverwaltung kv;
    private int kartenZähler;
    private int phase;

    public Welt() {
        this.sv = new Spielerverwaltung();
        this.lv = new Landverwaltung();
        this.kv = new Kartenverwaltung(this.lv);
        this.kartenZähler = 0;
        this.phase = 0;
    }

  // Metohden aus Spielerverwaltung:
    public void spielerHinzufuegen(String name) throws DoppelterNameException {
        try{
            sv.spielerHinzufuegen(name);
        } catch (DoppelterNameException e) {
            throw e;
        }
    }
    public boolean getAmZug(int spieler) {
        return sv.getAmZug(spieler);
    }
    public void setAmZug(int spieler, boolean istAmZug) {
        sv.setAmZug(spieler,istAmZug);
    }
    public void naechstePhase(){
        this.phase=(this.phase+1)%3;
    }

    public void naechsterZug() throws IOException {
        if(aktiverSpieler().isLandErobert()){
            aktiverSpieler().addEinheitskarten(kv.getZufälligeKarte());
        }
        sv.naechsterZug();
        aktiverSpieler().setLandErobert(false);
        List<Land> spielerLaender=landAusgabeVonSpieler(aktiverSpieler());
        for(Land land : spielerLaender){
            lv.resetBewegteTruppen(land);
        }
        aktiverSpieler().setEinheitenRunde(armeeVerteilung());
        naechstePhase();
        speichern();
    }
    public List<Spieler> getSpielerListe() {
        return sv.getSpielerListe();
    }
    public Spieler aktiverSpieler(){
      return sv.aktiverSpieler();
    }
    public int armeeVerteilung(){
     return sv.armeeVerteilung(this.lv);
    }
    public void laenderErstellung(){
      this.lv.laenderErstellung();
    }

    // Methode für Landverwaltung:
    public boolean angreifbar(int einheiten, Land angriffsLand, Land zielLand) throws ArmeeException, NachbarException {
        try {
            return lv.angreifbar(einheiten, angriffsLand, zielLand);
        } catch (ArmeeException | NachbarException e) {
            throw e;
        }
    }
    public boolean verteidigen(int einheiten, Land zielLand) throws ArmeeException {
        try{
            return lv.verteidigen(einheiten, zielLand);
        } catch (ArmeeException e) {
            throw e;
        }
    }
    
    public List<Integer> Kampf(Land angriffsLand, int angriff, Land zielLand, int verteidigung, List <Integer> angriffZahlen, List <Integer> verteidigungsZahlen) {
        List<Integer> temp = lv.Kampf(angriffsLand, angriff, zielLand, verteidigung,angriffZahlen,verteidigungsZahlen);
        if(zielLand.getErobert()){
            aktiverSpieler().setLandErobert(true);
        }
        return temp;
    }

    public void verschieben(Land quellLand, Land zielLand, int einheiten, boolean einrücken) throws ArmeeException,NachbarException,NotYourLandException{
      try {
          lv.verschieben(quellLand, zielLand, einheiten,einrücken);
      }catch(ArmeeException | NachbarException | NotYourLandException e){
          throw e;
      }
    }
    public void laenderVerteilung() {
        lv.laenderVerteilung(this.sv);
    }

    public Land idToLand(String id) throws IdException {

      try{
          return this.lv.idToLand(id);
      }catch(IdException e){
          throw e;
      }
    }

    public Landverwaltung getLandverwaltung() {
        return this.lv;
    }

    public int getArmee(Land land){
        return lv.getArmee(land);
    }

    public void setArmee(Land land, int i){
      lv.setArmee(land,i);
    }

    public List<Land> getLandListe() {
      return lv.getLandListe();
    }

    public List<Land> landAusgabeVonSpieler(Spieler spieler){
         return lv.landAusgabeVonSpieler(spieler);
    }

    //Methoden aus Kartenverwaltung
    public Kartenverwaltung getKartenverwaltung() { return this.kv; }

    public List<Einheitskarte> getEinheitskarten() { return kv.getEinheitskarten(); }

    public List<Missionskarten> getMissionskarten() { return kv.getMissionskarten(); }

    public void kartenErstellung(){ this.kv.kartenErstellung(sv.getSpielerListe()); }

    public Einheitskarte getZufälligeKarte(){ return kv.getZufälligeKarte(); }

    public boolean handkartenlimit(Spieler spieler){ return kv.handkartenlimit(spieler); }

    public void loserCheck(){
        this.sv.loserCheck();
    }

    public Einheitskarte idToKarte(String id) throws IdException, NotYourCardException {
        try {
            return this.kv.idToKarte(id,aktiverSpieler());
        } catch (NotYourCardException | IdException e) {
            throw e;
        }
    }


    public int einheitenVonKarten(int zähler){
        if(zähler == 0){
            return 4;
        } else if(zähler <4){
            return einheitenVonKarten(zähler-1)+2;
        } else if (zähler == 4) {
            return einheitenVonKarten(zähler-1)+3;
        } else {
            return einheitenVonKarten(zähler-1)+5;
        }
    }

    public void kartenLöschen(Einheitskarte karte1, Einheitskarte karte2, Einheitskarte karte3) {
        kv.getablageliste().add(karte1);
        kv.getablageliste().add(karte2);
        kv.getablageliste().add(karte3);
        aktiverSpieler().getEinheitskarten().remove(karte1);
        aktiverSpieler().getEinheitskarten().remove(karte2);
        aktiverSpieler().getEinheitskarten().remove(karte3);
    }

    public int kartenEinlösen(Einheitskarte karte1, Einheitskarte karte2, Einheitskarte karte3) throws NotYourCardException, SymbolException, DoppelteKarteException{
        try{
            sv.kartenCheck(aktiverSpieler(), karte1, karte2, karte3);
            List<Land> spielerLaender = landAusgabeVonSpieler(aktiverSpieler());
            if(spielerLaender.contains(karte1.getLand())) {
                karte1.getLand().setArmee(karte1.getLand().getArmee() + 2);
            }
            if(spielerLaender.contains(karte2.getLand())) {
                karte2.getLand().setArmee(karte2.getLand().getArmee() + 2);
            }
            if(spielerLaender.contains(karte3.getLand())) {
                karte3.getLand().setArmee(karte3.getLand().getArmee() + 2);
            }
            kartenLöschen(karte1, karte2, karte3);
            return einheitenVonKarten(kartenZähler++);
        }
        catch(NotYourCardException | SymbolException | DoppelteKarteException e){
            throw e;
        }
    }

    /**
     * Geht alle mission durch
     * @return
     */
    public String winnerCheck(){
    List<String> siegerNachrichten = new ArrayList<String>();
        for(Missionskarten mission:kv.getMissionskarten()){
            if(mission.istErfuellt()){
                siegerNachrichten.add(mission.siegerNachricht()) ;
            }
        }
        String winner = "" ;
        for( String nachricht:siegerNachrichten){
            winner += nachricht;
        }
        return winner;
    }

     /**
     *  Spieler gibt ID ein, um Land anzugreifen und hier wird geschaut, ob die ID (Land) nicht das eigene Land und ob überhaupt gültig ist.
     * @param id
     * @return
     * @throws IdException
     * @throws NotYourLandException
     */
    public boolean isIdGueltig(String id) throws  IdException,NotYourLandException{
        try{
            if (idToLand(id).getBesitzer()==aktiverSpieler()){
                return true;
            }else {
                throw new NotYourLandException();
            }
        }catch(IdException e) {
            throw e;
        }
    }

    /**
     *Spieler platziert Truppen in einem leeren Land zum Runden beginn.
     * @param einheiten
     * @param zuPlatzieren
     * @param id
     * @return
     * @throws ArmeeException
     */
    public int truppenPlatzieren(int einheiten,int zuPlatzieren,String id) throws ArmeeException, IdException {
        if(zuPlatzieren < 0){
            throw new ArmeeException("Die Zahl muss zwischen 0 und " + einheiten + " liegen!");
        }
        if (einheiten >= zuPlatzieren) {
            setArmee(idToLand(id), getArmee(idToLand(id)) + zuPlatzieren);
            return (einheiten - zuPlatzieren);
        } else {
            throw new ArmeeException("Es sind nur " + einheiten + " Einheiten übrig!");
        }
    }

    /**
     * Es wird überprüft, ob die eingegebene Spieleranzahl zwischen 1 und 6 liegt.
     * @param anzahlSpieler
     * @return
     * @throws SpielerAnzahlException
     */
    public boolean spielerZahl(int anzahlSpieler) throws SpielerAnzahlException {
        if(anzahlSpieler<7 && anzahlSpieler>1){
            return true;
        } else{
            throw new SpielerAnzahlException();
        }
    }

    /**
     * Es wird überprüft, ob es sich um das eigene Land handelt und ob man mit dem Land noch angreifen kann (hat genug Truppen & feindliches Nachbarland).
     * @param id
     * @return boolean
     * @throws IdException
     * @throws NotYourLandException
     * @throws ArmeeException
     */
    public boolean isIdYours(String id) throws IdException,NotYourLandException, ArmeeException, NoHostileNeighborException {
       try {
           if (idToLand(id).getBesitzer() == aktiverSpieler()) {
               if (idToLand(id).getArmee() > 1) {
                   boolean angreifbareLaender = false;
                   for(String land : idToLand(id).getNachbarLaender()) {
                        if (idToLand(land).getBesitzer()!=idToLand(id).getBesitzer()) {
                            angreifbareLaender=true;
                            break;
                        }
                   }
                   if (angreifbareLaender) {
                       return true;
                   }
                      throw new NoHostileNeighborException();


               } else {
                   throw new ArmeeException("Deine Armee ist zu klein, um einen Angriff zu starten!");
               }
           } else {
               throw new NotYourLandException();
           }
       }catch(IdException e){
           throw e;
       }
    }

    /**
     * Es wird überprüft, ob die eingegebene ID dem Gegner gehört und ein Nachbar der zweiten ID ist.
     * @param idZiel
     * @param id
     * @return boolean
     * @throws IdException
     * @throws YourLandException
     */
    public boolean isIdHostile(String idZiel,String id) throws IdException,YourLandException,NachbarException {
        try {
            if (idToLand(idZiel).getBesitzer() != aktiverSpieler()) {
                if(idToLand(idZiel).getNachbarLaender().contains(id)) {
                    return true;

                }else{
                    throw new NachbarException();
                }
            } else {
                throw new YourLandException();
            }
        }catch(IdException e){
            throw e;
        }
    }

    public void kontinentAktualisieren(){
        lv.kontinentAktualisieren();
    }

    public List <Integer> würfel(int anzahl){
        return this.lv.würfel(anzahl);
    }

    /**
     * Methode um Länder,Spieler und Kontinentlisten in txt datein zu speichern. Return true wenn erfolgreich gespeichert.
     * @return
     * @throws IOException
     */
    public boolean speichern() throws IOException {
        return (lv.speichereLand() && sv.speichereSpieler() && lv.speichereKontinentBesitzer()&& kv.speichereKarten(sv.getSpielerListe()));
    }

    /**
     * Methode um Länder, Spieler und Kontinentlisten aus txt datei zu laden. wirft keinSpeicherstandException, wenn nicht gespeichert wurde.
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws KeinSpeicherstandException
     */
    public void laden() throws IOException, ClassNotFoundException, KeinSpeicherstandException {
        sv.ladeSpieler();
        lv.ladeLand(sv.getSpielerListe());
        lv.ladeKontinentBesitzer(sv.getSpielerListe());
        kv.ladeKarten(sv.getSpielerListe());
    }

    public int getPhase() {
        return this.phase;
    }


}