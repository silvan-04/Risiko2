package Risiko.ui;

import Risiko.domain.*;
import Risiko.domain.exceptions.*;
import Risiko.entities.*;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
//Im Angriff geht das beenden nicht, da "input" nicht verändert wird in der funktion. Muss umgeschrieben werden und input benutzen (Maybe Spielerauswahl auch)

public class RisikoClientCUI {
   private Welt welt;
   private BufferedReader in;

    /**
     * Erzeugt einen neuen Konsolen-Client:
     * initialisiert die Spielwelt und richtet den Eingabe-Reader für System.in ein.
     *
     * @throws IOException falls die Standard-Eingabe nicht geöffnet werden kann
     */
   public RisikoClientCUI() throws IOException {
       this.welt=new Welt();
       this.in=new BufferedReader(new InputStreamReader(System.in));
   }

    /**
     * Fragt die Anzahl der Mitspieler ab und liest anschließend die Spielernamen ein.
     * Legt für jeden Spieler eine neue Instanz in der Spielwelt an und setzt
     * danach den ersten Spieler auf „ist am Zug“.
     *
     * @throws IOException wenn die Konsoleneingabe fehlschlägt
     */
   private void SpielerAuswahl() throws IOException {
       boolean gueltigeZahl=false;
       int spielerZahl = 0;
       do {
           System.out.println("Wie viele Spieler?(2-6)");
           try{
                spielerZahl = Integer.parseInt(liesEingabe());
                gueltigeZahl = welt.spielerZahl(spielerZahl);
           } catch (SpielerAnzahlException e) {
               System.out.println(e.getMessage());
           }catch(NumberFormatException e){
               System.out.println("Es sind nur ZAHLEN zwischen 2 und 6 gültig!");
           }
       }
       while (!gueltigeZahl);
       for (int i = 0; i < spielerZahl; i++) {
           System.out.println("Spieler Namen eingeben (mit Enter bestätigen):");
           boolean nameGueltig=false;
           do{
               try{
                    welt.spielerHinzufuegen(liesEingabe(), false);
                    nameGueltig=true;
               }catch(DoppelterNameException e){
                   System.out.println(e.getMessage());
               }
           } while (!nameGueltig);
           welt.setAmZug(0,true); //setz spieler 1 am zug damit es anfängt
           System.out.println(welt.getSpielerListe().get(i));

       }
   }

    /**
     * Führt die Truppenaufstellung zu Beginn einer Runde durch.
     *
     * Wenn der aktuelle Spieler das Handkartenlimit erreicht hat,
     * werden seine Karten gegen zusätzliche Einheiten eingetauscht.
     * Anschließend (oder falls kein Kartentausch nötig ist) werden
     * ihm neue Armeen zugewiesen und er kann sie beliebig auf seine Länder
     * verteilen, bis keine Einheiten mehr übrig sind.
     *
     * @throws IOException falls die Konsoleneingabe fehlschlägt
     */
   public void armeeRundenanfang()throws IOException {
       if (welt.handkartenlimit(welt.aktiverSpieler())) {
           armeeMitKarten();
       } else {
           int einheiten = welt.armeeVerteilung();
           do {
               System.out.println("Du kannst " + einheiten + " Einheiten verteilen.");
               boolean idGueltig = false;
               String id = null;
               do {
                   System.out.println("Id deines Ziellands:");
                   id = liesEingabe();
                   try {
                       idGueltig = welt.isIdGueltig(id);
                   } catch (IdException | NotYourLandException e) {
                       System.out.println(e.getMessage());
                   }

               }
               while (!idGueltig);
               boolean einheitenGültig = false;
               do {
                   try {
                       System.out.println("Zahl der Einheiten:");
                       int verschobenEinheiten = Integer.parseInt(liesEingabe());
                       try {
                           einheiten = welt.truppenPlatzieren(einheiten, verschobenEinheiten, id);
                           System.out.println("Das Land " + id + " wurde um " + verschobenEinheiten + " verstärkt.");
                           System.out.println(welt.idToLand(id));
                           einheitenGültig = true;
                       } catch (ArmeeException | IdException e) {
                           System.out.println(e.getMessage());
                       }
                   } catch (NumberFormatException e) {
                       System.out.println("Es muss ein Zahl für die Einheiten eingegeben werden!");
                   }
               }
               while (!einheitenGültig);
           }
           while (einheiten > 0);

       }
   }

