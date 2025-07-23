package Risiko.domain;

import Risiko.domain.exceptions.*;
import Risiko.entities.*;
import Risiko.persistence.FilePersistenceManager;

import java.util.*;
import java.io.*;

public class Landverwaltung implements Serializable {
    private List<Land> landListe= new ArrayList();
    private List<Spieler> asien=new ArrayList();
    private List<Spieler> europa=new ArrayList();
    private List<Spieler> afrika=new ArrayList();
    private List<Spieler> australien=new ArrayList();
    private List<Spieler> nordamerika=new ArrayList();
    private List<Spieler> suedamerika=new ArrayList();
    private FilePersistenceManager pm = new FilePersistenceManager();

    // getter
    public List <Land> getLandListe() { return landListe;}
    public List <Spieler> getAsien() { return asien;}
    public List <Spieler> getEuropa() { return europa; }
    public List <Spieler> getAfrika() { return afrika; }
    public List <Spieler> getAustralien() { return australien; }
    public List <Spieler> getNordamerika() { return nordamerika; }
    public List <Spieler> getSuedamerika() { return suedamerika; }

    public void resetBewegteTruppen(Land land) {
            land.setBewegteTruppen(-land.getBewegteTruppen());
    }

    /**
     * Erzeugt für jedes Land ein entsprechendes Land-Objekt, setzt dessen Nachbarländer
     * und fügt es der Landverwaltung hinzu.
     */
    public void laenderErstellung(){
        // Nordamerika
        landListe.add(new Land("Alaska", "ak"));
        landListe.get(0).setNachbarLaender(new ArrayList<>(Arrays.asList("nt", "ab", "ka")));

        landListe.add(new Land("Nordwest-Territorien", "nt"));
        landListe.get(1).setNachbarLaender(new ArrayList<>(Arrays.asList("ak", "gr", "on","ab")));

        landListe.add(new Land("Alberta", "ab"));
        landListe.get(2).setNachbarLaender(new ArrayList<>(Arrays.asList("ak", "unw", "on", "nt")));

        landListe.add(new Land("Ontario", "on"));
        landListe.get(3).setNachbarLaender(new ArrayList<>(Arrays.asList("ab", "unw", "une", "qb", "gr", "nt")));

        landListe.add(new Land("Quebec", "qb"));
        landListe.get(4).setNachbarLaender(new ArrayList<>(Arrays.asList("gr", "on", "une")));

        landListe.add(new Land("Grönland", "gr"));
        landListe.get(5).setNachbarLaender(new ArrayList<>(Arrays.asList("nt", "qb", "on","is")));

        landListe.add(new Land("Weststaaten", "unw"));
        landListe.get(6).setNachbarLaender(new ArrayList<>(Arrays.asList("ab", "on", "une", "cm")));

        landListe.add(new Land("Oststaaten", "une"));
        landListe.get(7).setNachbarLaender(new ArrayList<>(Arrays.asList("on", "unw", "qb", "cm")));

        landListe.add(new Land("Mittelamerika", "cm"));
        landListe.get(8).setNachbarLaender(new ArrayList<>(Arrays.asList("unw", "une", "ve")));

        // Südamerika
        landListe.add(new Land("Venezuela", "ve"));
        landListe.get(9).setNachbarLaender(new ArrayList<>(Arrays.asList("cm", "br", "pe")));

        landListe.add(new Land("Peru", "pe"));
        landListe.get(10).setNachbarLaender(new ArrayList<>(Arrays.asList("ve", "br", "ar")));

        landListe.add(new Land("Brasilien", "br"));
        landListe.get(11).setNachbarLaender(new ArrayList<>(Arrays.asList("ve", "pe", "ar", "an")));

        landListe.add(new Land("Argentinien", "ar"));
        landListe.get(12).setNachbarLaender(new ArrayList<>(Arrays.asList("br", "pe")));

        // Europa
        landListe.add(new Land("Island", "is"));
        landListe.get(13).setNachbarLaender(new ArrayList<>(Arrays.asList("sk", "gr", "gb")));

        landListe.add(new Land("Skandinavien", "sk"));
        landListe.get(14).setNachbarLaender(new ArrayList<>(Arrays.asList("is", "eun", "gb", "ua")));

        landListe.add(new Land("Großbritannien", "gb"));
        landListe.get(15).setNachbarLaender(new ArrayList<>(Arrays.asList("is", "sk", "eun", "euw")));

        landListe.add(new Land("Nordeuropa", "eun"));
        landListe.get(16).setNachbarLaender(new ArrayList<>(Arrays.asList("sk", "gb", "ua", "euw", "eus")));

        landListe.add(new Land("Westeuropa", "euw"));
        landListe.get(17).setNachbarLaender(new ArrayList<>(Arrays.asList("gb", "eun", "eus", "an")));

        landListe.add(new Land("Südeuropa", "eus"));
        landListe.get(18).setNachbarLaender(new ArrayList<>(Arrays.asList("euw", "eun", "ua", "eg", "an", "me")));

        landListe.add(new Land("Ukraine", "ua"));
        landListe.get(19).setNachbarLaender(new ArrayList<>(Arrays.asList("sk", "eun", "eus", "ur", "af", "me")));

        // Afrika
        landListe.add(new Land("Nordafrika", "an"));
        landListe.get(20).setNachbarLaender(new ArrayList<>(Arrays.asList("br", "euw", "eus", "eg", "ko", "ao")));

        landListe.add(new Land("Ägypten", "eg"));
        landListe.get(21).setNachbarLaender(new ArrayList<>(Arrays.asList("an", "ao", "eus", "me")));

        landListe.add(new Land("Ostafrika", "ao"));
        landListe.get(22).setNachbarLaender(new ArrayList<>(Arrays.asList("eg", "an", "ko", "as", "ma")));

        landListe.add(new Land("Kongo", "ko"));
        landListe.get(23).setNachbarLaender(new ArrayList<>(Arrays.asList("an", "ao", "as")));

        landListe.add(new Land("Südafrika", "as"));
        landListe.get(24).setNachbarLaender(new ArrayList<>(Arrays.asList("ko", "ao", "ma")));

        landListe.add(new Land("Madagaskar", "ma"));
        landListe.get(25).setNachbarLaender(new ArrayList<>(Arrays.asList("as", "ao")));

        // Asien
        landListe.add(new Land("Afghanistan", "af"));
        landListe.get(26).setNachbarLaender(new ArrayList<>(Arrays.asList("ua", "ur", "ch", "me", "in")));

        landListe.add(new Land("China", "ch"));
        landListe.get(27).setNachbarLaender(new ArrayList<>(Arrays.asList("ur", "mn", "mo", "aso", "in", "af")));

        landListe.add(new Land("Indien", "in"));
        landListe.get(28).setNachbarLaender(new ArrayList<>(Arrays.asList("me", "af", "ch", "aso")));

        landListe.add(new Land("Irkutsk", "ir"));
        landListe.get(29).setNachbarLaender(new ArrayList<>(Arrays.asList("mn", "ya", "ka", "mo")));

        landListe.add(new Land("Japan", "jp"));
        landListe.get(30).setNachbarLaender(new ArrayList<>(Arrays.asList("ka", "mo")));

        landListe.add(new Land("Kamtschatka", "ka"));
        landListe.get(31).setNachbarLaender(new ArrayList<>(Arrays.asList("ya", "ir", "mo", "jp")));

        landListe.add(new Land("Mongolei", "mo"));
        landListe.get(32).setNachbarLaender(new ArrayList<>(Arrays.asList("ch", "mn", "ir", "jp", "ka")));

        landListe.add(new Land("Mittlerer Osten", "me"));
        landListe.get(33).setNachbarLaender(new ArrayList<>(Arrays.asList("ua", "eus", "eg", "af", "in", "ao")));

        landListe.add(new Land("Sibirien", "mn"));
        landListe.get(34).setNachbarLaender(new ArrayList<>(Arrays.asList("ur", "mo", "ch", "ir", "ya")));

        landListe.add(new Land("Ural", "ur"));
        landListe.get(35).setNachbarLaender(new ArrayList<>(Arrays.asList("ua", "af", "ch", "mn")));

        landListe.add(new Land("Jakutien", "ya"));
        landListe.get(36).setNachbarLaender(new ArrayList<>(Arrays.asList("mn", "ir", "ka")));

        landListe.add(new Land("Südostasien", "aso"));
        landListe.get(37).setNachbarLaender(new ArrayList<>(Arrays.asList("ch", "in", "id")));

        // Australien
        landListe.add(new Land("Indonesien", "id"));
        landListe.get(38).setNachbarLaender(new ArrayList<>(Arrays.asList("aso", "ng", "auw")));

        landListe.add(new Land("Neuguinea", "ng"));
        landListe.get(39).setNachbarLaender(new ArrayList<>(Arrays.asList("id", "auw", "auo")));

        landListe.add(new Land("Westaustralien", "auw"));
        landListe.get(40).setNachbarLaender(new ArrayList<>(Arrays.asList("id", "ng", "auo")));

        landListe.add(new Land("Ostaustralien", "auo"));
        landListe.get(41).setNachbarLaender(new ArrayList<>(Arrays.asList("ng", "auw")));
    }

