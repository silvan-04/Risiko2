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
import java.util.ArrayList;
import java.util.List;

public class ActionPanel extends JPanel {
    private Welt welt;
    private JFrame frame;
    private WeltPanel weltPanel;
    private int buttonClicked;
    private String angriffsLand;
    private String zielLand;

    /**
     *
     * @param welt
     * @param frame
     * @param weltPanel
     */
    public ActionPanel(Welt welt, RisikoClientGUI frame, WeltPanel weltPanel) {
        super();
        this.welt = welt;
        this.frame = (JFrame) frame;
        this.weltPanel = weltPanel;
        this.setLayout(new BorderLayout());
        this.buttonClicked = 0;

        JButton aktionsKnopf = new JButton("Armee verteilen!");
        add(aktionsKnopf, BorderLayout.EAST);

        aktionsKnopf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Erste Phase
                if (welt.getPhase() == 0) {
//                    int auswahl = JOptionPane.showConfirmDialog(frame, "Möchtest du Karten einlösen?", "Armee verteilen!", JOptionPane.YES_NO_OPTION);
//                   if (auswahl == 1) {
//                       armeeMitKarten();
//                    }
                    if (buttonClicked == 0) {
                        buttonClicked++;
                        JOptionPane.showMessageDialog(frame, "Du kannst " + welt.aktiverSpieler().getEinheitenRunde() + " verteilen! \n Klicke das Land an, welches verstärkt werden soll!", " Armee verteilen!", JOptionPane.INFORMATION_MESSAGE);
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
                                    int verschobenEinheiten = Integer.parseInt(JOptionPane.showInputDialog(frame, "Wie viele Einheiten möchtest du einsetzen? \n " + "Max: " + welt.aktiverSpieler().getEinheitenRunde() + " Einheiten.", "Armee verteilen!", JOptionPane.QUESTION_MESSAGE));
                                    try {
                                        welt.aktiverSpieler().setEinheitenRunde(welt.truppenPlatzieren(welt.aktiverSpieler().getEinheitenRunde(), verschobenEinheiten, id));
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
                        int auswahl = JOptionPane.showOptionDialog(frame, "Was möchstest du tun ?", "Angriff!", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
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
                            System.out.println(angriffsLand);
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
                            System.out.println(zielLand);
                            weltPanel.setLastClickedCountry(null);
                            weltPanel.setCountryClicked(false);
                            boolean stop2 = false;
                            try {
                                welt.isIdHostile(zielLand, angriffsLand);
                            } catch (IdException | YourLandException | NachbarException exc) {
                                JOptionPane.showMessageDialog(frame, exc.getMessage(), "FEHLER!", JOptionPane.ERROR_MESSAGE);
                                stop2 = true;
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
                                        einheiten = Integer.parseInt(JOptionPane.showInputDialog(frame, "Gib die Anzahl der Einheiten ein, welche angreifen sollen! \n Mögliche Einheiten: " + möglicheEinheiten, "Angriff", JOptionPane.PLAIN_MESSAGE));
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
                                                        int verschieben = Integer.parseInt(JOptionPane.showInputDialog(frame, "Sie haben das Land eingenommen. Wie viele Einheiten sollen zusätzlich in das eroberte Land?" + "\n Gib eine Zahl zwischen 0 und " + (welt.idToLand(angriffsLand).getArmee() - 1) + " ein:", "Gewonnen!", JOptionPane.PLAIN_MESSAGE));
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
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            frame.revalidate();
                            frame.repaint();
                            aktionsKnopf.setText("Armee Verteilen");
                        }else {
                            JOptionPane.showMessageDialog(frame, "Du kannst nun Truppen verschieben! \n Klicke das Land an, aus dem die Truppen kommen.", "Armee verschieben!", JOptionPane.INFORMATION_MESSAGE);
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
                                int verschieben = Integer.parseInt(JOptionPane.showInputDialog(frame, "Gib an wie viele Truppen verschieben möchtest.", "Angriff", JOptionPane.PLAIN_MESSAGE));
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

        missionsKnopf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                JOptionPane.showMessageDialog(frame, welt.getKartenverwaltung().getMissionskarten().get(welt.aktiverSpieler().getId()).beschreibung(), "Deine Mission!", JOptionPane.INFORMATION_MESSAGE);

            }
        });
    }
}