    /**
     * Löst Karten gegen zusätzliche Armeen ein und verteilt diese.
     *
     * Der Spieler kann drei gültige Einheitskarten einlösen, um zusätzliche
     * Einheiten zu erhalten. Mit "q" kann er den Vorgang abbrechen und nur
     * seine regulären Armeen verteilen. Anschließend verteilt er die so
     * erhaltenen Einheiten wieder auf seine Länder, bis keine mehr übrig sind.
     *
     * @throws IOException falls die Konsoleneingabe fehlschlägt
     */
    public void armeeMitKarten()throws IOException{
       boolean idGueltig=false;
       boolean kartenGueltig=false;
       String id=null;
       Einheitskarte karte1 = null;
       Einheitskarte karte2 = null;
       Einheitskarte karte3 = null;
       int einheiten = 0;
       do {
           do {
               System.out.println("Um das Einlösen abzubrechen \"q\" eingeben!");
               System.out.println("ID der ersten Karte:");
               id = liesEingabe();
               if(id.equals("q")){
                   break;
               }
               try {
                   karte1 = welt.idToKarte(id);
                   idGueltig = true;
               } catch (IdException | NotYourCardException e) {
                   System.out.println(e.getMessage());
               }

           } while (!idGueltig);

           do {
               if(id.equals("q")){
                    break;
                }
               idGueltig = false;
               System.out.println("Um das Einlösen abzubrechen \"q\" eingeben!");
               System.out.println("ID der zweiten Karte:");
               id = liesEingabe();
               if(id.equals("q")){
                   break;
               }
               try {
                   karte2 = welt.idToKarte(id);
                   idGueltig = true;
               } catch (IdException | NotYourCardException e) {
                   System.out.println(e.getMessage());
               }

           } while (!idGueltig);

           do {
               if(id.equals("q")){
                   break;
               }
               idGueltig = false;
               System.out.println("Um das Einlösen abzubrechen \"q\" eingeben!");
               System.out.println("ID der dritten Karte:");
               id = liesEingabe();
               if(id.equals("q")){
                   break;
               }
               try {
                   karte3 = welt.idToKarte(id);
                   idGueltig = true;
               } catch (IdException | NotYourCardException e) {
                   System.out.println(e.getMessage());
               }

           } while (!idGueltig);
           if(id.equals("q")){
               einheiten = welt.armeeVerteilung();
               break;
           }
           try {
               einheiten = welt.armeeVerteilung() + welt.kartenEinlösen(karte1, karte2, karte3);
               kartenGueltig=true;
           } catch (NotYourCardException | SymbolException | DoppelteKarteException e) {
               System.out.println(e.getMessage());
           }
       } while(!kartenGueltig);

        do {
            System.out.println("Du kannst " + einheiten + " Einheiten verteilen.");
            do {
                idGueltig = false;
                System.out.println("ID deines Ziellands:");
                id = liesEingabe();
                try {
                    idGueltig = welt.isIdGueltig(id);
                }catch(IdException | NotYourLandException e){
                    System.out.println(e.getMessage());
                }
            }
            while(!idGueltig);
            boolean einheitenGültig=false;
            do {
                try {
                    System.out.println("Zahl der Einheiten:");
                    int verschobenEinheiten = Integer.parseInt(liesEingabe());
                    try {
                        einheiten = welt.truppenPlatzieren(einheiten,verschobenEinheiten,id);
                        System.out.println("Das Land " + id + " wurde um " + verschobenEinheiten + " verstärkt.");
                        System.out.println(welt.idToLand(id));
                        einheitenGültig = true;
                    } catch (ArmeeException | IdException e){
                        System.out.println(e.getMessage());
                    }
                }catch(NumberFormatException e){
                    System.out.println("Es muss ein Zahl für die Einheiten eingegeben werden!");
                }
            }
            while(!einheitenGültig);
        }
        while(einheiten>0);
    }

