package Risiko.ui.gui.Fenster;
import Risiko.domain.*;
import Risiko.domain.exceptions.DoppelterNameException;
import Risiko.domain.exceptions.KeinSpeicherstandException;
import Risiko.ui.gui.RisikoClientGUI;
import Risiko.ui.gui.panels.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuFenster extends JFrame {
    private JLabel titelLabel;
    private JPanel setupPanel;
    private StartMenuPanel startPanel;
    private OnlineMenuPanel onlineMenu;

    private Welt welt;
    private RisikoClientGUI gui;
    private List<String> spielerNamen;

    // Damit Input in anderen Klassen nutzbar ist:
    public java.util.List<String> getSpielerNamen() {
        return spielerNamen;
    }

    /**
     * Konstruktor für das Hauptmenü-Fenster.
     * Initialisiert das JFrame mit Titel, Größe und unveränderlichem Layout,
     * lädt das Hintergrundbild, richtet die Start-, Setup- und Online-Panels ein
     * und stellt das Fenster sichtbar.
     *
     * @param welt die im Menü für Lade- und Start-Operationen verwendet wird
     * @throws RuntimeException falls das Hintergrundbild oder andere Ressourcen nicht geladen werden können
     */
    public MenuFenster(Welt welt) throws RuntimeException {
        this.welt = welt;
        setTitle("RISIKO");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);


        /**
         * Hintergrund bild für StartMenü
         */
        JPanel backgroundPanel = new JPanel() {
            private final Image hintergrund = new ImageIcon("banner.png").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(hintergrund, 0, 0, getWidth(), getHeight(), this);
            }
        };

        backgroundPanel.setLayout(null);
        backgroundPanel.setBounds(0, 0, 1000, 600);
        setContentPane(backgroundPanel);

        startPanel = new StartMenuPanel(this);
        startPanel.setBounds(0, 0, 1000, 600);
        startPanel.setOpaque(false);
        backgroundPanel.add(startPanel);

        // Titel
        titelLabel = new JLabel("Risiko - Prog 2 Projekt");
        titelLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titelLabel.setForeground(Color.white);
        titelLabel.setBounds(20, 30, 500, 30);
        backgroundPanel.add(titelLabel);

        // Setup Panel
        setupPanel = new SpielerErstellenPanel(welt, this) ;
        backgroundPanel.add(setupPanel);

        //OnlineMenu
        onlineMenu = new OnlineMenuPanel(this);
        backgroundPanel.add(onlineMenu);

        setVisible(true);

    }

    /**
     * Blendet das Start- und das Online-Menü aus und zeigt das Setup-Panel
     * zur Spielererstellung an.
     */
    public void zeigeSetupPanel() {
        startPanel.setVisible(false);
        onlineMenu.setVisible(false);
        setupPanel.setVisible(true);
    }

    /**
     * Lädt einen gespeicherten Spielstand und startet die Risiko-Client-GUI.
     * Schließt das aktuelle Menü-Fenster. Falls kein Speicherstand existiert,
     * wird eine entsprechende Fehlermeldung angezeigt.
     *
     * @throws IOException            falls beim Lesen der Datei ein I/O-Fehler auftritt
     * @throws ClassNotFoundException falls die gespeicherten Objekte nicht geladen werden können
     */
    public void loadGame() throws IOException, ClassNotFoundException {
        try{
            welt.laden();
            this.dispose();
            new RisikoClientGUI(welt);
        }catch (KeinSpeicherstandException e){
            JOptionPane.showMessageDialog(this, "Kein Speicherstand vorhanden!");
        }
    }

    /**
     * Wechselt in den Online-Modus,
     * indem das Start-Panel ausgeblendet und das Online-Menü sichtbar gemacht wird.
     */
    public void startOnlineMode() {
        startPanel.setVisible(false);
        onlineMenu.setVisible(true);
    }

    /**
     * Erstellt eine farbige Box zum Darstellen einer Spielerfarbe.
     *
     * @param x   X-Koordinate der Box
     * @param y   Y-Koordinate der Box
     * @param w   Breite der Box
     * @param h   Höhe der Box
     * @param hex Farbcode im Hex
     * @return ein JPanel mit der angegebenen Farbe und Größe
     */
    private JPanel farbbox(int x, int y, int w, int h, String hex) {
        JPanel box = new JPanel();
        box.setBackground(Color.decode(hex));
        box.setBounds(x, y, w, h);
        return box;
    }

    /**
     * Setzt die Haupt-Spieloberfläche, damit das Menü mit der RisikoClientGUI interagieren kann.
     *
     * @param gui die Instanz der RisikoClientGUI
     */
    public void setGui(RisikoClientGUI gui) {
        this.gui = gui;
    }

    /**
     * Macht das Start-Panel sichtbar.
     */
    public void startVisible(){
        startPanel.setVisible(true);
    }
}




