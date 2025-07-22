package Risiko.entities;

import Risiko.domain.*;
import Risiko.domain.exceptions.*;
import Risiko.persistence.FilePersistenceManager;


import java.io.Serializable;
import java.util.List;

public class Land  implements Serializable {
    private String name;
    private int armee;
    private Spieler besitzer;
    private String id;
    private List<String> nachbarLaender;
    private boolean erobert;
    private int bewegteTruppen;
    //Konstruktoren
    public Land(String name,String id) {
        this.name = name;
        this.armee=1;
        this.besitzer=null;
        this.id=id;
        this.erobert=false;
        this.bewegteTruppen=0;
    }

    /**
     * Erzeugt ein Land mit allen übergebenen Eigenschaften.
     *
     * @param name            Name des Landes
     * @param armee           aktuelle Zahl der Armeen in diesem Land
     * @param besitzer        Spieler, dem dieses Land gehört
     * @param id              eindeutige ID des Landes
     * @param nachbarLaender  Liste der IDs benachbarter Länder
     * @param erobert         true, wenn das Land in dieser Runde erobert wurde
     * @param bewegteTruppen  bereits im aktuellen Zug verschobene Truppen
     */
    public Land(String name, int armee, Spieler besitzer, String id, List <String> nachbarLaender, boolean erobert, int bewegteTruppen) {
        this.name = name;
        this.armee=armee;
        this.besitzer=besitzer;
        this.id = id;
        this.nachbarLaender=nachbarLaender;
        this.erobert=erobert;
        this.bewegteTruppen=bewegteTruppen;
    }
    //Getter
   public String getName() {
        return name;
   }
   public int getArmee() {
        return armee;
    }
   public Spieler getBesitzer() {
        return besitzer;
    }
   public String getID(){
        return id;
   }
   public int getBewegteTruppen() {
        return bewegteTruppen;
   }
   public List<String> getNachbarLaender() {
        return nachbarLaender;
   }
    public boolean getErobert(){
        return erobert;
    };
    //Setter
    public void setErobert(boolean erobert){
        this.erobert=erobert;
    }
    public void setNachbarLaender(List<String> nachbarLaender) {
        this.nachbarLaender=nachbarLaender;
    }
    public void setBesitzer(Spieler spieler){
       this.besitzer=spieler;
    }
    public void setArmee(int armee) {
        this.armee=armee;
    }
    public void setBewegteTruppen(int bewegteTruppen) {
        this.bewegteTruppen += bewegteTruppen;
    }

    /**
     * Gibt eine lesbare Beschreibung des Landes zurück,
     * bestehend aus Name, ID, Besitzer und aktueller Armeeanzahl.
     *
     * @return String mit Landesinformationen
     */
    public String toString(){
        return (this.name+" mit der ID: \"" + this.id +"\", gehört "+ this.besitzer.getName() + " mit " + this.armee + " Einheiten.");
    }
}
