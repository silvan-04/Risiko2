package Risiko.ui.gui.panels;

import Risiko.ui.gui.Fenster.MenuFenster;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

/**
 * 4 Buttons werden angezeigt die zu anderen Panels weiterleiten.
 */
public class StartMenuPanel extends JPanel {
    public StartMenuPanel(MenuFenster parent) throws RuntimeException {
        setLayout(null);
        setOpaque(false);
        setBounds(0, 0, 1000, 600);

        JButton neuButton    = new JButton("Neues Spiel");
        JButton ladenButton  = new JButton("Spiel laden");
        JButton onlineButton = new JButton("Online spielen");
        JButton exitButton   = new JButton("Spiel beenden");

        neuButton   .setBounds(350, 150, 300, 50);
        ladenButton .setBounds(350, 220, 300, 50);
        onlineButton.setBounds(350, 290, 300, 50);
        exitButton  .setBounds(350, 360, 300, 50);

        // Aktion: neues Spiel → Setup-Panel anzeigen
        neuButton.addActionListener(e -> parent.zeigeSetupPanel());
        ladenButton.addActionListener(e -> {
            try {
                parent.loadGame();
            } catch (IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        onlineButton.addActionListener(e -> parent.startOnlineMode());
        exitButton.addActionListener(e -> System.exit(0));

        add(neuButton);
        add(ladenButton);
        add(onlineButton);
        add(exitButton);
    }
}
