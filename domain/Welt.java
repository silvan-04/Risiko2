package Risiko.domain;

import Risiko.domain.exceptions.*;
import Risiko.entities.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.Serial;
import java.io.Serializable;

public class Welt implements Serializable {
    private Spielerverwaltung sv;
    private Landverwaltung lv;
    private Kartenverwaltung kv;
    private int kartenZähler;
    private int phase;

    /**
     * Initialisiert Spieler-, Länder- und Kartenverwaltung; setzt Zähler und Phase auf 0.
     */
    public Welt() {
        this.sv = new Spielerverwaltung();
        this.lv = new Landverwaltung();
        this.kv = new Kartenverwaltung(this.lv);
        this.kartenZähler = 0;
        this.phase = 0;
    }


    /**
     * Fügt einen neuen Spieler mit dem angegebenen Namen hinzu.
     *
     * @param name   der gewünschte Spielername
     * @param online true = Online-Modus, false = Offline-Modus
     * @throws DoppelterNameException wenn der Name bereits vergeben ist
     */
    public void spielerHinzufuegen (String name,boolean online) throws DoppelterNameException {
            sv.spielerHinzufuegen(name,online);
    }

    /**
     * Gibt zurück, ob der Spieler mit dem angegebenen ID aktuell am Zug ist.
     *
     * @param spieler ID des Spielers in der Spielerliste
     * @return true, wenn dieser Spieler gerade am Zug ist
     */
    public boolean getAmZug(int spieler) {
        return sv.getAmZug(spieler);
    }

    /**
     * Legt fest, ob der Spieler mit dem angegebenen ID am Zug ist.
     *
     * @param spieler ID des Spielers in der Spielerliste
     * @param istAmZug true, wenn dieser Spieler am Zug sein soll
     */
    public void setAmZug(int spieler, boolean istAmZug) {
        sv.setAmZug(spieler,istAmZug);
    }

    /**
     * Wechselt zur nächsten Phase des Spiels.
     */
    public void naechstePhase(){
        this.phase=(this.phase+1)%3;
    }

    /**
     * Führt den Zugwechsel durch:
     * vergibt bei Eroberung eine Karte, wechselt den aktiven Spieler,
     * setzt Truppen- und Phasenstatus zurück und speichert das Spiel.
     *
     * @throws IOException wenn das Speichern fehlschlägt
     */
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

    /**
     * Liefert die aktuelle Liste aller Spieler.
     *
     * @return Liste der Spieler im Spiel
     */
    public List<Spieler> getSpielerListe() {
        return sv.getSpielerListe();
    }

    /**
     * Gibt den Spieler zurück, der aktuell am Zug ist.
     *
     * @return der aktive Spieler
     */
    public Spieler aktiverSpieler(){
      return sv.aktiverSpieler();
    }

    /**
     * Ermittelt die für den aktiven Spieler zu verteilenden Einheiten
     * basierend auf Länderzahl und Kontinentbonus.
     *
     * @return Anzahl der neuen Einheiten
     */
    public int armeeVerteilung(){
        System.out.println(sv.armeeVerteilung(this.lv));
     return sv.armeeVerteilung(this.lv);
    }

    /**
     * Initialisiert alle Länder in der Welt über die Länderverwaltung.
     */
    public void laenderErstellung(){
      this.lv.laenderErstellung();
    }

    /**
     * Prüft, ob ein Angriff mit der angegebenen Einheitenzahl von angriffsLand auf zielLand zulässig ist.
     *
     * @param einheiten    Anzahl der angreifenden Einheiten
     * @param angriffsLand das angreifende Land
     * @param zielLand     das verteidigende Land
     * @return true, wenn der Angriff erlaubt ist
     * @throws ArmeeException      wenn die Einheitenzahl ungültig ist
     * @throws NachbarException    wenn die Länder nicht benachbart sind
     */
    public boolean angreifbar(int einheiten, Land angriffsLand, Land zielLand) throws ArmeeException, NachbarException {
        try {
            return lv.angreifbar(einheiten, angriffsLand, zielLand);
        } catch (ArmeeException | NachbarException e) {
            throw e;
        }
    }

    /**
     * Überprüft, ob die angegebene Einheitenzahl zur Verteidigung des Ziel-Landes zulässig ist.
     *
     * @param einheiten     Anzahl der verteidigenden Einheiten
     * @param zielLand      das zu verteidigende Land
     * @return true, wenn die Verteidigung möglich ist
     * @throws ArmeeException   wenn die Einheitenzahl ungültig ist oder nicht ausreicht
     */
    public boolean verteidigen(int einheiten, Land zielLand) throws ArmeeException {
        try{
            return lv.verteidigen(einheiten, zielLand);
        } catch (ArmeeException e) {
            throw e;
        }
    }