    /**
     * Führt einen vollständigen Angriffsvorgang in der Konsole aus:
     * 1. Auswahl des angreifenden eigenen Landes
     * 2. Auswahl eines feindlichen Nachbarlandes
     * 3. Eingabe der Angreifer-Einheiten und Validierung
     * 4. Eingabe der Verteidiger-Einheiten durch den verteidigenden Spieler
     * 5. Würfeln beider Seiten und Auswertung des Kampfergebnisses
     * 6. Bei Eroberung: optionales Verschieben zusätzlicher Truppen ins eroberte Land
     *
     * @throws IOException wenn die Konsoleneingabe fehlschlägt
     */
    public void ganzerAngriff()throws IOException{
       boolean gueltigeID=false;
       String id=null;
       do {
           System.out.println("Gib die Id deines Landes ein, welches in den Angriff gehen soll!");
            id=liesEingabe();
           try{
               gueltigeID=welt.isIdYours(id);
           }catch(IdException | NotYourLandException | ArmeeException | NoHostileNeighborException e){
               System.out.println(e.getMessage());
           }
       }
       while(!gueltigeID);

       boolean gueltigeIDziel=false;
       String idZiel=null;
       do {
           System.out.println("Gib die ID deines Ziellands ein!");
           idZiel=liesEingabe();
           try {
               gueltigeIDziel = welt.isIdHostile(idZiel,id);
           }catch (IdException | YourLandException | NachbarException e){
               System.out.println(e.getMessage());
           }
       }
       while(!gueltigeIDziel);

        boolean angreifbar=false;
        int einheiten=0;
        do {
            System.out.println("Gib die Anzahl der einheiten ein, welche angreifen sollen! Mögliche Einheiten:");
            if(welt.idToLand(id).getArmee()>3){
                System.out.println("Min:1 Max:3 Einheiten.");
            } else if (welt.idToLand(id).getArmee()>2) {
                System.out.println("Min:1 Max:2 Einheiten.");
            }else if (welt.idToLand(id).getArmee()>1) {
                System.out.println("Min:1 Max:1 Einheiten.");
            }
            try {
                einheiten = Integer.parseInt(liesEingabe());
                angreifbar=welt.angreifbar(einheiten,welt.idToLand(id),welt.idToLand(idZiel));
            } catch (NumberFormatException e) {
                System.out.println("Es muss eine Zahl eingegeben werden!");
            }catch(ArmeeException | NachbarException e) {
                System.out.println(e.getMessage());
            }
        }while(!angreifbar);

        System.out.println(welt.idToLand(idZiel).getBesitzer().getName() + " dein Land "+ welt.idToLand(idZiel).getName()+", mit "+welt.idToLand(idZiel).getArmee()+" Einheiten, wird angegriffen! Gib die Zahl der Einheiten zur Verteidigung ein!");
        boolean verteidigung=false;
        int verteidigen=0;
        do{
            if(welt.idToLand(idZiel).getArmee()>1){
                       System.out.println("Min:1 Max:2 Einheiten.");
                   }else{
                       System.out.println("Min:1 Max:1 Einheiten.");
                   }
            try{
                verteidigen=Integer.parseInt(liesEingabe());
                verteidigung=welt.verteidigen(verteidigen,welt.idToLand(idZiel));
            }catch(NumberFormatException e) {
                System.out.println("Es muss eine Zahl eingegeben werden!");
            }catch(ArmeeException e){
                System.out.println(e.getMessage());
            }
        }while(!verteidigung);

        List <Integer> angriffZahlen=welt.würfel(einheiten);
        List <Integer> verteidigungsZahlen=welt.würfel(verteidigen);
        System.out.println(welt.aktiverSpieler().getName()+" würfelt:"+angriffZahlen);
        System.out.println(welt.idToLand(idZiel).getBesitzer().getName()+" würfelt:"+verteidigungsZahlen);
        List<Integer> endTruppen=welt.Kampf(welt.idToLand(id),einheiten,welt.idToLand(idZiel),verteidigen,angriffZahlen,verteidigungsZahlen);
        if(welt.idToLand(idZiel).getErobert()){
            welt.idToLand(idZiel).setErobert(false);

            boolean fehler=true;
            do {
                System.out.println("Sie haben das Land eingenommen und dabei " +(einheiten-endTruppen.getFirst())+" verloren. Wie viele Einheiten sollen zusätzlich in das eroberte Land?");
                System.out.println("Gib eine Zahl zwischen 0 und "+ (welt.idToLand(id).getArmee()-1)+" ein:");
                try {
                    int verschieben = Integer.parseInt(liesEingabe());
                    welt.verschieben(welt.idToLand(id), welt.idToLand(idZiel), verschieben,true);
                    System.out.println(welt.idToLand(id));
                    System.out.println(welt.idToLand(idZiel));
                    fehler = false;
                } catch (ArmeeException | NachbarException | NotYourLandException e) {
                    System.out.println(e.getMessage());
                } catch (NumberFormatException e) {
                    System.out.println("Eingabe muss eine ganze Zahl sein!");
                }
            }while(fehler);
        }else {
            System.out.println(welt.idToLand(idZiel).getBesitzer().getName()+" hat erfolgreich " + welt.idToLand(idZiel).getName() + " verteidigt. Sie haben " + (verteidigen-endTruppen.get(1))+" Es haben " +welt.idToLand(idZiel).getArmee() + " Einheiten überlebt.");
            System.out.println(welt.idToLand(id).getBesitzer().getName()+ " hat "+ (einheiten-endTruppen.getFirst()) + " Einheiten verloren!");
        }
   }

