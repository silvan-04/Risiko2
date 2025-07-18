package Risiko.ui.gui.panels;

import Risiko.domain.*;
import Risiko.domain.exceptions.*;
import Risiko.entities.*;
import Risiko.ui.gui.RisikoClientGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class ActionPanelOnline extends JPanel  implements Action{
    private Welt welt;
    private JFrame frame;
    private WeltPanel weltPanel;
    private int buttonClicked;
    private String angriffsLand;
    private String zielLand;
    private int einheiten;
    private JButton aktionsKnopf;
    private Spieler spieler;

    /**
     *
     * @param welt
     * @param frame
     * @param weltPanel
     */
    public ActionPanelOnline(Welt welt, RisikoClientGUI frame, WeltPanel weltPanel,Spieler spieler) {
        super();
        this.welt = welt;
        this.frame = (JFrame) frame;
        this.weltPanel = weltPanel;
        this.setLayout(new BorderLayout());
        this.buttonClicked = 0;
        this.einheiten = 0;
        this.spieler = spieler;

        aktionsKnopf = new JButton("Armee verteilen!");
        add(aktionsKnopf, BorderLayout.EAST);

        aktionsKnopf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                //Erste Phase
                if (welt.getPhase() == 0) {
                    if(buttonClicked == 0 && welt.aktiverSpieler().getEinheitskarten().size()>2) {
                        int kartenEntscheidung = 0;
                        if(!welt.handkartenlimit(welt.aktiverSpieler())){
                            kartenEntscheidung = JOptionPane.showConfirmDialog(frame, "Möchtest du Karten einlösen?", "Armee verteilen!", JOptionPane.YES_NO_OPTION);
                        }
                        if (kartenEntscheidung == 0 ){
                            String [] karten = new String[welt.aktiverSpieler().getEinheitskarten().size()];
                            for(int i =0;i<welt.aktiverSpieler().getEinheitskarten().size();i++){
                                karten[i] = welt.aktiverSpieler().getEinheitskarten().get(i).toString();
                            }
                            Einheitskarte karte1 = null;
                            Einheitskarte karte2 = null;
                            Einheitskarte karte3 = null;
                            int kartenAuswahl1 = JOptionPane.showOptionDialog(frame, "Wähle die erste Karte","Karte 1", 0, 3, null, karten, karten[0]);
                            if (kartenAuswahl1 == 0){
                                karte1 = welt.aktiverSpieler().getEinheitskarten().get(0);
                            }
                            if (kartenAuswahl1 == 1){
                                karte1 = welt.aktiverSpieler().getEinheitskarten().get(1);
                            }
                            if (kartenAuswahl1 == 2){
                                karte1 = welt.aktiverSpieler().getEinheitskarten().get(2);
                            }
                            if (kartenAuswahl1 == 3){
                                karte1 = welt.aktiverSpieler().getEinheitskarten().get(3);
                            }
                            if (kartenAuswahl1 == 4){
                                karte1 = welt.aktiverSpieler().getEinheitskarten().get(4);
                            }
                            int kartenAuswahl2 = JOptionPane.showOptionDialog(frame, "Wähle die zweite Karte","Karte 2", 0, 3, null, karten, karten[0]);
                            if (kartenAuswahl2 == 0){
                                karte2 = welt.aktiverSpieler().getEinheitskarten().get(0);
                            }
                            if (kartenAuswahl2 == 1){
                                karte2 = welt.aktiverSpieler().getEinheitskarten().get(1);
                            }
                            if (kartenAuswahl2 == 2){
                                karte2 = welt.aktiverSpieler().getEinheitskarten().get(2);
                            }
                            if (kartenAuswahl2 == 3){
                                karte2 = welt.aktiverSpieler().getEinheitskarten().get(3);
                            }
                            if (kartenAuswahl2 == 4){
                                karte2 = welt.aktiverSpieler().getEinheitskarten().get(4);
                            }
                            int kartenAuswahl3 = JOptionPane.showOptionDialog(frame, "Wähle die dritte Karte","Karte 3", 0, 3, null, karten, karten[0]);
                            if (kartenAuswahl3 == 0){
                                karte3 = welt.aktiverSpieler().getEinheitskarten().get(0);
                            }
                            if (kartenAuswahl3 == 1){
                                karte3 = welt.aktiverSpieler().getEinheitskarten().get(1);
                            }
                            if (kartenAuswahl3 == 2){
                                karte3 = welt.aktiverSpieler().getEinheitskarten().get(2);
                            }
                            if (kartenAuswahl3 == 3){
                                karte3 = welt.aktiverSpieler().getEinheitskarten().get(3);
                            }
                            if (kartenAuswahl3 == 4){
                                karte3 = welt.aktiverSpieler().getEinheitskarten().get(4);
                            }
                            try {
                                einheiten = welt.armeeVerteilung() + welt.kartenEinlösen(karte1, karte2, karte3);
                                buttonClicked++;
                                JOptionPane.showMessageDialog(frame, "Du kannst " + (welt.aktiverSpieler().getEinheitenRunde()+einheiten) + " verteilen! \nKlicke das Land an, welches verstärkt werden soll!", " Armee verteilen!", JOptionPane.INFORMATION_MESSAGE);
                            } catch (NotYourCardException | SymbolException | DoppelteKarteException ex) {
                                JOptionPane.showMessageDialog(frame, ex.getMessage(), "FEHLER!", JOptionPane.ERROR_MESSAGE);
                            }
                        }else if(kartenEntscheidung == 1){
                            buttonClicked++;
                            JOptionPane.showMessageDialog(frame, "Du kannst " + (welt.aktiverSpieler().getEinheitenRunde()+einheiten) + " verteilen! \nKlicke das Land an, welches verstärkt werden soll!", " Armee verteilen!", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else if (buttonClicked == 0) {
                        buttonClicked++;
                        JOptionPane.showMessageDialog(frame, "Du kannst " + (welt.aktiverSpieler().getEinheitenRunde()+einheiten) + " verteilen! \nKlicke das Land an, welches verstärkt werden soll!", " Armee verteilen!", JOptionPane.INFORMATION_MESSAGE);
                    } else if (buttonClicked == 1) {
                        if (weltPanel.getCountryClicked()) {
                            String id = weltPanel.getLastClickedCountry();
                            boolean stop = false;
                            try {
                                welt.isIdGueltig(id);
                            } catch (IdException | NotYourLandException ex) {
                                JOptionPane.showMessageDialog(frame, ex.getMessage(), "FEHLER!", JOptionPane.ERROR_MESSAGE);
                                stop = true;
                            }
                            if (!stop) {
                                try {
                                    int verschobenEinheiten = Integer.parseInt(JOptionPane.showInputDialog(frame, "Wie viele Einheiten möchtest du einsetzen? \n" + "Max: " + (welt.aktiverSpieler().getEinheitenRunde()+einheiten) + " Einheiten.", "Armee verteilen!", JOptionPane.QUESTION_MESSAGE));
                                    try {
                                        welt.aktiverSpieler().setEinheitenRunde(welt.truppenPlatzieren(welt.aktiverSpieler().getEinheitenRunde()+einheiten, verschobenEinheiten, id));
                                        buttonClicked = 0;
                                        weltPanel.setLastClickedCountry(null);
                                        weltPanel.setCountryClicked(false);
                                        if (welt.aktiverSpieler().getEinheitenRunde() == 0) {
                                            welt.naechstePhase();
                                            frame.revalidate();
                                            frame.repaint();
                                            aktionsKnopf.setText("Angreifen");
                                        }
                                    } catch (ArmeeException | IdException exc) {
                                        JOptionPane.showMessageDialog(frame, exc.getMessage(), "FEHLER!", JOptionPane.ERROR_MESSAGE);
                                    }
                                } catch (NumberFormatException exce) {
                                    JOptionPane.showMessageDialog(frame, "Es muss eine Zahl eingegeben werden!", "FEHLER!", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame, "Klicke erst ein Land an!", "FEHLER!", JOptionPane.ERROR_MESSAGE);
                        }
                    }

                    //Zweite Phase
                } else if (welt.getPhase() == 1) {
                    if (buttonClicked == 0) {
                        String [] options = {"Angreifen!","Nächste Phase!"};
                        int auswahl = JOptionPane.showOptionDialog(frame, "Was möchtest du tun ?", "Angriff!", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                        if (auswahl == 1) {
                            welt.naechstePhase();
                            frame.revalidate();
                            frame.repaint();
                            aktionsKnopf.setText("Armee verschieben");
                        }else {
                            JOptionPane.showMessageDialog(frame, "Klicke das Land an, mit dem du angreifen möchtest", "Angriff!", JOptionPane.INFORMATION_MESSAGE);
                            buttonClicked++;
                        }
                    } else if (buttonClicked == 1) {
                        if (weltPanel.getCountryClicked()) {
                            angriffsLand = weltPanel.getLastClickedCountry();
                            weltPanel.setLastClickedCountry(null);
                            weltPanel.setCountryClicked(false);
                            boolean stop = false;
                            try {
                                welt.isIdGueltig(angriffsLand);
                            } catch (IdException | NotYourLandException ex) {
                                JOptionPane.showMessageDialog(frame, ex.getMessage(), "FEHLER!", JOptionPane.ERROR_MESSAGE);
                                stop = true;
                            }
                            if (!stop) {
                                JOptionPane.showMessageDialog(frame, "Klicke das Land an, welches du angreifen möchtest", "Angriff!", JOptionPane.INFORMATION_MESSAGE);
                                buttonClicked++;
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame, "Klicke erst ein Land an!", "FEHLER!", JOptionPane.ERROR_MESSAGE);
                        }
                    } else if (buttonClicked == 2) {
                        if (weltPanel.getCountryClicked()) {
                            zielLand = weltPanel.getLastClickedCountry();
                            weltPanel.setLastClickedCountry(null);
                            weltPanel.setCountryClicked(false);
                            boolean stop2 = false;
                            try {
                                welt.isIdHostile(zielLand, angriffsLand);
                            } catch (IdException | YourLandException | NachbarException exc) {
                                JOptionPane.showMessageDialog(frame, exc.getMessage(), "FEHLER!", JOptionPane.ERROR_MESSAGE);
                                stop2 = true;
                                buttonClicked = 1;
                            }
                            if (!stop2) {
                                boolean stop3 = false;
                                int einheiten = 0;
                                String möglicheEinheiten = null;
                                if (welt.idToLand(angriffsLand).getArmee() > 3) {
                                    möglicheEinheiten = "Min:1 Max:3 Einheiten.";
                                } else if (welt.idToLand(angriffsLand).getArmee() > 2) {
                                    möglicheEinheiten = "Min:1 Max:2 Einheiten.";
                                } else if (welt.idToLand(angriffsLand).getArmee() > 1) {
                                    möglicheEinheiten = "Min:1 Max:1 Einheiten.";
                                }
                                try {
                                    einheiten = Integer.parseInt(JOptionPane.showInputDialog(frame, "Gib die Anzahl der Einheiten ein, welche angreifen sollen! \nMögliche Einheiten: " + möglicheEinheiten, "Angriff", JOptionPane.PLAIN_MESSAGE));
                                    welt.angreifbar(einheiten, welt.idToLand(angriffsLand), welt.idToLand(zielLand));
                                } catch (NumberFormatException exce) {
                                    JOptionPane.showMessageDialog(frame, "Es muss eine Zahl eingegeben werden!", "FEHLER!", JOptionPane.ERROR_MESSAGE);
                                    stop3 = true;
                                } catch (ArmeeException | NachbarException excep) {
                                    JOptionPane.showMessageDialog(frame, excep.getMessage(), "FEHLER!", JOptionPane.ERROR_MESSAGE);
                                    stop3 = true;
                                }
                                if(!stop3){
                                    boolean stop4 = false;
                                    int verteidigen = 0;
                                    if (welt.idToLand(zielLand).getArmee() > 1) {
                                        möglicheEinheiten = "Min:1 Max:2 Einheiten.";
                                    } else {
                                        möglicheEinheiten = "Min:1 Max:1 Einheiten.";
                                    }
                                    try {
                                        verteidigen = Integer.parseInt(JOptionPane.showInputDialog(frame, welt.idToLand(zielLand).getBesitzer().getName() + " dein Land " + welt.idToLand(zielLand).getName() + ", mit " + welt.idToLand(zielLand).getArmee() + " Einheiten, wird angegriffen! \n Gib die Zahl der Einheiten zur Verteidigung ein!" + möglicheEinheiten, "Angriff", JOptionPane.PLAIN_MESSAGE));
                                        welt.verteidigen(verteidigen, welt.idToLand(zielLand));
                                    } catch (NumberFormatException except) {
                                        JOptionPane.showMessageDialog(frame, "Es muss eine Zahl eingegeben werden!", "FEHLER!", JOptionPane.ERROR_MESSAGE);
                                        stop4 = true;
                                    } catch (ArmeeException excepti) {
                                        JOptionPane.showMessageDialog(frame, excepti.getMessage(), "FEHLER!", JOptionPane.ERROR_MESSAGE);
                                        stop4 = true;
                                    }
                                    if(!stop4) {
                                        java.util.List<Integer> angriffZahlen = welt.würfel(einheiten);
                                        java.util.List<Integer> verteidigungsZahlen = welt.würfel(verteidigen);
                                        JOptionPane.showMessageDialog(frame, welt.aktiverSpieler().getName() + " würfelt:" + angriffZahlen + "\n" + welt.idToLand(zielLand).getBesitzer().getName() + " würfelt:" + verteidigungsZahlen, "Würfel", JOptionPane.WARNING_MESSAGE);
                                        List<Integer> endTruppen = welt.Kampf(welt.idToLand(angriffsLand), einheiten, welt.idToLand(zielLand), verteidigen, angriffZahlen, verteidigungsZahlen);

                                        //Verschieben nach eroberung
                                        if (welt.idToLand(zielLand).getErobert()) {
                                            welt.idToLand(zielLand).setErobert(false);

                                            boolean fehler = true;
                                            do {
                                                try {
                                                    if ((welt.idToLand(angriffsLand).getArmee() - 1) != 0) {
                                                        welt.loserCheck();
                                                        if(!welt.winnerCheck().equals("")){
                                                            JOptionPane.showMessageDialog(frame,welt.winnerCheck(), "DU HAST GEWONNEN!", JOptionPane.INFORMATION_MESSAGE,new ImageIcon("win.png"));
                                                            frame.dispose();
                                                            break;
                                                        }
                                                        int verschieben = Integer.parseInt(JOptionPane.showInputDialog(frame, "Sie haben das Land eingenommen. Wie viele Einheiten sollen zusätzlich in das eroberte Land?" + "\nGib eine Zahl zwischen 0 und " + (welt.idToLand(angriffsLand).getArmee() - 1) + " ein:", "Gewonnen!", JOptionPane.PLAIN_MESSAGE));
                                                        welt.verschieben(welt.idToLand(angriffsLand), welt.idToLand(zielLand), verschieben, true);
                                                    }
                                                    fehler = false;
                                                    buttonClicked = 0;
                                                    angriffsLand = null;
                                                    zielLand = null;
                                                } catch (ArmeeException | NachbarException | NotYourLandException exceptio) {
                                                    JOptionPane.showMessageDialog(frame, exceptio.getMessage(), "FEHLER!", JOptionPane.ERROR_MESSAGE);
                                                } catch (NumberFormatException exception) {
                                                    JOptionPane.showMessageDialog(frame, "Es muss eine Zahl eingegeben werden!", "FEHLER!", JOptionPane.ERROR_MESSAGE);
                                                }
                                            } while (fehler);
                                        } else {
                                            buttonClicked = 0;
                                            angriffsLand = null;
                                            zielLand = null;
                                        }
                                    }
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame, "Klicke erst ein Land an!", "FEHLER!", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    //Dritte Phase
                } else {
                    if(buttonClicked == 0){
                        String [] options = {"Verschieben!","Nächster Spieler!"};
                        int auswahl = JOptionPane.showOptionDialog(frame, "Was möchstest du tun ?", "Verschieben!", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                        if (auswahl == 1) {
                            try {
                                welt.naechsterZug();
                                einheiten =0;
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            frame.revalidate();
                            frame.repaint();
                            aktionsKnopf.setText("Armee Verteilen");
                        }else {
                            JOptionPane.showMessageDialog(frame, "Du kannst nun Truppen verschieben! \nKlicke das Land an, aus dem die Truppen kommen.", "Armee verschieben!", JOptionPane.INFORMATION_MESSAGE);
                            buttonClicked++;
                        }
                    }else if(buttonClicked == 1){
                        if (weltPanel.getCountryClicked()) {
                            angriffsLand = weltPanel.getLastClickedCountry();
                            weltPanel.setLastClickedCountry(null);
                            weltPanel.setCountryClicked(false);
                            boolean stop = false;
                            try {
                                welt.isIdGueltig(angriffsLand);
                            } catch (IdException | NotYourLandException ex) {
                                JOptionPane.showMessageDialog(frame, ex.getMessage(), "FEHLER!", JOptionPane.ERROR_MESSAGE);
                                stop = true;
                            }
                            if(!stop){
                                JOptionPane.showMessageDialog(frame, "Klicke das Land an, in welches die Truppen verschoben werden sollen.", "Armee verschieben!", JOptionPane.INFORMATION_MESSAGE);
                                buttonClicked++;
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame, "Klicke erst ein Land an!", "FEHLER!", JOptionPane.ERROR_MESSAGE);
                        }
                    } else if (buttonClicked == 2) {
                        if (weltPanel.getCountryClicked()) {
                            zielLand = weltPanel.getLastClickedCountry();
                            weltPanel.setLastClickedCountry(null);
                            weltPanel.setCountryClicked(false);
                            try {
                                int verschieben = Integer.parseInt(JOptionPane.showInputDialog(frame, "Gib an wie viele Truppen verschieben möchtest. Du kannst bis zu "+ (welt.idToLand(angriffsLand).getArmee() - welt.idToLand(angriffsLand).getBewegteTruppen()-1) +" Truppen aus "+ welt.idToLand(angriffsLand).getName()+ " verschieben!", "Angriff", JOptionPane.PLAIN_MESSAGE));
                                welt.verschieben(welt.idToLand(angriffsLand),welt.idToLand(zielLand),verschieben,false);
                            } catch (NumberFormatException except) {
                                JOptionPane.showMessageDialog(frame, "Es muss eine Zahl eingegeben werden!", "FEHLER!", JOptionPane.ERROR_MESSAGE);
                            } catch (ArmeeException | NotYourLandException | IdException | NachbarException excepti) {
                                JOptionPane.showMessageDialog(frame, excepti.getMessage(), "FEHLER!", JOptionPane.ERROR_MESSAGE);
                            }
                            buttonClicked = 0;
                        } else {
                            JOptionPane.showMessageDialog(frame, "Klicke erst ein Land an!", "FEHLER!", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }

        });

        JButton missionsKnopf = new JButton("Mission");
        add(missionsKnopf, BorderLayout.WEST);
        JButton kartenKnopf = new JButton("Karten");
        add(kartenKnopf, BorderLayout.CENTER);

        missionsKnopf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                JOptionPane.showMessageDialog(frame, welt.getKartenverwaltung().getMissionskarten().get(spieler.getId()).beschreibung(), "Deine Mission!", JOptionPane.INFORMATION_MESSAGE);

            }
        });

        kartenKnopf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                JOptionPane.showMessageDialog(frame, !spieler.getEinheitskarten().isEmpty() ? spieler.getEinheitskarten() : "Du hast keine Karten im Besitz!", "Deine Karten!", JOptionPane.INFORMATION_MESSAGE);

            }
        });

    }
    public void setButton(boolean enabled) {
        aktionsKnopf.setEnabled(enabled);
    }
    public JButton getActionbutton() {
        return aktionsKnopf;
    }
    public void setSpieler(Spieler spieler){
        this.spieler=spieler;
    }
}

