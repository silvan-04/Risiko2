package Risiko.persistence;

import Risiko.domain.exceptions.KeinSpeicherstandException;

import Risiko.domain.*;
import Risiko.domain.exceptions.*;
import Risiko.entities.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FilePersistenceManager implements Serializable {
    private BufferedReader reader = null;
    private PrintWriter writer = null;

    /**
     * Öffnet die Datei und liest sie zeilenweise aus.
     * @param datei
     * @throws FileNotFoundException
     */
    public void openForReading(String datei) throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(datei));
    }

    /**
     * Öffnet die Datei und beschreibt sie zeilenweise.
     * @param datei
     * @throws IOException
     */
    public void openForWriting(String datei) throws IOException {
        writer = new PrintWriter(new BufferedWriter(new FileWriter(datei)));
    }

    /**
     * Schließt Reader und Writer und prüft, ob ein Fehler aufgetreten ist.
     * @return
     */
    public boolean close() {
        if (writer != null)
            writer.close();

        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

                return false;
            }
        }

        return true;
    }

    /**
     * Methode um Land in Datei zu speichern.
     * Booleans werden als "t" oder "f" gespeichert.
     * Liste erst die Anzahl, danach die Strings einzelt.
     * Besitzer wird nur der name des Spielers gespeichert.
     *
     * @param l
     * @throws IOException
     */
    public boolean speichereLand(Land l)throws IOException{
        schreibeZeile(l.getName());
        schreibeZeile(""+l.getArmee());
        schreibeZeile(l.getBesitzer().getName());
        schreibeZeile(l.getID());
        schreibeZeile(""+l.getNachbarLaender().size());
        for(int i=0;i<l.getNachbarLaender().size();i++){
            schreibeZeile(l.getNachbarLaender().get(i));
        }
        if(l.getErobert()){
            schreibeZeile("t");
        }else {
            schreibeZeile("f");
        }
        schreibeZeile(""+l.getBewegteTruppen());
        return true;
    }

    /**
     * Methode um ein Land aus Datei zu laden, booleans sind als "t" oder "f" gespeichert.
     * Vom Besitzer nur der name gespeichert, deswegen wird mithilfe der Spielerliste der Besitzer zugeordnet.
     *
     * @param spielerList
     * @return
     * @throws IOException
     */
    public Land ladeLand(List<Spieler> spielerList) throws IOException, KeinSpeicherstandException {
        String name = liesZeile();
        if(name.equals("")){
            throw new KeinSpeicherstandException();
        }
        int armee= Integer.parseInt(liesZeile());
        String spielerName=liesZeile();
        Spieler spieler=null;
        for(int i=0;i<spielerList.size();i++){
            if(spielerName.equals(spielerList.get(i).getName())){
                spieler=spielerList.get(i);
                break;
            }
        }
        String id = liesZeile();
        int nachbarn=Integer.parseInt(liesZeile());
        List <String>nachbarLaender = new ArrayList<String>();
        for(int i=0;i<nachbarn;i++){
            nachbarLaender.add(liesZeile());
        }
        boolean erobert;
        if(liesZeile()=="t"){
            erobert = true;
        }else {
            erobert = false;
        }
        int bewegteTruppen=Integer.parseInt(liesZeile());
        return new Land(name, armee, spieler, id, nachbarLaender,erobert,bewegteTruppen);
    }

    /**
     * Speichert alle Spieldaten eines Spielers: ID, Name, Länderanzahl, Einheiten, Status und Bild.
     * @param spieler
     * @return
     * @throws IOException
     */
    public boolean speichereSpieler(Spieler spieler) throws IOException{
        schreibeZeile(""+ Spieler.getIdZaehler());
        schreibeZeile(""+spieler.getId());
        schreibeZeile(spieler.getName());
        schreibeZeile(""+spieler.getAnzahlLaender());
        schreibeZeile(""+spieler.getEinheitenRunde());
        if(spieler.getIstAmZug()){
            schreibeZeile("t");
        }else{
            schreibeZeile("f");
        }
        if(spieler.getLebendig()){
            schreibeZeile("t");
        }else {
            schreibeZeile("f");
        }
        schreibeZeile(spieler.getCharBild());
        return true;
    }

    /**
     * Liest die gespeicherte Spieldatei aus und erstellt ein neues Spieler-Objekt.
     * @return
     * @throws IOException
     * @throws KeinSpeicherstandException
     */
    public Spieler ladeSpieler() throws IOException, KeinSpeicherstandException {
        int idZaeler;
        try {
             idZaeler = Integer.parseInt(liesZeile());
        }catch (NumberFormatException e){
            throw new KeinSpeicherstandException();
        }
        Spieler.setIdZaehler(idZaeler);
        int id = Integer.parseInt(liesZeile());
        String name = liesZeile();
        int anzahlLaender = Integer.parseInt(liesZeile());
        int einheitenRunde = Integer.parseInt(liesZeile());
        boolean istAmZug;
        if(Objects.equals(liesZeile(), "t")){
            istAmZug = true;
        }else{
            istAmZug = false;
        }
        boolean lebendig;
        if(Objects.equals(liesZeile(), "t")){
            lebendig = true;
        }else {
            lebendig = false;
        }
        String charBild = liesZeile();
        return new Spieler(id,name,anzahlLaender,einheitenRunde,istAmZug,lebendig, charBild);
    }

    /**
     * Speichert die Anzahl und die Details aller Einheitskarten eines Spielers in der Datei.
     * @param spieler
     * @return
     * @throws IOException
     */
    public boolean speichereSpielerKarten(Spieler spieler) throws IOException {
        List<Einheitskarte> karten =spieler.getEinheitskarten();
        schreibeZeile(Integer.toString(karten.size()));
        for(Einheitskarte karte:karten){
            schreibeZeile(karte.getLand().getID());
            schreibeZeile(karte.getSymbol());
        }
        return true;
    }

    /**
     * Lädt die gespeicherten Einheitskarten eines Spielers aus der Datei und gibt sie an das aktuelle Spiel zurück.
     * @param lv
     * @return
     * @throws IOException
     * @throws KeinSpeicherstandException
     */
    public List<Einheitskarte> ladeSpielerKarten(Landverwaltung lv) throws IOException, KeinSpeicherstandException {
       int kartenAnzahl;
       List <Einheitskarte> karten = new ArrayList<>();
        try {
            kartenAnzahl = Integer.parseInt(liesZeile());
       }catch(NumberFormatException e){
           throw new KeinSpeicherstandException();
       }
       for(int i=0; i<kartenAnzahl;i++){
           String id= liesZeile();
           Land land=lv.idToLand(id);
           String symbol= liesZeile();
           karten.add(new Einheitskarte(land,symbol));
       }
       return karten;
    }

    /**
     * Speichert, welcher Spieler einen Kontinent eingenommen hat.
     * @param besitzer
     * @return
     * @throws IOException
     */
    public boolean speichereKontinentBesitzer(Spieler besitzer) throws IOException{
        schreibeZeile(besitzer.getName());
        return true;
    }

    /**
     * Gibt für das aktuelle Spiel zurück, welcher Spieler im Speicher einen Kontinent eingenommen hat.
     * @param spielerList
     * @return
     * @throws IOException
     * @throws KeinSpeicherstandException
     */
    public Spieler ladeKontinentBesitzer(List<Spieler> spielerList) throws IOException, KeinSpeicherstandException {
        String name = liesZeile();
        for(Spieler spieler:spielerList){
            if(spieler.getName().equals(name)){
                return spieler;
            }
        }
        throw new KeinSpeicherstandException();
    }

    /**
     * Speichert die Anzahl und die Details der Einheitskarten.
     * @param e
     * @return
     * @throws IOException
     */
    public boolean speichereEinheitskarten(List<Einheitskarte> e) throws IOException{
        String anzahl= Integer.toString(e.size());
        schreibeZeile(anzahl);
        for(Einheitskarte einheitskarte:e){
            schreibeZeile(einheitskarte.getLand().getID());
            schreibeZeile(einheitskarte.getSymbol());
        }
        return true;
    }

    /**
     * Lädt alle Einheitskarten der Spieler mit den richtigen Details.
     * @param lv
     * @return
     * @throws IOException
     * @throws KeinSpeicherstandException
     */
    public List<Einheitskarte> ladeEinheitskarten(Landverwaltung lv) throws IOException, KeinSpeicherstandException {
        int anzahl;
        try{
             anzahl=Integer.parseInt(liesZeile());
        }catch (NumberFormatException e){
            throw new KeinSpeicherstandException();
        }
        List<Einheitskarte> einheitskarten=new ArrayList<>();
       for(int i=0;i<anzahl;i++){
         String id=liesZeile();
         Land land=lv.idToLand(id);
         String symbol=liesZeile();
         einheitskarten.add(new Einheitskarte(land,symbol));
       }
       return einheitskarten;
    }

    /**
     * Speichert die Anzahl und die Details der Einheitskarten.
     * @param missionskarten
     * @return
     * @throws IOException
     */
    public boolean speichereMissionen(Missionskarten missionskarten) throws IOException{
        if(missionskarten instanceof MissionEliminiereSpieler){
            MissionEliminiereSpieler mission = (MissionEliminiereSpieler) missionskarten;
            schreibeZeile("0");
            schreibeZeile(mission.getSpieler().getName());
            schreibeZeile(mission.getZielspieler().getName());

        } else if(missionskarten instanceof MissionErobereKontinent){
            MissionErobereKontinent mission=(MissionErobereKontinent) missionskarten;
            schreibeZeile("1");
            schreibeZeile(mission.getKontinent1Name());
            schreibeZeile(mission.getKontinent2Name());
            if(mission.isZusatzKontinent()){
                schreibeZeile("t");
            }else{
                schreibeZeile("f");
            }
            List<Spieler> kontinent1=mission.getKontinent1();
            List<Spieler> kontinent2=mission.getKontinent2();
            schreibeZeile(Integer.toString(kontinent1.size()));
            for(Spieler spieler:kontinent1){
                schreibeZeile(spieler.getName());
            }
            schreibeZeile(Integer.toString(kontinent2.size()));
            for(Spieler spieler:kontinent2){
                schreibeZeile(spieler.getName());
            }
            schreibeZeile(mission.getSpieler().getName());


        } else if (missionskarten instanceof MissionErobereLänder) {
            MissionErobereLänder mission = (MissionErobereLänder) missionskarten;
            schreibeZeile("2");
            if(mission.getNurLänder()){
                schreibeZeile("t");
            }else{
                schreibeZeile("f");
            }
            schreibeZeile(mission.getSpieler().getName());
        }
        return true;
    }

    /**
     * Liest aus dem Speicher die Missionstypen und die dazugehörigen Details aus und erstellt daraus neue Missionskarten.
     * @param spielerList
     * @param lv
     * @return
     * @throws IOException
     * @throws KeinSpeicherstandException
     */
    public Missionskarten ladeMissionen(List<Spieler> spielerList, Landverwaltung lv)throws IOException, KeinSpeicherstandException {
        int missionsTyp=Integer.parseInt(liesZeile());
        switch(missionsTyp){
            case 0->{
                String spielerName=liesZeile();
                Spieler spieler=null;
                for(Spieler spieler1:spielerList){
                    if(spieler1.getName().equals(spielerName)){
                        spieler=spieler1;
                    }
                }
                String spielerName2=liesZeile();
                Spieler spieler2=null;
                for(Spieler spieler21: spielerList){
                    if(spieler21.getName().equals(spielerName2)){
                        spieler2=spieler21;
                    }
                }
                return (new MissionEliminiereSpieler(spieler,spieler2));
            }
            case 1->{
                String kontinent1Name=liesZeile();
                String kontinent2Name=liesZeile();
                boolean zusatzKontinent;
                if(liesZeile().equals("t")){
                    zusatzKontinent=true;
                }else{
                    zusatzKontinent=false;
                }
                int anzahlKontinent1=Integer.parseInt(liesZeile());
                List<Spieler> kontinent1= new ArrayList<>();
                for(int i=0;i<anzahlKontinent1;i++){
                    String spielerName= liesZeile();
                    for(Spieler spieler1:spielerList){
                          if(spielerName.equals(spieler1.getName())){
                              kontinent1.add(spieler1);
                              break;
                          }
                    }

                }
                int anzahlKontinent2=Integer.parseInt(liesZeile());
                List<Spieler> kontinent2= new ArrayList<>();
                for(int i=0;i<anzahlKontinent2;i++){
                    String spielerName=liesZeile();
                    for(Spieler spieler2:spielerList){
                        if(spielerName.equals(spieler2.getName())){
                            kontinent2.add(spieler2);
                            break;
                        }
                    }
                }
                String spielerName3=liesZeile();
                Spieler spieler33=null;
                for(Spieler spieler3:spielerList){
                    if(spieler3.getName().equals(spielerName3)){
                        spieler33=spieler3;
                    }
                }
                return (new MissionErobereKontinent(kontinent1Name,kontinent2Name,kontinent1,kontinent2,zusatzKontinent,spieler33,lv));
            }
            case 2->{
                boolean nurLänder;
                if(liesZeile().equals("t")){
                    nurLänder=true;
                }else{
                    nurLänder=false;
                }
                String spielerName=liesZeile();
                Spieler spieler=null;
                for(Spieler spieler1:spielerList){
                    if(spielerName.equals(spieler1.getName())){
                        spieler=spieler1;
                        break;
                    }
                }
                return new MissionErobereLänder(nurLänder,spieler,lv);
            }
            default -> throw new KeinSpeicherstandException();
        }
    }

    /**
     * Speichert die Ablageliste mit Anzahl, Land-IDs und Symbolen in die Datei.
     * @param a
     * @return
     * @throws IOException
     */
    public boolean speichereAblageliste(List<Einheitskarte> a) throws IOException{
        schreibeZeile(Integer.toString(a.size()));
        for(Einheitskarte e:a){
            schreibeZeile(e.getLand().getID());
            schreibeZeile(e.getSymbol());
        }
        return true;
    }

    /**
     * Lädt die gespeicherten Einheitskarten aus der Datei, erstellt neue Kartenobjekte und fügt sie der Liste hinzu.
     * @param lv
     * @return
     * @throws IOException
     * @throws KeinSpeicherstandException
     */
    public List<Einheitskarte> ladeAblageliste(Landverwaltung lv) throws  IOException, KeinSpeicherstandException {
        List<Einheitskarte> ablageliste = new ArrayList<>();
        int anzahl;
        try{
            anzahl = Integer.parseInt(liesZeile());
        }catch(NumberFormatException e){
            throw new KeinSpeicherstandException();
        }
        for(int i=0; i<anzahl;i++){
            String id=liesZeile();
            Land land=lv.idToLand(id);
            String symbol= liesZeile();
            ablageliste.add(new Einheitskarte(land,symbol));
        }
        return ablageliste;
    }

    /**
     * Schreibt eine Textzeile, wenn der Writer geöffnet ist.
     * @param daten
     */
    private void schreibeZeile(String daten) {
        if (writer != null)
            writer.println(daten);
    }

    /**
     * Liest eine Textzeile aus der geöffneten Datei oder gibt null zurück, wenn der Reader geschlossen ist.
     * @return
     * @throws IOException
     */
    private String liesZeile() throws IOException {
        if (reader != null)
            return reader.readLine();
        else
            return "";
    }

//    public static void main(String[] args) throws IOException {
//       FilePersistenceManager pm= new FilePersistenceManager();
//       pm.ladeSpieler();
//    }
}
