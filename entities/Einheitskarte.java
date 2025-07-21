package Risiko.entities;

import Risiko.domain.*;
import Risiko.domain.exceptions.*;
import Risiko.persistence.FilePersistenceManager;
import java.io.Serializable;

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
    public String getSymbol() { return symbol; }
    //toString-Methode
    public String toString(){
        return (land.getName() + " - " + symbol);
    }
}