    /**
     * Liest eine Eingabezeile von der Konsole ein.
     *
     * @return der eingegebene Text
     * @throws IOException falls ein Fehler beim Lesen aus dem Input-Stream auftritt
     */
    private String liesEingabe() throws IOException {
        return in.readLine();
    }

    /**
     * Gibt in der Konsole die verfügbaren Befehle für die erste Spielphase aus.
     */
    private void erstePhaseAusgabe() {
       System.out.print("Befehle: \n INFO: eigene Länder mit Einheiten 'e'");
       System.out.print("         \n INFO: gegnerische Länder mit Einheiten 'g'");
       System.out.print("         \n INFO: Angreifbare Nachbarländer 'n'");
       System.out.print("         \n INFO: Mission anzeigen 'm'");
       System.out.print("         \n INFO: Einheitskarten anzeigen 'k'");
        System.out.print("         \n Löse Karten ein 'c'");
       System.out.print("         \n Verteile Truppen 'v'");
       System.out.print("         \n  ---------------------");
       System.out.println("         \n  Beenden:        'q'");
       System.out.print("         \n ");
    }

    /**
     * Gibt in der Konsole die verfügbaren Befehle für die zweite Spielphase aus.
     */
    private void zweitePhaseAusgabe() {
        System.out.print("Befehle: \n INFO: eigene Länder mit Einheiten 'e'");
        System.out.print("         \n INFO: gegnerische Länder mit Einheiten 'g'");
        System.out.print("         \n INFO: Angreifbare Nachbarländer 'n'");
        System.out.print("         \n INFO: Mission anzeigen 'm'");
        System.out.print("         \n INFO: Einheitskarten anzeigen 'k'");
        System.out.print("         \n Angreifen 'v'");
        System.out.print("         \n Nächste Phase 'p'");
        System.out.print("         \n  ---------------------");
        System.out.println("         \n  Beenden:        'q'");
        System.out.print("         \n ");

    }

