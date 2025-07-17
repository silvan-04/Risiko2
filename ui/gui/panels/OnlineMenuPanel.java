package Risiko.ui.gui.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;

import Risiko.Client.GameClient;
import Risiko.Server.src.GameServer;
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
    private JButton playButton;
    private boolean mode;


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
        zurück.setBounds(50, 500, 100, 40);


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
        box1.setVisible(false);
        name1Label.setVisible(false);
        name1Field.setVisible(false);
        // Play-Button
        playButton = new JButton("Play");
        playButton.setBounds(160, 500, 200, 40);
        playButton.setFont(new Font("Arial", Font.BOLD, 18));
        playButton.setBackground(Color.lightGray);
        playButton.setVisible(false);
        this.add(playButton);


        neuesSpiel.addActionListener(e ->  {
            mode = true;
            neuesSpiel.setVisible(false);
            spielBeitreten.setVisible(false);
            box1.setVisible(true);
            name1Label.setVisible(true);
            name1Field.setVisible(true);
            playButton.setVisible(true);
            repaint();
            revalidate();
        });

        spielBeitreten.addActionListener(e -> {
            mode = false;
            neuesSpiel.setVisible(false);
            spielBeitreten.setVisible(false);
            box1.setVisible(true);
            name1Label.setVisible(true);
            name1Field.setVisible(true);
            playButton.setVisible(true);
            repaint();
            revalidate();
        });

        playButton.addActionListener(e -> {
            if (mode) {
                parent.dispose();
                // Server starten
                System.out.println("vor gameserver");
                new Thread(() -> {
                    GameServer gameServer = null;
                    try {
                        gameServer = new GameServer(new Welt());
                        gameServer.start();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }).start();

                System.out.println("nach gameServer");
                boolean clientConnected = false;
                do{
                    try {
                        System.out.println("vor client");
                        GameClient client = new GameClient(mode);
                        clientConnected = true;
                        System.out.println("nach client");
                    } catch (IOException | ClassNotFoundException ex) {}
                }while(!clientConnected);
                System.out.println("is im Online Panel");
            } else if (!mode) {
                parent.setVisible(false);
                try {
                    GameClient client = new GameClient(mode);
                    parent.dispose();
                } catch (IOException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(this, "Es ist ein Fehler aufgetreten! Ist bereits ein Server erstellt worden ?", " Fehler!", JOptionPane.INFORMATION_MESSAGE);
                }
                System.out.println("is im Online Panel");
            }
        });





        /**
         * geht zurück zu darüber liegendem J-Frame, entfernt alten Inhalt + StartPanel hinzufügen
         */
        zurück.addActionListener(e -> {
            parent.startVisible();
            setVisible(false);
            neuesSpiel.setVisible(true);
            spielBeitreten.setVisible(true);
            box1.setVisible(false);
            name1Label.setVisible(false);
            name1Field.setVisible(false);
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

    public boolean getMode() {
        return mode;
    }

}