    /**
     * Methode die anhand id das Land objekt zurückgeben, kann null returnen wenn kein land diese id hat.
     * @param id
     * @return Land oder null
     */
    public Land idToLand(String id) throws IdException {
        for(int i =0;i<landListe.size();i++){
            if(landListe.get(i).getID().equals(id)){
                return landListe.get(i);
            }
        }
        throw new IdException();
    }

    public void setArmee(Land land, int i){
        land.setArmee(i);
    }

    public int getArmee(Land land) {
        return land.getArmee();
    }

    /**
     *  Abfrage ob ein Land angreifbar ist. entscheidend: eigene Truppenanzahl, ob Nachbarland und nicht eigenes.
     * @param einheiten
     * @param angriffsLand
     * @param zielLand
     * @return boolean
     */
    public boolean angreifbar(int einheiten, Land angriffsLand, Land zielLand) throws ArmeeException, NachbarException {
        if (angriffsLand.getNachbarLaender().contains(zielLand.getID()) && angriffsLand.getBesitzer()!= zielLand.getBesitzer() ) {
            if (einheiten > 0 && einheiten < 4 && (angriffsLand.getArmee() - einheiten) >= 1) {
                return true;
            } else if (einheiten <= 0) {
                throw new ArmeeException("Das sind nicht genug Einheiten!");
            } else if (einheiten >= 4) {
                throw new ArmeeException("Das sind zu viele Einheiten!");
            } else {
                throw new ArmeeException("Du hast dafür nicht genug Einheiten!");
            }
        }else if(!angriffsLand.getNachbarLaender().contains(zielLand.getID())){
            throw new NachbarException();
        } else  {
            return false;
        }
    }

