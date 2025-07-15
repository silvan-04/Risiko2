package Risiko.ui.gui.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import Risiko.domain.*;
import Risiko.domain.exceptions.DoppelterNameException;
import Risiko.domain.exceptions.KeinSpeicherstandException;
import Risiko.ui.gui.RisikoClientGUI;
import Risiko.ui.gui.panels.*;
import Risiko.ui.gui.Fenster.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OnlineMenuPanel extends JPanel{
    private JLabel name1Label;
    private JTextField name1Field;

    public OnlineMenuPanel(MenuFenster parent) {
        super();
        this.setLayout(null);
        this.setOpaque(false);
        this.setBounds(0, 0, 1000, 600);

        JButton neuesSpiel = new JButton("Neues Spiel erstellen");
        JButton spielBeitreten = new JButton("Spiel Beitreten");
        JButton zurück = new JButton("Zurück");

        neuesSpiel.setBounds(350, 150, 300, 50);
        spielBeitreten.setBounds(350, 220, 300, 50);
        zurück.setBounds(350, 290, 300, 50);

        neuesSpiel.addActionListener(e ->  {
            neuesSpiel.setVisible(false);
            spielBeitreten.setVisible(false);
            JPanel box1 = farbbox(125, 132, 21, 21, "#1B3B6F");
            add(box1);
            name1Label = new JLabel("1. Spieler:");
            name1Label.setBounds(50, 130, 100, 25);
            name1Label.setForeground(Color.white);
            add(name1Label);
            name1Field = new JTextField();
            name1Field.setForeground(Color.white);
            name1Field.setBackground(Color.decode("#191C21"));
            name1Field.setBorder(BorderFactory.createLineBorder(Color.darkGray));
            name1Field.setBounds(160, 130, 200, 25);
            add(name1Field);
            repaint();
            revalidate();
        });

        /**
         * geht zurück zu darüber liegendem J-Frame, entfernt alten Inhalt + StartPanel hinzufügen
         */
        zurück.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.getContentPane().removeAll();
            frame.getContentPane().add(new StartMenuPanel((MenuFenster)frame));
            frame.revalidate();
            frame.repaint();
        });

        add(neuesSpiel);
        add(spielBeitreten);
        add(zurück);

    }
    private JPanel farbbox(int x, int y, int w, int h, String hex) {
        JPanel box = new JPanel();
        box.setBackground(Color.decode(hex));
        box.setBounds(x, y, w, h);
        return box;
    }

}