    /**
     * Gibt in der Konsole die verfügbaren Befehle für die dritte Spielphase aus.
     */
    private void drittePhaseAusgabe() {
        System.out.print("Befehle: \n INFO: eigene Länder mit Einheiten 'e'");
        System.out.print("         \n INFO: gegnerische Länder mit Einheiten 'g'");
        System.out.print("         \n INFO: Angreifbare Nachbarländer 'n'");
        System.out.print("         \n INFO: Mission anzeigen 'm'");
        System.out.print("         \n INFO: Einheitskarten anzeigen 'k'");
        System.out.print("         \n Einheiten verschieben 'v'");
        System.out.print("         \n Nächster Spieler 'p'");
        System.out.print("         \n Spiel speichern 's'");
        System.out.print("         \n  ---------------------");
        System.out.println("         \n  Beenden:        'q'");
        System.out.print("         \n ");

    }

    /**
     * Verarbeitet die Konsoleneingabe in Phase 1 und führt die jeweils
     * gewählte Aktion aus:
     * – Listet alle eigenen Länder mit ihren Einheiten auf.
     * – Listet alle Länder auf der Karte mit ihren Einheiten auf.
     * – Zeigt für jedes eigene Land seine feindlichen Nachbarn an.
     * – Gibt die aktuelle Missionsbeschreibung aus.
     * – Zeigt die Einheitenkarten des aktiven Spielers an.
     * – Startet die Phase zur Verteilung neuer Truppen.
     * – Startet die Karteneinlöse-Phase.
     * @param line der eingegebene Befehl (z. B. "e", "g", "n", "m", "k", "v" oder "c")
     * @throws IOException falls bei der Konsoleneingabe ein Fehler auftritt
     */
    private void verarbeiteEingabeEins(String line) throws IOException {
        List<Land> spielerLaender=welt.landAusgabeVonSpieler(welt.aktiverSpieler());
        switch (line) {
            case "e":
                for(Land land : spielerLaender ){
                    System.out.println(land);
                }
                break;
            case "g":
                for (Land land : welt.getLandListe()) {
                    System.out.println(land);
                }
                break;
            case "n":
                for(Land land : spielerLaender ){
                    List<String> nachbarn=land.getNachbarLaender();
                    System.out.println("Nachbarn von "+land.getName()+": \""+land.getID()+"\" "+land.getArmee()+" Einheiten:");
                    for(String nachbar : nachbarn){
                        if(land.getBesitzer() != welt.idToLand(nachbar).getBesitzer()) {
                            System.out.println(welt.idToLand(nachbar));
                        }
                }
                }
                break;
            case "m":
                System.out.println(welt.getKartenverwaltung().getMissionskarten().get(welt.aktiverSpieler().getId()).beschreibung());
                break;
            case "k":
                System.out.println(welt.aktiverSpieler().getEinheitskarten());
                break;
            case "v":
                armeeRundenanfang();
                break;
            case "c":
                armeeMitKarten();
                break;
        }
    }

