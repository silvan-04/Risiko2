package Risiko.ui.gui.panels;

import Risiko.domain.*;
import Risiko.entities.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;

/**
 * Erstellt Panel für Spieler. Spielerpanel werden überein ander angeordenet.
 */
public class SpielerPanel extends JPanel {
    private Welt welt;
    private int minWidth;
    private int maxHeight;
    public SpielerPanel(Welt welt) {
        super();
        this.welt=welt;
        this.maxHeight = 972;
        this.minWidth = 192;
        this.setPreferredSize(new Dimension(this.maxHeight/7,this.maxHeight));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); //SpielerPanels sind übereinander
        this.add(Box.createVerticalStrut(this.maxHeight/20));//Abstand zum oberen Rand
        for(Spieler spieler: welt.getSpielerListe()){ //Erstellt Panel für jeden Spieler
            SpielerEinzelPanel panel = new SpielerEinzelPanel(spieler,welt,this.getSize());
            this.add(panel);
            this.add(Box.createVerticalStrut(this.maxHeight/20)); //abstand zwischen spielerPanels
        }
        this.revalidate();
        this.repaint();
    }

    /**
     *
     */
    public void heightUpdate() {
        this.maxHeight = this.getHeight();
        for(Component comp: this.getComponents()){
            if(comp instanceof SpielerEinzelPanel){
                ((SpielerEinzelPanel)comp).setPreferredHeight((int)(this.getWidth()),(int)(this.getHeight()/10));
            }
        }
        this.repaint();
    }

}