    /**
     * Überprüft, ob die angegebene Anzahl an Einheiten zur Verteidigung des Ziel-Landes zulässig ist.
     *
     * @param einheiten  Anzahl der zum Verteidigen eingesetzten Einheiten
     * @param zielLand   das Land, das verteidigt werden soll
     * @return           true, wenn die Verteidigung mit der angegebenen Einheitenzahl möglich ist
     */
    public boolean verteidigen(int einheiten, Land zielLand) throws ArmeeException{
        if(einheiten<3 && einheiten>0){
            if (zielLand.getArmee()>=einheiten){
                return true;
            } else {
                throw new ArmeeException("Du hast nicht genug Einheiten!");
            }
        }else if(einheiten>=3){
            throw new ArmeeException("Du kannst nur mit maximal 2 Einheiten verteidigen!");
        }else{
            throw new ArmeeException("Du musst mit mindestens 1 Einheit verteidigen!");
        }
    }

    /**
     * Führt die angegebene Anzahl an Würfelwürfen (1–6) durch
     * und gibt die aufsteigend sortierte Liste der Ergebnisse zurück.
     *
     * @param wiederholungen Anzahl der Würfelwürfe
     * @return sortierte Liste der gewürfelten Augenzahlen (1–6)
     */
    public List<Integer> würfel(int wiederholungen){
        List<Integer> liste = new ArrayList<Integer>();
        Random rand = new Random();
        for(int i=0;i<wiederholungen;i++){
            liste.add(rand.nextInt(6) + 1);
        }
        Collections.sort(liste);
        return liste;
    }