    /**
     * Vergleicht Würfelergebnisse, paarweise (höchster gegen höchsten),
     * reduziert die jeweiligen Armeen, übernimmt bei vollständiger Vernichtung des Verteidigers das Ziel-Land.
     *
     * @param angriffsLand       Angreifer-Land
     * @param angriff            Truppenanzahl des Angreifers
     * @param zielLand           Verteidiger-Land
     * @param verteidigung       Truppenanzahl des Verteidigers
     * @param angriffZahlen      Würfelergebnisse des Angreifers
     * @param verteidigungsZahlen Würfelergebnisse des Verteidigers
     * @return Liste [verbleibende Angreifer, verbleibende Verteidiger]
     */
    public List<Integer> Kampf(Land angriffsLand, int angriff, Land zielLand, int verteidigung, List <Integer> angriffZahlen, List <Integer> verteidigungsZahlen) {
        List<Integer> temp = lv.Kampf(angriffsLand, angriff, zielLand, verteidigung,angriffZahlen,verteidigungsZahlen);
        if(zielLand.getErobert()){
            aktiverSpieler().setLandErobert(true);
        }
        return temp;
    }

    /**
     * Verschiebt eine bestimmte Anzahl von Einheiten von einem Land in ein anderes.
     * Leitet alle nötigen Prüfungen (Besitz, Mindesttruppen, Nachbar) an die Landverwaltung weiter.
     *
     * @param quellLand  das Land, aus dem die Einheiten abgezogen werden
     * @param zielLand   das Land, in das die Einheiten verschoben werden
     * @param einheiten  Anzahl der zu verschiebenden Einheiten
     * @param einrücken  true bei Rückführung nach Eroberung, false bei normaler Verschiebung
     * @throws ArmeeException       wenn die Einheitenzahl ungültig oder zu hoch ist
     * @throws NachbarException     wenn die beiden Länder nicht benachbart sind
     * @throws NotYourLandException wenn eines der Länder nicht dem aktiven Spieler gehört
     */
    public void verschieben(Land quellLand, Land zielLand, int einheiten, boolean einrücken) throws ArmeeException,NachbarException,NotYourLandException{
      try {
          lv.verschieben(quellLand, zielLand, einheiten,einrücken);
      }catch(ArmeeException | NachbarException | NotYourLandException e){
          throw e;
      }
    }

    /**
     * Verteilt alle Länder zufällig auf die in der Spielerverwaltung registrierten Spieler.
     */
    public void laenderVerteilung() {
        lv.laenderVerteilung(this.sv);
    }

    /**
     * Sucht und liefert das Land-Objekt mit der angegebenen ID.
     *
     * @param id von Land
     * @return das zugehörige Land-Objekt
     * @throws IdException wenn kein Land mit dieser ID gefunden wird
     */
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

    // Getter und Setter
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

    /**
     * Liefert die Einheitskarte des aktiven Spielers anhand der Länder-ID.
     *
     * @param id    von Land, von dem Einheitskarte gesucht wird
     * @return      die gefundene Einheitskarte
     * @throws IdException            wenn keine Karte mit dieser ID existiert
     * @throws NotYourCardException   wenn die Karte nicht im Besitz des Spielers ist
     */
    public Einheitskarte idToKarte(String id) throws IdException, NotYourCardException {
        try {
            return this.kv.idToKarte(id,aktiverSpieler());
        } catch (NotYourCardException | IdException e) {
            throw e;
        }
    }

    /**
     * Ermittelt die Bonustruppen für das nächste Karten-Einlösen:
     *
     * @param zähler Anzahl bereits eingelöster Karten
     * @return Bonustruppen für das (zähler+1) Einlösen
     */
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

    /**
     * Entfernt die angegebenen drei Einheitskarten aus dem Besitz des aktuellen Spielers
     * und legt sie auf den Ablagestapel der Kartenverwaltung.
     *
     * @param karte1 erste einzulösende Karte
     * @param karte2 zweite einzulösende Karte
     * @param karte3 dritte einzulösende Karte
     */
    public void kartenLöschen(Einheitskarte karte1, Einheitskarte karte2, Einheitskarte karte3) {
        kv.getablageliste().add(karte1);
        kv.getablageliste().add(karte2);
        kv.getablageliste().add(karte3);
        Einheitskarte remove1 = null;
        Einheitskarte remove2 = null;
        Einheitskarte remove3 = null;
        for(Einheitskarte karte: aktiverSpieler().getEinheitskarten()){
                if(karte.getLandToString().equals(karte1.getLandToString())){
                    remove1 = karte;
                }
                if(karte.getLandToString().equals(karte2.getLandToString())){
                    remove2 = karte;
                }
                if(karte.getLandToString().equals(karte3.getLandToString())){
                    remove3 = karte;
                }
        }
        aktiverSpieler().getEinheitskarten().remove(remove1);
        aktiverSpieler().getEinheitskarten().remove(remove2);
        aktiverSpieler().getEinheitskarten().remove(remove3);
    }

