package Risiko.ui.gui.panels;

import Risiko.Server.src.GameServer;

import javax.swing.*;
import java.awt.*;

public class WarteFenster extends JFrame {
    private JProgressBar progressBar;
    private JButton play;
    private boolean mode;

    public WarteFenster(boolean mode) {
        this.mode = mode;
        progressBar = new JProgressBar(0, 2);
        progressBar.setStringPainted(true);
        play = new JButton("Play");
        play.setEnabled(false);
        setLayout(new BorderLayout());
        add(progressBar, BorderLayout.NORTH);
        add(play, BorderLayout.SOUTH);
        setSize(300, 200);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Schaut, ob man sich im Modus 0 („Spiel erstellen“) oder 1 („Spiel beitreten“) befindet und ob genügend Spieler dabei sind. Wenn man der Ersteller ist und sich 2–6 Spieler im Spiel befinden, kann man starten.
     * @param anzahl
     */
    public void updateAnzahl(int anzahl) {
        progressBar.setValue(anzahl);
        if ((anzahl >= 2 && anzahl <= 6) && (mode)) {
            System.out.println(mode + " is im Wartefenster");
            play.setEnabled(true);
            play.addActionListener(e -> {
//                GameServer.startGame();
            });
        }
        if (!mode) {
            System.out.println(mode + " is im Wartefenster");
            play.setEnabled(false);
        }
    }
}