    /**
     * Methode für Angriff auszuführen, würfelt für beide Teams und vergleicht größte, verringert Armee entsprechend,
     * setzt bei Erfolg neuen Besitzer und übrig gebliebene Truppen für das Zielland.
     * Nach dieser Methode muss verschoben werden, wenn vom Zielland erobert true ist (danach auf false setzen).
     * Returnt Liste mit zwei zahlen: index 0 sind übrige angriffs truppen, index 1 übrige verteidigungs truppen
     * @param angriffsLand        das angreifende Land
     * @param angriff             die Zahl der angreifenden Einheiten (und Würfelwürfe)
     * @param zielLand            das verteidigende Land
     * @param verteidigung        die Zahl der verteidigenden Einheiten (und Würfelwürfe)
     * @param angriffZahlen       Würfelergebnisse des Angreifers
     * @param verteidigungsZahlen Würfelergebnisse des Verteidigers
     * @return Liste mit zwei Werten:
     *         Index 0 = verbleibende Angreifer-Einheiten,
     *         Index 1 = verbleibende Verteidiger-Einheiten
     */
    public List<Integer> Kampf(Land angriffsLand, int angriff, Land zielLand, int verteidigung, List <Integer> angriffZahlen, List <Integer> verteidigungsZahlen){
        List<Integer> endTruppen= new ArrayList<Integer>();
        int k = verteidigung-1; // k sorgt dafür, dass man maximal so oft vergleicht wie es Verteidiger gibt
        for(int i=(angriff-1);i>=0;i--) { //i genauso nur für Angreifer
            if (angriffZahlen.get(i) > verteidigungsZahlen.get(k)) { //vergleicht jeweils höchsten Wurf
                zielLand.setArmee(zielLand.getArmee()-1);
                verteidigung--;
            }else {
                angriffsLand.setArmee(angriffsLand.getArmee() - 1);
                angriff--;
            }
            if(k==0){
                break;
            } else {
                k--;
            }
        }
        endTruppen.add(angriff);
        endTruppen.add(verteidigung);
        if(zielLand.getArmee() == 0){
            angriffsLand.getBesitzer().setAnzahlLaender(angriffsLand.getBesitzer().getAnzahlLaender()+1); //erhöht länderanzahl angreiffer
            zielLand.getBesitzer().setAnzahlLaender(zielLand.getBesitzer().getAnzahlLaender()-1); // verringert länderanzahl verteidiger
            zielLand.setBesitzer(angriffsLand.getBesitzer());
            zielLand.setArmee(angriff);
            angriffsLand.setArmee(angriffsLand.getArmee() - angriff);
            zielLand.setErobert(true);
        }
        return endTruppen;
    }

