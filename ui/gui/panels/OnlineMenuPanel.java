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
    private JButton playButton,zurückOnline;
    private boolean mode;


    /**
     * Erstellt das Panel für das Onlinespiel. Hier kann man ein neues Spiel erstellen oder einem bestehenden beitreten.
     * Anschließend gibt man einen Namen ein und drückt „Play“.
     * @param parent das übergeordnete Fenster oder der Container, in dem das Panel angezeigt wird
     */
    public OnlineMenuPanel(MenuFenster parent) {
        super();
        this.setLayout(null);
        this.setOpaque(false);
        this.setBounds(0, 0, 1000, 600);

        JButton neuesSpiel = new JButton("Neues Spiel");
        JButton spielBeitreten = new JButton("Spiel Beitreten");
        JButton spielLaden = new JButton("Spiel laden");
        JButton spielBeitretenNeu = new JButton("neuem Spiel beitreten");
        JButton spielBeitretenLaden = new JButton("geladenem Spiel beitreten");
        zurückOnline = new JButton("Zurück");

        neuesSpiel.setBounds(350, 150, 140, 50);
        spielBeitreten.setBounds(350, 220, 300, 50);
        spielLaden.setBounds(510, 150, 140, 50);
        spielBeitretenNeu.setBounds(350,150,300,50);
        spielBeitretenLaden.setBounds(350,220,300,50);
        zurückOnline.setBounds(50, 500, 100, 40);
        zurückOnline.setVisible(false);
        add(zurückOnline);

        name1Label = new JLabel("  Spieler:");
        name1Label.setBounds(50, 130, 100, 25);
        name1Label.setForeground(Color.white);
        add(name1Label);
        name1Field = new JTextField();
        name1Field.setForeground(Color.white);
        name1Field.setBackground(Color.decode("#191C21"));
        name1Field.setBorder(BorderFactory.createLineBorder(Color.darkGray));
        name1Field.setBounds(160, 130, 200, 25);
        add(name1Field);

        name1Label.setVisible(false);
        name1Field.setVisible(false);
        // Play-Button
        playButton = new JButton("Play");
        playButton.setBounds(160, 500, 200, 40);
        playButton.setFont(new Font("Arial", Font.BOLD, 18));
        playButton.setBackground(Color.lightGray);
        playButton.setVisible(false);
        this.add(playButton);
        spielBeitretenNeu.setVisible(false);
        spielBeitretenLaden.setVisible(false);


        spielLaden.addActionListener(e->    {
            // Server starten
            final boolean[] speicherstand = new boolean[1];
            new Thread(() -> {
                GameServer gameServer = null;
                try {
                    Welt welt = new Welt();
                    welt.laden();
                    speicherstand[0]=true;
                    gameServer = new GameServer(welt);
                    gameServer.start();
                } catch (IOException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                } catch (KeinSpeicherstandException ex) {
                    speicherstand[0]=false;
                    JOptionPane.showMessageDialog(parent, ex.getMessage(), "FEHLER!", JOptionPane.ERROR_MESSAGE);
                }
            }).start();

                parent.dispose();
                boolean clientConnected = false;
                do{
                    try {
                        GameClient client = new GameClient(mode);
                        clientConnected = true;
                    } catch (IOException | ClassNotFoundException ex) {} catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }while(!clientConnected);
        });

        spielBeitretenNeu.addActionListener(e->    {
            mode = false;
            neuesSpiel.setVisible(false);
            spielBeitretenLaden.setVisible(false);
            spielBeitretenNeu.setVisible(false);
            spielBeitreten.setVisible(false);
            spielLaden.setVisible(false);
            name1Label.setVisible(true);
            name1Field.setVisible(true);
            playButton.setVisible(true);
            zurückOnline.setVisible(true);
            repaint();
            revalidate();
        });
        spielBeitretenLaden.addActionListener(e->    {
            parent.setVisible(false);
            try {
                GameClient client = new GameClient(mode);
                parent.dispose();
            } catch (IOException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Es ist ein Fehler aufgetreten! Ist bereits ein Server erstellt worden ?", " Fehler!", JOptionPane.INFORMATION_MESSAGE);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        neuesSpiel.addActionListener(e ->  {
            mode = true;
            neuesSpiel.setVisible(false);
            spielBeitreten.setVisible(false);
            spielLaden.setVisible(false);
            name1Label.setVisible(true);
            name1Field.setVisible(true);
            playButton.setVisible(true);
            zurückOnline.setVisible(true);
            repaint();
            revalidate();
        });

        spielBeitreten.addActionListener(e -> {
            mode = false;
            neuesSpiel.setVisible(false);
            spielBeitreten.setVisible(false);
            spielBeitretenLaden.setVisible(true);
            spielBeitretenNeu.setVisible(true);
            spielLaden.setVisible(false);
            zurückOnline.setVisible(true);
            repaint();
            revalidate();
        });

        /**
         * Spieler 1 startet den GameServer und tritt ihm bei.
         * Die restlichen Spieler treten dem Server bei.
         * Prüft, ob bereits ein Server existiert, und fordert zur Namenseingabe auf.
         */
        playButton.addActionListener(e -> {
            String name = name1Field.getText();
            if(!name.equals("")){
                if (mode) {
                    parent.dispose();
                    // Server starten
                    new Thread(() -> {
                        GameServer gameServer = null;
                        try {
                            gameServer = new GameServer(new Welt());
                            gameServer.start();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }).start();
                    boolean clientConnected = false;
                    do{
                        try {
                            GameClient client = new GameClient(mode,name);
                            clientConnected = true;
                        } catch (IOException | ClassNotFoundException ex) {} catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    }while(!clientConnected);
                } else if (!mode) {
                    parent.setVisible(false);
                    try {
                        GameClient client = new GameClient(mode,name);
                        parent.dispose();
                    } catch (IOException | ClassNotFoundException ex) {
                        JOptionPane.showMessageDialog(this, "Es ist ein Fehler aufgetreten! Ist bereits ein Server erstellt worden ?", " Fehler!", JOptionPane.INFORMATION_MESSAGE);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }else{
               JOptionPane.showMessageDialog(this, "Es muss ein Name eingegeben werden!", "Keine Eingabe!", JOptionPane.ERROR_MESSAGE);
            }

        });

        /**
         * Kehrt zum übergeordneten JFrame zurück, entfernt den bisherigen Inhalt
         * und fügt das StartPanel hinzu.
         */

        zurückOnline.addActionListener(e -> {
            parent.startVisible();
            neuesSpiel.setVisible(true);
            spielBeitreten.setVisible(true);
            spielLaden.setVisible(true);
            name1Label.setVisible(false);
            name1Field.setVisible(false);
            spielBeitretenLaden.setVisible(false);
            spielBeitretenNeu.setVisible(false);
            zurückOnline.setVisible(false);
        });

        add(neuesSpiel);
        add(spielBeitreten);
        add(spielLaden);

        add(spielBeitretenNeu);
        add(spielBeitretenLaden);
    }

    public JButton getZurueckButton(){
        return zurückOnline;
    }

    public boolean getMode() {
        return mode;
    }
}
