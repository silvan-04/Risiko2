package Risiko.ui.gui.panels;

import Risiko.domain.*;
import Risiko.domain.exceptions.*;
import Risiko.entities.*;
import Risiko.ui.gui.RisikoClientGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class ActionPanelOnline extends JPanel  implements Action{
    private Welt welt;
    private JFrame frame;
    private WeltPanel weltPanel;
    private int buttonClicked;
    private String angriffsLand;
    private String zielLand;
    private int einheiten;
    private JButton aktionsKnopf;
    private JButton kartenKnopf;
    private Spieler spieler;

    /**
     * Erstellt das Aktions-Panel für den Online-Modus eines einzelnen Spielers.
     * Das Panel enthält den Hauptaktionsknopf (Armee verteilen)
     * sowie Buttons zum Anzeigen der Mission und der Handkarten.
     *
     * @param welt       die gemeinsame welt
     * @param frame      das übergeordnete RisikoClientGUI-Fenster
     * @param weltPanel  das Panel, das das eigentliche Spielfeld rendert
     * @param spieler    der lokale Spieler, für den dieses Panel seine Aktionen bereitstellt
     */
    public ActionPanelOnline(Welt welt, RisikoClientGUI frame, WeltPanel weltPanel,Spieler spieler) {
        super();
        this.welt = welt;
        this.frame = (JFrame) frame;
        this.weltPanel = weltPanel;
        this.setLayout(new BorderLayout());
        this.buttonClicked = 0;
        this.einheiten = 0;
        this.spieler = spieler;

        aktionsKnopf = new JButton("Armee verteilen!");
        add(aktionsKnopf, BorderLayout.EAST);

        JButton missionsKnopf = new JButton("Mission");
        add(missionsKnopf, BorderLayout.WEST);
        kartenKnopf = new JButton("Karten");
        add(kartenKnopf, BorderLayout.CENTER);

        missionsKnopf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                JOptionPane.showMessageDialog(frame, welt.getKartenverwaltung().getMissionskarten().get(spieler.getId()).beschreibung(), "Deine Mission!", JOptionPane.INFORMATION_MESSAGE);

            }
        });

        kartenKnopf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                JOptionPane.showMessageDialog(frame, !spieler.getEinheitskarten().isEmpty() ? spieler.getEinheitskarten() : "Du hast keine Karten im Besitz!", "Deine Karten!", JOptionPane.INFORMATION_MESSAGE);

            }
        });

    }

    public void updateKartenListener(){
        for(ActionListener al : kartenKnopf.getActionListeners()){
            kartenKnopf.removeActionListener(al);
        }

        kartenKnopf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                JOptionPane.showMessageDialog(frame, !spieler.getEinheitskarten().isEmpty() ? spieler.getEinheitskarten() : "Du hast keine Karten im Besitz!", "Deine Karten!", JOptionPane.INFORMATION_MESSAGE);

            }
        });
    }
    /**
     * Aktiviert oder deaktiviert den Hauptaktionsknopf.
     *
     * @param enabled  true, um den Knopf zu aktivieren; false, um ihn zu deaktivieren
     */
    public void setButton(boolean enabled) {
        aktionsKnopf.setEnabled(enabled);
    }

    /**
     * Gibt den Haupt-Aktionsknopf dieses Panels zurück.
     * Über diesen Knopf kann der Client Aktionen (z.B. Armee verteilen, angreifen, verschieben) auslösen.
     *
     * @return der JButton, der für Spielaktionen verwendet wird
     */
    public JButton getActionbutton() {
        return aktionsKnopf;
    }

    /**
     *
     * @param spieler
     */
    public void setSpieler(Spieler spieler){
        this.spieler=spieler;
        updateKartenListener();
        this.revalidate();
        this.repaint();
    }
}