    /**
     * Methode um zu checken, ob verschieben von Einheiten möglich
     * checkt, ob das Land dem gleichen Spieler gehört, mindestens einer im Land bleibt,
     * Besitzer am Zug ist, und die Länder Nachbarländer sind.
     * @param quellLand das Land, aus dem die Truppen verschoben werden
     * @param zielLand  das Land, in das die Truppen verschoben werden
     * @param armee     die Anzahl der zu verschiebenden Einheiten
     * @param einrücken true, wenn es sich um ein Rückführungsmanöver handelt
     * @return true, wenn alle Prüfungen erfolgreich sind
     */
    public boolean verschiebenCheck(Land quellLand, Land zielLand, int armee, boolean einrücken) throws ArmeeException, NotYourLandException,NachbarException{
        if(armee < 0){
            throw new ArmeeException("Die Anzahl Einheiten darf nicht kleiner als 0 sein!");
        }
            if(!einrücken){
                    if (quellLand.getArmee() - quellLand.getBewegteTruppen()-armee > 0) {
                        if (quellLand.getBesitzer() == zielLand.getBesitzer()) {
                            if (quellLand.getBesitzer().getIstAmZug()) {
                                if (quellLand.getNachbarLaender().contains(zielLand.getID())) {
                                    return true;
                                } else {
                                    throw new NachbarException();
                                }
                            }
                            throw new NotYourLandException();
                        }
                        throw new NotYourLandException();
                    } else {
                        throw new ArmeeException("Nicht genug bewegbare Einheiten!");
                    }
            } else {
                if (quellLand.getArmee() - armee > 0) {
                    if (quellLand.getBesitzer() == zielLand.getBesitzer()) {
                        if (quellLand.getBesitzer().getIstAmZug()) {
                            if (quellLand.getNachbarLaender().contains(zielLand.getID())) {
                                return true;
                            } else {
                                throw new NachbarException();
                            }
                        }
                        throw new NotYourLandException();
                    }
                    throw new NotYourLandException();
                } else {
                    throw new ArmeeException("Nicht genug Einheiten!");
                }
        }

    }

    /**
     * Prüft und verschiebt die angegebene Anzahl Einheiten von quellLand zu zielLand.
     *
     * @param quellLand   Land der Abgabe
     * @param zielLand    Land der Aufnahme
     * @param einheiten   zu verschiebende Einheiten
     * @param einrücken   true für Rückführung
     */
    public void verschieben(Land quellLand, Land zielLand, int einheiten, boolean einrücken) throws ArmeeException,NotYourLandException,NachbarException{
        verschiebenCheck(quellLand, zielLand, einheiten, einrücken);
        quellLand.setArmee(quellLand.getArmee() - einheiten);
        zielLand.setArmee(zielLand.getArmee() + einheiten);
        zielLand.setBewegteTruppen(einheiten);
        if(quellLand.getBewegteTruppen() != 0) {
            quellLand.setBewegteTruppen(-einheiten);
        }
    }