    /**
     * Tauscht drei Karten ein, verteilt Einheitenbonus und entfernt die Karten.
     *
     * @param karte1 erste Karte
     * @param karte2 zweite Karte
     * @param karte3 dritte Karte
     * @return Anzahl der erhaltenen Einheiten
     * @throws NotYourCardException   wenn eine Karte nicht zum Spieler gehört
     * @throws SymbolException        bei ungültiger Symbolkombination
     * @throws DoppelteKarteException wenn Karten doppelt gewählt wurden
     */
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
     * Prüft alle Missionskarten auf Erfüllung und gibt eine Siegesnachricht zurück.
     *
     * @return verkettete Siegesnachrichten (leer, falls keine Mission erfüllt)
     */
    public String winnerCheck(){
        List<String> siegerNachrichten = new ArrayList<String>();
        kontinentAktualisieren();
        for(Missionskarten mission:kv.getMissionskarten()){
            if(mission.istErfuellt()){
                siegerNachrichten.add(mission.siegerNachricht());
            }
        }
        String winner = "" ;
        for( String nachricht:siegerNachrichten){
            winner += nachricht;
        }
        return winner;
    }

    /**
     * Prüft, ob die angegebene Länder-ID einem eigenen Land des aktiven Spielers entspricht.
     *
     * @param id die ID des zu überprüfenden Landes
     * @return true, wenn das Land existiert und dem aktiven Spieler gehört
     * @throws IdException           wenn kein Land mit dieser ID gefunden wird
     * @throws NotYourLandException  wenn das Land nicht im Besitz des aktiven Spielers ist
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
     * Platziert eine Anzahl Truppen zu Rundenbeginn in das Land mit der angegebenen ID.
     *
     * @param einheiten    Gesamtzahl der verfügbaren Einheiten
     * @param zuPlatzieren Anzahl der einzusetzenden Einheiten
     * @param id           ID des Ziel-Landes
     * @return verbleibende Einheiten nach der Platzierung
     * @throws ArmeeException wenn zuPlatzieren negativ ist oder die Einheiten nicht ausreichen
     * @throws IdException    wenn kein Land mit der übergebenen ID existiert
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
     * Prüft, ob die angegebene Spielerzahl im gültigen Bereich von 2 bis 6 liegt.
     *
     * @param anzahlSpieler Anzahl der Spieler
     * @return true, wenn 2 ≤ anzahlSpieler ≤ 6
     * @throws SpielerAnzahlException wenn die Zahl außerhalb dieses Bereichs liegt
     */
    public boolean spielerZahl(int anzahlSpieler) throws SpielerAnzahlException {
        if(anzahlSpieler<7 && anzahlSpieler>1){
            return true;
        } else{
            throw new SpielerAnzahlException();
        }
    }

    /**
     * Prüft, ob das Land mit der angegebenen ID angreifbar ist-->gehört es dem aktiven Spieler,
     * verfügt es über mindestens 2 Einheiten, besitzt es mindestens ein feindliches Nachbarland.
     *
     * @param id ID des zu prüfenden Landes
     * @return true, wenn alle Bedingungen erfüllt sind
     * @throws IdException                wenn kein Land mit dieser ID existiert
     * @throws NotYourLandException       wenn das Land nicht dem aktiven Spieler gehört
     * @throws ArmeeException             wenn nicht genug Einheiten für einen Angriff vorhanden sind
     * @throws NoHostileNeighborException wenn kein feindliches Nachbarland zum Angreifen vorhanden ist
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
     * Überprüft, ob das Ziel-Land einem gegnerischen Spieler gehört und
     * benachbart zum angreifenden Land ist.
     *
     * @param idZiel ID des zu prüfenden Ziel-Land
     * @param id     ID des angreifenden Land
     * @return true, wenn idZiel nicht dem aktiven Spieler gehört und als Nachbar zu id gelistet ist
     * @throws IdException       wenn eines der Länder nicht existiert
     * @throws YourLandException wenn das Ziel-Land dem aktiven Spieler gehört
     * @throws NachbarException  wenn die beiden Länder nicht benachbart sind
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
     * Speichert Länder, Spieler und Kontinentbesitzer in jeweils eigenen Textdateien.
     *
     * @return true, wenn alle Speichervorgänge erfolgreich waren
     * @throws IOException bei einem Fehler beim Schreiben einer Datei
     */
    public boolean speichern() throws IOException {
        return (lv.speichereLand() && sv.speichereSpieler() && lv.speichereKontinentBesitzer()&& kv.speichereKarten(sv.getSpielerListe()));
    }

    /**
     * Lädt den gesamten Spielstand aus Persistenzdateien:
     * Spieler, Länder, Kontinentbesitzer und Kartenverwaltung werden reihenweise wiederhergestellt.
     *
     * @throws IOException                  bei I/O-Fehlern beim Datei zugriff
     * @throws ClassNotFoundException       wenn eine geladene Klasse nicht gefunden wird
     * @throws KeinSpeicherstandException   wenn kein gültiger Spielstand vorhanden ist
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