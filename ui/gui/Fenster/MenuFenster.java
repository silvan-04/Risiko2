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

    private JTextField name1Field, name2Field, name3Field, name4Field, name5Field, name6Field;
    private List<JTextField> nameFields;
    private JLabel name1Label, name2Label, name3Label, name4Label, name5Label, name6Label;
    private JButton playButton;
    private JButton zurückButton;
    private JButton hinzuButton3,hinzuButton4,hinzuButton5,hinzuButton6;
    private JButton entButton4,entButton5,entButton6,entButton7;

    private Welt welt;
    private RisikoClientGUI gui;
    private List<String> spielerNamen;



    // Damit Input in anderen Klassen nutzbar ist:
    public java.util.List<String> getSpielerNamen() {
        return spielerNamen;
    }

    public void zeigeSetupPanel() {
        startPanel.setVisible(false);
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
        // Online-Logik…
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
        setupPanel = new JPanel();
        setupPanel.setLayout(null);
        setupPanel.setOpaque(false);
        setupPanel.setBounds(0, 0, 1000, 600);
        setupPanel.setVisible(false);
        backgroundPanel.add(setupPanel);

        // Spieler-Farbboxen
        JPanel box1 = farbbox(125, 132, 21, 21, "#1B3B6F");
        setupPanel.add(box1);
        JPanel box2 = farbbox(125, 172, 21, 21, "#8E2835");
        setupPanel.add(box2);
        JPanel box3 = farbbox(125, 212, 21, 21, "#728E4B");
        setupPanel.add(box3);
        box3.setVisible(false);
        JPanel box4 = farbbox(125, 252, 21, 21, "#5C215C");
        setupPanel.add(box4);
        box4.setVisible(false);
        JPanel box5 = farbbox(125, 292, 21, 21, "#BF5C00");
        setupPanel.add(box5);
        box5.setVisible(false);
        JPanel box6 = farbbox(125, 332, 21, 21, "#6F4E37");
        setupPanel.add(box6);
        box6.setVisible(false);

        // Eingabefelder
        name1Label = new JLabel("1. Spieler:");
        name1Label.setBounds(50, 130, 100, 25);
        name1Label.setForeground(Color.white);
        setupPanel.add(name1Label);
        name1Field = new JTextField();
        name1Field.setForeground(Color.white);
        name1Field.setBackground(Color.decode("#191C21"));
        name1Field.setBorder(BorderFactory.createLineBorder(Color.darkGray));
        name1Field.setBounds(160, 130, 200, 25);
        setupPanel.add(name1Field);

        name2Label = new JLabel("2. Spieler:");
        name2Label.setBounds(50, 170, 100, 25);
        name2Label.setForeground(Color.white);
        setupPanel.add(name2Label);
        name2Field = new JTextField();
        name2Field.setForeground(Color.white);
        name2Field.setBackground(Color.decode("#191C21"));
        name2Field.setBorder(BorderFactory.createLineBorder(Color.darkGray));
        name2Field.setBounds(160, 170, 200, 25);
        setupPanel.add(name2Field);

        name3Label = new JLabel("3. Spieler:");
        name3Label.setBounds(50, 210, 100, 25);
        name3Label.setForeground(Color.white);
        name3Label.setVisible(false);
        setupPanel.add(name3Label);
        name3Field = new JTextField();
        name3Field.setForeground(Color.white);
        name3Field.setBackground(Color.decode("#191C21"));
        name3Field.setBorder(BorderFactory.createLineBorder(Color.darkGray));
        name3Field.setBounds(160, 210, 200, 25);
        name3Field.setVisible(false);
        setupPanel.add(name3Field);

        name4Label = new JLabel("4. Spieler:");
        name4Label.setBounds(50, 250, 100, 25);
        name4Label.setForeground(Color.white);
        name4Label.setVisible(false);
        setupPanel.add(name4Label);
        name4Field = new JTextField();
        name4Field.setForeground(Color.white);
        name4Field.setBackground(Color.decode("#191C21"));
        name4Field.setBorder(BorderFactory.createLineBorder(Color.darkGray));
        name4Field.setBounds(160, 250, 200, 25);
        name4Field.setVisible(false);
        setupPanel.add(name4Field);

        name5Label = new JLabel("5. Spieler:");
        name5Label.setBounds(50, 290, 100, 25);
        name5Label.setForeground(Color.white);
        name5Label.setVisible(false);
        setupPanel.add(name5Label);
        name5Field = new JTextField();
        name5Field.setForeground(Color.white);
        name5Field.setBackground(Color.decode("#191C21"));
        name5Field.setBorder(BorderFactory.createLineBorder(Color.darkGray));
        name5Field.setBounds(160, 290, 200, 25);
        name5Field.setVisible(false);
        setupPanel.add(name5Field);

        name6Label = new JLabel("6. Spieler:");
        name6Label.setBounds(50, 330, 100, 25);
        name6Label.setForeground(Color.white);
        name6Label.setVisible(false);
        setupPanel.add(name6Label);
        name6Field = new JTextField();
        name6Field.setForeground(Color.white);
        name6Field.setBackground(Color.decode("#191C21"));
        name6Field.setBorder(BorderFactory.createLineBorder(Color.darkGray));
        name6Field.setBounds(160, 330, 200, 25);
        name6Field.setVisible(false);
        setupPanel.add(name6Field);


        // Zum Überprüfen, ob die Felder richtig ausgefüllt wurden.
        nameFields = new ArrayList<>(Arrays.asList(name1Field, name2Field, name3Field, name4Field, name5Field, name6Field));


        // Button Spieler hinzufügen/entfernen
        hinzuButton3 = new JButton("+");
        hinzuButton3.setBounds(160, 210, 42, 42);
        hinzuButton3.setFont(new Font("Arial", Font.BOLD, 14));
        hinzuButton3.setVisible(true);
        hinzuButton3.setBackground(Color.gray);
        setupPanel.add(hinzuButton3);
        hinzuButton3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                name3Label.setVisible(true);
                name3Field.setVisible(true);
                hinzuButton3.setVisible(false);
                hinzuButton4.setVisible(true);
                entButton4.setVisible(true);
                box3.setVisible(true);
            }
        });

        entButton4 = new JButton("-");
        entButton4.setBounds(215, 250, 42, 42);
        entButton4.setFont(new Font("Arial", Font.BOLD, 14));
        entButton4.setVisible(false);
        entButton4.setBackground(Color.gray);
        setupPanel.add(entButton4);
        entButton4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                name3Label.setVisible(false);
                name3Field.setVisible(false);
                hinzuButton3.setVisible(true);
                hinzuButton4.setVisible(false);
                entButton4.setVisible(false);
                box3.setVisible(false);
            }
        });

        hinzuButton4 = new JButton("+");
        hinzuButton4.setBounds(160, 250, 42, 42);
        hinzuButton4.setFont(new Font("Arial", Font.BOLD, 14));
        hinzuButton4.setVisible(false);
        hinzuButton4.setBackground(Color.gray);
        setupPanel.add(hinzuButton4);
        hinzuButton4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                name4Label.setVisible(true);
                name4Field.setVisible(true);
                hinzuButton4.setVisible(false);
                hinzuButton5.setVisible(true);
                entButton4.setVisible(false);
                entButton5.setVisible(true);
                box4.setVisible(true);

            }
        });

        entButton5 = new JButton("-");
        entButton5.setBounds(215, 290, 42, 42);
        entButton5.setFont(new Font("Arial", Font.BOLD, 14));
        entButton5.setVisible(false);
        entButton5.setBackground(Color.gray);
        setupPanel.add(entButton5);
        entButton5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                name4Label.setVisible(false);
                name4Field.setVisible(false);
                hinzuButton4.setVisible(true);
                hinzuButton5.setVisible(false);
                entButton5.setVisible(false);
                entButton4.setVisible(true);
                box4.setVisible(false);
            }
        });

        hinzuButton5 = new JButton("+");
        hinzuButton5.setBounds(160, 290, 42, 42);
        hinzuButton5.setFont(new Font("Arial", Font.BOLD, 14));
        hinzuButton5.setVisible(false);
        hinzuButton5.setBackground(Color.gray);
        setupPanel.add(hinzuButton5);
        hinzuButton5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                name5Label.setVisible(true);
                name5Field.setVisible(true);
                hinzuButton5.setVisible(false);
                hinzuButton6.setVisible(true);
                entButton5.setVisible(false);
                entButton6.setVisible(true);
                box5.setVisible(true);

            }
        });

        entButton6 = new JButton("-");
        entButton6.setBounds(215, 330, 42, 42);
        entButton6.setFont(new Font("Arial", Font.BOLD, 14));
        entButton6.setVisible(false);
        entButton6.setBackground(Color.gray);
        setupPanel.add(entButton6);
        entButton6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                name5Label.setVisible(false);
                name5Field.setVisible(false);
                hinzuButton5.setVisible(true);
                hinzuButton6.setVisible(false);
                entButton6.setVisible(false);
                entButton5.setVisible(true);
                box5.setVisible(false);
            }
        });

        hinzuButton6 = new JButton("+");
        hinzuButton6.setBounds(160, 330, 42, 42);
        hinzuButton6.setFont(new Font("Arial", Font.BOLD, 14));
        hinzuButton6.setVisible(false);
        hinzuButton6.setBackground(Color.gray);
        setupPanel.add(hinzuButton6);
        hinzuButton6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                name6Label.setVisible(true);
                name6Field.setVisible(true);
                hinzuButton6.setVisible(false);
                //hinzuButton5.setVisible(true);
                entButton6.setVisible(false);
                entButton7.setVisible(true);
                box6.setVisible(true);
            }
        });

        entButton7 = new JButton("-");
        entButton7.setBounds(215, 370, 42, 42);
        entButton7.setFont(new Font("Arial", Font.BOLD, 14));
        entButton7.setVisible(false);
        entButton7.setBackground(Color.gray);
        setupPanel.add(entButton7);
        entButton7.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                name6Label.setVisible(false);
                name6Field.setVisible(false);
                hinzuButton6.setVisible(true);
                entButton7.setVisible(false);
                entButton6.setVisible(true);
                box6.setVisible(false);
            }
        });

        // Play-Button
        playButton = new JButton("Play");
        playButton.setBounds(160, 500, 200, 40);
        playButton.setFont(new Font("Arial", Font.BOLD, 18));
        playButton.setBackground(Color.lightGray);
        setupPanel.add(playButton);

        /**
         * überprüft Eingabe von den Textfeldern. gibt dann Spielernamen weiter.
         * schließt Startmenü und öffnet das Spiel
         */
        playButton.addActionListener(e -> {
            java.util.List<String> namen = new ArrayList<>();
            boolean leer=false;
            boolean richtigeEingabe=true;
            for(JTextField textfeld : nameFields) {
                if (textfeld != null) {
                    if (!leer) {
                        if (textfeld.getText().equals("")) {
                            leer = true;
                        } else {
                            namen.add(textfeld.getText());
                        }
                    } else {
                        if (!textfeld.getText().isEmpty()) {
                            namen.clear();
                            richtigeEingabe = false;
                            JOptionPane.showMessageDialog(this, "Bitte die Namen nacheinander eingeben!");
                            break;
                        } else if (namen.size() < 2) {
                            namen.clear();
                            richtigeEingabe = false;
                            JOptionPane.showMessageDialog(this, "Es benötigt mindestens 2 Spieler!");
                            break;
                        }
                    }
                }
            }
            boolean spielerErstellt = false;
            if(richtigeEingabe){
                for(String name : namen){
                    try {
                        welt.spielerHinzufuegen(name);
                        spielerErstellt = true;
                    } catch (DoppelterNameException ex) {
                        spielerErstellt = false;
                        JOptionPane.showMessageDialog(this, "Keine doppelten Namen!");
                        break;
                    }
                }
            }
            if(spielerErstellt){
                welt.setAmZug(0,true);
                welt.laenderErstellung();
                welt.laenderVerteilung();
                welt.kartenErstellung();
                welt.kontinentAktualisieren();
                welt.aktiverSpieler().setEinheitenRunde(welt.armeeVerteilung());
                JOptionPane ms = new JOptionPane();
                this.dispose();
                this.gui = new RisikoClientGUI(this.welt);
                this.gui.repaint();
            }
        });

        // Wechselt zum Startfenster.
        zurückButton = new JButton("zurück");
        zurückButton.setBounds(50, 500, 100, 40);
        zurückButton.setFont(new Font("Arial", Font.BOLD, 18));
        zurückButton.setBackground(Color.lightGray);
        setupPanel.add(zurückButton);

        zurückButton.addActionListener(e -> {
            startPanel.setVisible(true);
            setupPanel.setVisible(false);
        });

        setVisible(true);

    }
}