    /**
     * Überprüft, ob jedes Land in der Liste einen Besitzer hat.
     *
     * @return true, wenn alle Länder vergeben sind, andernfalls false
     */
    public boolean laenderBesitzerCheck(){
        for (Land land : landListe) {
            if (land.getBesitzer() == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verteilt alle Länder zufällig auf die Spieler in sv und wiederholt die Verteilung,
     * bis jedes Land einen Besitzer hat.
     *
     * @param sv die Spielerverwaltung mit der aktuellen Liste aller Spieler
     */
    public void laenderVerteilung(Spielerverwaltung sv){
        Random rand = new Random();
        List<Integer> laenderZahlen=new ArrayList<Integer>();
        do {
            for(int i=0;i<42;i++){
                laenderZahlen.add(i);
            }
            for(int i=42;i>0;i--){
                int random = rand.nextInt(i);
                int zahl = laenderZahlen.get(random);
                laenderZahlen.remove(random);
                Spieler spieler = sv.getSpielerListe().get(i%(Spieler.getIdZaehler()));
                if (landListe.get(zahl).getBesitzer() == null) {
                    landListe.get(zahl).setBesitzer(spieler);
                    spieler.setAnzahlLaender(spieler.getAnzahlLaender() + 1);
                }
            }
        }
        while (!laenderBesitzerCheck());
    }

    /**
     * Prüft, ob alle Länder in Asien dem angegebenen Spieler gehören.
     *
     * @param spieler der zu prüfende Spieler
     * @return true, wenn jedes Land in Asien im Besitz des Spielers ist, sonst false
     */
    public boolean asienBesitzer(Spieler spieler){
        for (Spieler land : asien) {
            if (!(land.getName().equals(spieler.getName()))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Prüft, ob alle Länder in Europa dem angegebenen Spieler gehören.
     *
     * @param spieler der zu prüfende Spieler
     * @return true, wenn jedes Land in Europa im Besitz des Spielers ist, sonst false
     */
    public boolean europaBesitzer(Spieler spieler){
        for (Spieler land : europa) {
            if (!(land.getName().equals(spieler.getName()))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Prüft, ob alle Länder in Afrika dem angegebenen Spieler gehören.
     *
     * @param spieler der zu prüfende Spieler
     * @return true, wenn jedes Land in Afrika im Besitz des Spielers ist, sonst false
     */
    public boolean afrikaBesitzer(Spieler spieler){
        for (Spieler land : afrika) {
            if (!(land.getName().equals(spieler.getName()))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Prüft, ob alle Länder in Ausstralien dem angegebenen Spieler gehören.
     *
     * @param spieler der zu prüfende Spieler
     * @return true, wenn jedes Land in Ausstralien im Besitz des Spielers ist, sonst false
     */
    public boolean australienBesitzer(Spieler spieler){
        for (Spieler land : australien) {
            if (!(land.getName().equals(spieler.getName()))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Prüft, ob alle Länder in Nordamerika dem angegebenen Spieler gehören.
     *
     * @param spieler der zu prüfende Spieler
     * @return true, wenn jedes Land in Nordamerika im Besitz des Spielers ist, sonst false
     */
    public boolean nordamerikaBesitzer(Spieler spieler){
        for (Spieler land : nordamerika) {
            if (!(land.getName().equals(spieler.getName()))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Prüft, ob alle Länder in Suedamerika dem angegebenen Spieler gehören.
     *
     * @param spieler der zu prüfende Spieler
     * @return true, wenn jedes Land in Suedamerika im Besitz des Spielers ist, sonst false
     */
    public boolean suedamerikaBesitzer(Spieler spieler){
        for (Spieler land : suedamerika) {
            if (!(land.getName().equals(spieler.getName()))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Ermittelt alle Länder, die im Besitz des angegebenen Spielers sind.
     *
     * @param spieler dessen Länder abgefragt werden
     * @return Liste aller Länder, die der Spieler besitzt
     */
    public List<Land> landAusgabeVonSpieler(Spieler spieler) {
        List<Land> spielerLaender = new ArrayList<Land>();
        for (Land land : landListe) {
            if (land.getBesitzer().equals(spieler) ) {
                spielerLaender.add(land);
            }
        }
        return spielerLaender;
    }

    /**
     * Leert und befüllt die Besitzerlisten für alle Kontinente neu,
     * indem jeweils die aktuellen Eigentümer aus landListe übernommen werden.
     */
    public void kontinentAktualisieren(){
        asien.clear();
        nordamerika.clear();
        suedamerika.clear();
        europa.clear();
        afrika.clear();
        australien.clear();
        nordamerika.add(landListe.get(0).getBesitzer());
        nordamerika.add(landListe.get(1).getBesitzer());
        nordamerika.add(landListe.get(2).getBesitzer());
        nordamerika.add(landListe.get(3).getBesitzer());
        nordamerika.add(landListe.get(4).getBesitzer());
        nordamerika.add(landListe.get(5).getBesitzer());
        nordamerika.add(landListe.get(6).getBesitzer());
        nordamerika.add(landListe.get(7).getBesitzer());
        nordamerika.add(landListe.get(8).getBesitzer());
        suedamerika.add(landListe.get(9).getBesitzer());
        suedamerika.add(landListe.get(10).getBesitzer());
        suedamerika.add(landListe.get(11).getBesitzer());
        suedamerika.add(landListe.get(12).getBesitzer());
        europa.add(landListe.get(13).getBesitzer());
        europa.add(landListe.get(14).getBesitzer());
        europa.add(landListe.get(15).getBesitzer());
        europa.add(landListe.get(16).getBesitzer());
        europa.add(landListe.get(17).getBesitzer());
        europa.add(landListe.get(18).getBesitzer());
        europa.add(landListe.get(19).getBesitzer());
        afrika.add(landListe.get(20).getBesitzer());
        afrika.add(landListe.get(21).getBesitzer());
        afrika.add(landListe.get(22).getBesitzer());
        afrika.add(landListe.get(23).getBesitzer());
        afrika.add(landListe.get(24).getBesitzer());
        afrika.add(landListe.get(25).getBesitzer());
        asien.add(landListe.get(26).getBesitzer());
        asien.add(landListe.get(27).getBesitzer());
        asien.add(landListe.get(28).getBesitzer());
        asien.add(landListe.get(29).getBesitzer());
        asien.add(landListe.get(30).getBesitzer());
        asien.add(landListe.get(31).getBesitzer());
        asien.add(landListe.get(32).getBesitzer());
        asien.add(landListe.get(33).getBesitzer());
        asien.add(landListe.get(34).getBesitzer());
        asien.add(landListe.get(35).getBesitzer());
        asien.add(landListe.get(36).getBesitzer());
        asien.add(landListe.get(37).getBesitzer());
        australien.add(landListe.get(38).getBesitzer());
        australien.add(landListe.get(39).getBesitzer());
        australien.add(landListe.get(40).getBesitzer());
        australien.add(landListe.get(41).getBesitzer());
    }

    /**
     * Sichert alle Länder aus landListe in der Datei "persistence/Speicher/Landspeicher.txt".
     *
     * @return true, wenn alle Einträge erfolgreich geschrieben wurden
     * @throws IOException bei einem Schreibfehler in der Datei
     */
    public boolean speichereLand() throws IOException{
        pm.openForWriting("persistence/Speicher/Landspeicher.txt");
        boolean gespeichert = true;
        for (Land land : landListe) {
            if(!(pm.speichereLand(land))){
                gespeichert=false;
            }
        }
        pm.close();
        return gespeichert;
    }

    /**
     * Lädt alle Länder aus der Persistenzdatei „persistence/Speicher/Landspeicher.txt“
     * und befüllt die interne landListe, wobei jede geladene Land-ID anhand der übergebenen
     * spielerList seinem Besitzer zugeordnet wird.
     *
     * @param spielerList Liste der Spieler zur Zuordnung der Länderbesitzer
     * @throws IOException                  bei Lesefehlern der Datei
     * @throws KeinSpeicherstandException   wenn kein gültiger Speicherstand vorliegt
     */
    public void ladeLand(List <Spieler> spielerList) throws IOException, KeinSpeicherstandException {
        this.pm.openForReading("persistence/Speicher/Landspeicher.txt");
        try {
            for (int i = 0; i < 42; i++) {
                this.landListe.add(pm.ladeLand(spielerList));
            }
        }catch (KeinSpeicherstandException e){
            pm.close();
            throw new KeinSpeicherstandException();
        }
        pm.close();
    }

    /**
     * Speichert die Besitzer aller in der Datei "persistence/Speicher/KontinentBesitzer.txt".
     *
     * @return true, wenn alle Einträge erfolgreich geschrieben wurden
     * @throws IOException  bei einem Fehler beim Schreiben der Datei
     */
    public boolean speichereKontinentBesitzer() throws IOException{
        pm.openForWriting("persistence/Speicher/KontinentBesitzer.txt");
        boolean gespeichert=true;
        for (int i = 0; i < asien.size(); i++) {
            if(!(pm.speichereKontinentBesitzer(asien.get(i)))){
                gespeichert=false;
            }
        }
        for (int i = 0; i < europa.size(); i++) {
            if(!(pm.speichereKontinentBesitzer(europa.get(i)))){
                gespeichert=false;
            }
        }
        for (int i = 0; i < afrika.size(); i++) {
            if(!(pm.speichereKontinentBesitzer(afrika.get(i)))){
                gespeichert=false;
            }
        }
        for (int i = 0; i < australien.size(); i++) {
            if(!(pm.speichereKontinentBesitzer(australien.get(i)))){
                gespeichert=false;
            }
        }
        for (int i = 0; i < nordamerika.size(); i++) {
            if(!(pm.speichereKontinentBesitzer(nordamerika.get(i)))){
                gespeichert=false;
            }
        }
        for (int i = 0; i < suedamerika.size(); i++) {
            if(!(pm.speichereKontinentBesitzer(suedamerika.get(i)))){
                gespeichert=false;
            }
        }
        pm.close();
        return gespeichert;
    }

    /**
     * Lädt die Besitzer für alle Kontinente aus der Datei
     * "persistence/Speicher/KontinentBesitzer.txt" und befüllt die Listen der Kontinente entsprechend.
     *
     * @param spielerList Liste der Spieler zur Zuordnung der geladenen Besitzer
     * @throws IOException               bei Lesefehlern der Persistenzdatei
     * @throws KeinSpeicherstandException wenn kein gültiger Speicherstand vorliegt
     */
    public void ladeKontinentBesitzer(List<Spieler> spielerList) throws IOException, KeinSpeicherstandException {
        pm.openForReading("persistence/Speicher/KontinentBesitzer.txt");
        try {
            for (int i = 0; i < 12; i++) {
                asien.add(pm.ladeKontinentBesitzer(spielerList));
            }
            for (int i = 0; i < 7; i++) {
                europa.add(pm.ladeKontinentBesitzer(spielerList));
            }
            for (int i = 0; i < 6; i++) {
                afrika.add(pm.ladeKontinentBesitzer(spielerList));
            }
            for (int i = 0; i < 4; i++) {
                australien.add(pm.ladeKontinentBesitzer(spielerList));
            }
            for (int i = 0; i < 9; i++) {
                nordamerika.add(pm.ladeKontinentBesitzer(spielerList));
            }
            for (int i = 0; i < 4; i++) {
                suedamerika.add(pm.ladeKontinentBesitzer(spielerList));
            }
        }catch(KeinSpeicherstandException e) {
            pm.close();
            throw e;
        }
        pm.close();
    }


    // ab hier Getter für MissionKontinent------------------------------------------------------------------

    public List<Land> getNordamerikaLaender() {
        List<Land> result = new ArrayList<>();
        for (Land land : landListe) {
            String id = land.getID();
            if (Arrays.asList("ak","nt","ab","on","qb","gr","unw","une","cm").contains(id)) {
                result.add(land);
            }
        }
        return result;
    }

    public List<Land> getSuedamerikaLaender() {
        List<Land> result = new ArrayList<>();
        for (Land land : landListe) {
            String id = land.getID();
            if (Arrays.asList("ve","pe","br","ar").contains(id)) {
                result.add(land);
            }
        }
        return result;
    }

    public List<Land> getEuropaLaender() {
        List<Land> result = new ArrayList<>();
        for (Land land : landListe) {
            String id = land.getID();
            if (Arrays.asList("is","sk","gb","eun","euw","eus","ua").contains(id)) {
                result.add(land);
            }
        }
        return result;
    }

    public List<Land> getAfrikaLaender() {
        List<Land> result = new ArrayList<>();
        for (Land land : landListe) {
            String id = land.getID();
            if (Arrays.asList("an","eg","ao","ko","as","ma").contains(id)) {
                result.add(land);
            }
        }
        return result;
    }

    public List<Land> getAsienLaender() {
        List<Land> result = new ArrayList<>();
        for (Land land : landListe) {
            String id = land.getID();
            if (Arrays.asList("af","ch","in","ir","jp","ka","mo","me","mn","ur","ya","aso").contains(id)) {
                result.add(land);
            }
        }
        return result;
    }

    public List<Land> getAustralienLaender() {
        List<Land> result = new ArrayList<>();
        for (Land land : landListe) {
            String id = land.getID();
            if (Arrays.asList("id","ng","auw","auo").contains(id)) {
                result.add(land);
            }
        }
        return result;
    }
}
