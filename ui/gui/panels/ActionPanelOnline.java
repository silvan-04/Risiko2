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
    private Spieler spieler;

    /**
     *
     * @param welt
     * @param frame
     * @param weltPanel
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
        JButton kartenKnopf = new JButton("Karten");
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
    public void setButton(boolean enabled) {
        aktionsKnopf.setEnabled(enabled);
    }
    public JButton getActionbutton() {
        return aktionsKnopf;
    }
    public void setSpieler(Spieler spieler){
        this.spieler=spieler;
    }
}
