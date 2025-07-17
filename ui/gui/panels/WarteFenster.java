package Risiko.ui.gui.panels;

import javax.swing.*;
import java.awt.*;

public class WarteFenster extends JFrame {
    private JProgressBar progressBar;
    private JButton play;
    private int mode;

    public WarteFenster(int mode) {
        this.mode = mode;
        JFrame frame = new JFrame("Warte Fenster");
        progressBar = new JProgressBar(0, 2);
        progressBar.setStringPainted(true);
        play = new JButton("Play");
        play.setEnabled(false);
        frame.setLayout(new BorderLayout());
        frame.add(progressBar, BorderLayout.NORTH);
        frame.add(play, BorderLayout.SOUTH);
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(false);
    }

    /**
     * Schaut, ob man sich im Modus 0 („Spiel erstellen“) oder 1 („Spiel beitreten“) befindet und ob genügend Spieler dabei sind. Wenn man der Ersteller ist und sich 2–6 Spieler im Spiel befinden, kann man starten.
     * @param anzahl
     */
    public void updateAnzahl(int anzahl) {
        progressBar.setValue(anzahl);
        if ((anzahl >= 2 && anzahl <= 6) && (mode == 0)) {
            play.setEnabled(true);
        }
        if (mode == 1) {
            play.setEnabled(false);
        }
    }
}
