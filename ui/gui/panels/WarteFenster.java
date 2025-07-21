package Risiko.ui.gui.panels;

import Risiko.Server.src.GameServer;

import javax.swing.*;
import java.awt.*;

public class WarteFenster extends JFrame {
    private JProgressBar progressBar;
    private JButton play;
    private boolean mode;
    private JLabel label;

    /**
     * Erstellt ein Fenster, in dem über eine Fortschrittsanzeige und einen Text ersichtlich ist,
     * wie viele Spieler beigetreten sind.
     * Ab zwei Spielern kann das Spiel gestartet werden; ab sechs Spielern erfolgt der automatische Spielstart.
     * @param mode Modus (0 = Spiel erstellen, 1 = Spiel beitreten)
     */

    public WarteFenster(boolean mode) {
        this.mode = mode;

        JPanel main = new JPanel(new BorderLayout(10, 20));
        main.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        main.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        main.setBackground(new Color(30, 30, 30));
        setContentPane(main);

        progressBar = new JProgressBar(0, 6);
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(100, 200, 100));
        progressBar.setBackground(Color.DARK_GRAY);
        progressBar.setFont(progressBar.getFont().deriveFont(Font.BOLD, 14f));
        main.add(progressBar, BorderLayout.NORTH);

        label = new JLabel("0/6 Spieler", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 28));
        label.setForeground(Color.WHITE);
        main.add(label, BorderLayout.CENTER);

        play = new JButton("Play");
        play.setEnabled(false);
        play.setFont(play.getFont().deriveFont(Font.PLAIN, 16f));
        main.add(play, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 220);
        setSize(300, 200);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Überprüft, ob der Client im Modus „Spiel erstellen“ (mode = 0) oder „Spiel beitreten“ (mode = 1) ist
     * und ob die erforderliche Spielerzahl erreicht wurde.
     * Ab zwei Spielern färbt sich die Fortschrittsleiste grün und der Host kann das Spiel starten;
     * ab sechs Spielern erfolgt der automatische Spielstart.
     * @param anzahl Die aktuell beigetretenen Spieler
     */
    public void updateAnzahl(int anzahl) {
        this.label.setText(String.valueOf(anzahl));

        progressBar.setValue(anzahl);
        progressBar.setString(anzahl + " / " + progressBar.getMaximum());
        label.setText(anzahl + " / " + progressBar.getMaximum() + " Spieler");
        if (anzahl < 2) {
            progressBar.setForeground(Color.lightGray);
        } else {
            progressBar.setForeground(new Color(100, 200, 100));
        }
        boolean canStart = mode && (anzahl >= 2 && anzahl <= progressBar.getMaximum());
        play.setEnabled(canStart);

        if (!mode) {
            play.setEnabled(false);
        }
        this.label.repaint();
        this.label.revalidate();
    }
    public void getAnzahl() {
        updateAnzahl(progressBar.getValue());
    }
    public JButton getPlay(){
        return this.play;
    }
}
