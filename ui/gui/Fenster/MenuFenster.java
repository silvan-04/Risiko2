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

    public void zeigeSetupPanel() {
        startPanel.setVisible(false);
        onlineMenu.setVisible(false);
        setupPanel.setVisible(true);
    }

    /**
     * Lade Spielstand aus Speicher und schießt Menü
     * @throws IOException
     * @throws ClassNotFoundException
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


    public void startOnlineMode() {
        startPanel.setVisible(false);
        onlineMenu.setVisible(true);
    }

    /**
     * Boxen für Spielerfarben
     * @param x
     * @param y
     * @param w
     * @param h
     * @param hex
     * @return
     */
    private JPanel farbbox(int x, int y, int w, int h, String hex) {
        JPanel box = new JPanel();
        box.setBackground(Color.decode(hex));
        box.setBounds(x, y, w, h);
        return box;
    }

    /**
     * Start-Fenstereinstellung
     * @param welt
     * @throws RuntimeException
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
    public void setGui(RisikoClientGUI gui) {
        this.gui = gui;
    }
    public void startVisible(){
        startPanel.setVisible(true);
    }
}




