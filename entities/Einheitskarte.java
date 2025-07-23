package Risiko.entities;

import Risiko.domain.*;
import Risiko.domain.exceptions.*;
import Risiko.persistence.FilePersistenceManager;
import java.io.Serializable;

/**
 * Repräsentiert eine Einheitskarte.
 * Jede Karte ist einer bestimmten Provinz (Land) zugeordnet und trägt ein Symbol
 * (z. B. "Soldat", "Reiter" oder "Kanone")
 */
public class Einheitskarte implements Serializable {
    private Land land;
    private String symbol;

    //Konstruktor
    public Einheitskarte (Land land, String symbol){
        this.land = land;
        this.symbol = symbol;
    }
    //Getter
    public Land getLand() { return land; }
    public String getLandToString(){
        return land.getName();
    }
    public String getSymbol() { return symbol; }
    //toString-Methode
    public String toString(){
        return (land.getName() + " - " + symbol);
    }
}