    /**
     * Verarbeitet die Konsoleneingabe in Phase 2 (Angriff) und führt
     * die jeweils gewählte Aktion aus:
     * – Listet alle eigenen Länder mit ihren Einheiten auf.
     * – Listet sämtliche Länder der Karte mit ihren Einheiten auf.
     * – Zeigt für jedes eigene Land seine feindlichen Nachbarn an.
     * – Gibt die Missionsbeschreibung des aktiven Spielers aus.
     * – Zeigt die Einheitskarten des aktiven Spielers an.
     * – Startet den kompletten Angriffsvorgang
     * @param line der eingegebene Befehl: "e", "g", "n", "m", "k" oder "v"
     * @throws IOException falls bei der Konsoleneingabe ein I/O-Fehler auftritt
     */
    private void verarbeiteEingabeZwei(String line) throws IOException {
        List<Land> spielerLaender=welt.landAusgabeVonSpieler(welt.aktiverSpieler());
       switch (line) {
           case "e":
               for(Land land : spielerLaender ){
                   System.out.println(land);
               }
               break;
           case "g":
                for (Land land : welt.getLandListe()) {
                    System.out.println(land);
                }
               break;
           case "n":
               for(Land land : spielerLaender){
                   List<String> nachbarn=land.getNachbarLaender();
                   System.out.println("Nachbarn von "+land.getName()+": \""+land.getID()+"\" "+land.getArmee()+" Einheiten:");
                   for(String nachbar : nachbarn){
                        if(land.getBesitzer() != welt.idToLand(nachbar).getBesitzer()) {
                            System.out.println(welt.idToLand(nachbar));
                        }
                   }
               }
               break;
           case "m":
               System.out.println(welt.getKartenverwaltung().getMissionskarten().get(welt.aktiverSpieler().getId()).beschreibung());
               break;
           case "k":
               System.out.println(welt.aktiverSpieler().getEinheitskarten());
               break;
           case "v":
               ganzerAngriff();
               break;
       }
    }

    /**
     * Verarbeitet die Konsoleneingabe in Phase 3 (Truppenverschiebung) und führt
     * die jeweils gewählte Aktion aus:
     * – Listet alle eigenen Länder mit ihren Einheiten auf.
     * – Listet sämtliche Länder der Karte mit ihren Einheiten auf.
     * – Zeigt für jedes eigene Land seine feindlichen Nachbarn an.
     * – Gibt die Missionsbeschreibung des aktiven Spielers aus.
     * – Zeigt die Einheitenkarten des aktiven Spielers an.
     * – Ermöglicht das Verschieben von Truppen zwischen zwei Ländern.
     * @param line der eingegebene Befehl
     * @throws IOException falls bei der Konsoleneingabe ein I/O-Fehler auftritt
     */
    private void verarbeiteEingabeDrei(String line) throws IOException {
        List<Land> spielerLaender=welt.landAusgabeVonSpieler(welt.aktiverSpieler());
       switch (line) {
            case "e":
                for(Land land : spielerLaender ){
                    System.out.println(land);
                }
                break;
            case "g":
                for (Land land : welt.getLandListe()) {
                    System.out.println(land);
                }
                break;
            case "n":
                for(Land land : spielerLaender ){
                    List<String> nachbarn=land.getNachbarLaender();
                    System.out.println("Nachbarn von "+land.getName()+": \""+land.getID()+"\" "+land.getArmee()+" Einheiten:");
                    for(String nachbar : nachbarn){
                        if(land.getBesitzer() != welt.idToLand(nachbar).getBesitzer()) {
                            System.out.println(welt.idToLand(nachbar));
                        }
                    }
                }

                break;
           case "m":
               System.out.println(welt.getKartenverwaltung().getMissionskarten().get(welt.aktiverSpieler().getId()).beschreibung());
               break;
           case "k":
               System.out.println(welt.aktiverSpieler().getEinheitskarten());
               break;
            case "v":
                   try {
                       System.out.println("Du kannst nun Truppen verschieben!");
                       System.out.println("Gib die ID des Landes mit den Truppen an:");
                       String id=liesEingabe();
                       System.out.println("Gib das Zielland deiner Truppen an:");
                       String idZiel=liesEingabe();
                       System.out.println("Gib an wie viele Truppen du verschieben möchtest:");
                       int verschieben = Integer.parseInt(liesEingabe());
                       welt.verschieben(welt.idToLand(id), welt.idToLand(idZiel), verschieben,false);
                       System.out.println(welt.idToLand(id));
                       System.out.println(welt.idToLand(idZiel));
                   } catch (ArmeeException | NachbarException | NotYourLandException | IdException e) {
                       System.out.println(e.getMessage());
                   } catch (NumberFormatException e) {
                       System.out.println("Eingabe muss eine ganze Zahl sein!");
                   }
                break;
        }
    }

