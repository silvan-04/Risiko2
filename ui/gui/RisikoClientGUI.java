package Risiko.ui.gui;

import Risiko.domain.*;
import Risiko.domain.exceptions.*;
import Risiko.ui.gui.Fenster.MenuFenster;
import Risiko.ui.gui.panels.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;


public class RisikoClientGUI extends JFrame {
    private Welt welt;

    public RisikoClientGUI(Welt welt){
        super();
        setTitle("Risiko");
        this.welt = welt;


//        UIManager.put("Panel.background", Color.DARK_GRAY);
//        UIManager.put("OptionPane.background", Color.LIGHT_GRAY);
//        UIManager.put("Button.background", Color.LIGHT_GRAY);

        this.setSize(800, 600);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);//maximiert (nicht vollbild)

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        WindowClosingListener closingListener = new WindowClosingListener(welt); // fragt ob gespeichert werden soll anstatt direkt zu schließen
        addWindowListener(closingListener);

        this.setBackground(Color.black);
        this.setLayout( new BorderLayout());

        JPanel spielerPanel = new SpielerPanel(welt); // Spieler info Rechts
        spielerPanel.setBackground(Color.black);

        JPanel weltPanel = new WeltPanel(welt); // Panel für Karte und info
        weltPanel.setPreferredSize(new Dimension(1728,972));

        JPanel actionPanel = new ActionPanel(welt, this, ((WeltPanel)weltPanel)); // Panel unten mit buttons
        actionPanel.setBorder(BorderFactory.createLineBorder(Color.black));



        add(spielerPanel, BorderLayout.EAST);
        add(actionPanel, BorderLayout.SOUTH);
        add(weltPanel, BorderLayout.CENTER);

        //führt methoden bei Größenänderung vom fenster aus
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                ((WeltPanel) weltPanel).resizeImage(weltPanel.getSize()); //Größe von Karte,GrayMap und Infos anpassen
                ((SpielerPanel)spielerPanel).heightUpdate(); // anpassen von abständen und größen von spielerinfos
            }
        });

        weltPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ((WeltPanel)weltPanel).clickedCountry(e.getX(), e.getY(),weltPanel.getSize());
            }
        });
        this.setVisible(true);
        this.revalidate();
        this.repaint();

    }

        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                Welt welt = new Welt();
//                try {
//                    new RisikoClientGUI(welt);
//                } catch (DoppelterNameException e) {
//                    throw new RuntimeException(e);
//                }
                JFrame fenster = null;
                try {
                    fenster = new MenuFenster(welt);
                } catch (RuntimeException e) {
                    throw new RuntimeException(e);
                }

                fenster.setAlwaysOnTop(true);
                fenster.setLocationRelativeTo(null);
            });
        }
}