    /**
     * Startet die Hauptschleife des Konsolen-Clients und steuert
     * den Ablauf von Spielstart, Laden und den drei Spielphasen.
     * Ablauf:
     * Abfrage, ob ein neues Spiel gestartet oder ein gespeichertes Spiel geladen,
     * Initialisierung der Welt (Länder erzeugen, verteilen, Karten erstellen, Kontinente aktualisieren)
     * Rundenweise Ablauf der drei Phasen:
     * Phase 1: Einheitenverteilung
     * Phase 2: Angriff
     * Phase 3: Truppenverschiebung
     * In jeder Phase werden über eine Unterschleife
     * Befehle eingelesen und mit den Methoden
     *
     * Am Ende jeder Runde: Überprüfung auf Spielende,
     * Anzeige der Spielerstände, Wechsel zum nächsten Zug.
     * Möglichkeit, den aktuellen Spielstand zu speichern
     * oder das Programm jederzeit zu beenden.
     *
     * @throws IOException falls beim Einlesen der Benutzereingabe ein Fehler auftritt
     */
    public void run() throws IOException {
      String input="";
      boolean eingabe=false;
      do {
          System.out.print("         \n Neues Spiel 'n'");
          System.out.print("         \n Spiel laden 'l'");
          System.out.print("         \n  ---------------------");
          System.out.print("         \n ");
          input = liesEingabe();
          if (input.equals("n")) {
              SpielerAuswahl();
              welt.laenderErstellung();
              welt.laenderVerteilung();
              welt.kartenErstellung();
              eingabe = true;
          } else if (input.equals("l")) {
              try {
                  welt.laden();
                  eingabe = true;
              } catch (ClassNotFoundException | KeinSpeicherstandException e) {
                  System.out.println(e.getMessage());
              }
          } else {
              System.out.println("Eingabe wiederholen!");
          }
      }while(!eingabe);
       welt.kontinentAktualisieren();

       do {
           System.out.println(welt.aktiverSpieler());
            do {
                erstePhaseAusgabe();
                input=liesEingabe();
                verarbeiteEingabeEins(input);
            }while (!input.equals("v")&&!input.equals("q")&&!input.equals("c"));
            if(input.equals("q")) {
                break;
            }
            do {
                zweitePhaseAusgabe();
                input=liesEingabe();
                verarbeiteEingabeZwei(input);
            }
            while (!input.equals("p")&&!input.equals("q"));
           if(input.equals("q")) {
               break;
           }
            do {
                drittePhaseAusgabe();
                input=liesEingabe();
                verarbeiteEingabeDrei(input);
            }
            while (!input.equals("p")&&!input.equals("q")&&!input.equals("s"));
           if(input.equals("q")) {
               break;
           }
           welt.loserCheck();
           if(!welt.winnerCheck().equals("")){
               System.out.println(welt.winnerCheck());
               input="q";
           }
           for(Spieler spieler:welt.getSpielerListe()){
               System.out.println(spieler);
           }
           welt.naechsterZug();
           welt.kontinentAktualisieren();
           if(input.equals("s")) {
               if(welt.speichern()){
                   System.out.println("Das Speichern war erfolgreich!");
               }
           }
       }
       while(!input.equals("q"));
    }


    /**
     *  Programmstart für den Konsolen-Client.
     *  Erzeugt eine RisikoClientCUI
     *  startet die Hauptschleife
     * @param args
     */
    public static void main(String[] args) {

       try{
            RisikoClientCUI rcui= new RisikoClientCUI();
            rcui.run();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}