package Risiko.Server.src;

import Risiko.commands.GameCommand;
import Risiko.commands.GameCommandType;
import Risiko.domain.exceptions.*;
import Risiko.entities.Einheitskarte;
import Risiko.entities.Spieler;
import Risiko.domain.Welt;
import Risiko.events.GameActionEvent;
import Risiko.events.GameControlEvent;
import Risiko.events.GameEvent;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Random;


public class ClientRequestProcessor {
    private static final int NO_OF_PLAYERS = 2;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Welt welt;
    private GameServer gameServer;
    private int buttonClicked;
    private String angriffsLand;
    private String zielLand;
    private int einheiten;

    public ClientRequestProcessor(Socket socket,Welt welt,GameServer gameServer) {
        this.socket = socket;
        this.welt = welt;
        this.gameServer = gameServer;
        this.buttonClicked = 0;
        this.einheiten = 0;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            try {
                socket.close();
            } catch (IOException ioe) {

            }
        }
    }
    public ObjectOutputStream getOut(){
        return out;
    }

    public ObjectInputStream getIn(){
        return in;
    }

    public void handleClient() throws IOException, ClassNotFoundException, DoppelterNameException {
        while (true) {
            Object obj = in.readObject();
            if (obj instanceof GameCommand cmd) {
                switch (cmd.getType()) {
                    case GameCommandType.GAME_START -> startGame();
                    case GameCommandType.NEXT_TURN -> nextTurn();
                    case GameCommandType.GAME_ACTION -> someGameAction();
                    case GameCommandType.VERTEIDIGUNG -> verteidung(cmd);
                }
            } else if (obj instanceof Spieler player) {
                addPlayer(player.getName());

            }
        }
    }

    /*
     * Game-setup: new players
     * (starts game, after second player is registered)
     *
     * (non-Javadoc)
     * @see prog2.vl.gameloop.common.ServerRemote#addPlayer(prog2.vl.gameloop.common.Spieler)
     */
    public void addPlayer(String spieler) throws IOException {
        try {
            welt.spielerHinzufuegen(spieler,true);
            out.writeObject(true);
            out.flush();
            out.writeObject(welt.getSpielerListe().getLast());
            out.flush();
            gameServer.notifyListeners(new GameControlEvent(welt,GameControlEvent.GameControlEventType.PLAYER_JOINED));
        }catch(DoppelterNameException e){
            out.writeObject(false);
            out.flush();
        }

        if (welt.getSpielerListe().size() == 6) {
            startGame();
        }else if (welt.getSpielerListe().size() > NO_OF_PLAYERS-1) {
            // signal an spieler1 (server erstellt) das er starten kann
            gameServer.notifyListeners(new GameControlEvent(welt, GameControlEvent.GameControlEventType.GAME_CAN_START));
        }
    }

    public void startGame() throws IOException {
        welt.setAmZug(0, true);
        welt.laenderErstellung();
        welt.laenderVerteilung();
        welt.kartenErstellung();
        welt.kontinentAktualisieren();
        welt.aktiverSpieler().setEinheitenRunde(welt.armeeVerteilung());
        // Notify clients: info "game has started"
        gameServer.notifyListeners(new GameControlEvent(welt, GameControlEvent.GameControlEventType.GAME_STARTED));
    }



    /*
     * Controlling the game loop.
     * (Server is in full control of game status!)
     *
     * (non-Javadoc)
     * @see prog2.vl.gameloop.common.ServerRemote#getTurn()
     */
    public void nextTurn() throws IOException {
        welt.naechsterZug();
        gameServer.notifyListeners(new GameControlEvent(welt, GameControlEvent.GameControlEventType.NEXT_TURN));
        updateClientWelt();
    }

    /*
     * One of probably many game actions that are executed by the clients.
     *
     * (non-Javadoc)
     * @see prog2.vl.gameloop.common.ServerRemote#someGameAction()
     */
    public void someGameAction() throws IOException, ClassNotFoundException {

        if(welt.getPhase()==0){
            erstePhase();
        } else if (welt.getPhase()==1) {
            zweitePhase();
        }else if (welt.getPhase()==2) {
            drittePhase();
        }
//        // Simulate one (of three possible) actions
//        int actionID = new Random().nextInt(3);
//        GameActionEvent.GameActionEventType type = GameActionEvent.GameActionEventType.values()[actionID];
//        // Hier: Aktion ausführen...
//        gameServer.notifyListeners(new GameActionEvent(welt.aktiverSpieler(),welt.getSpielerListe().get(((welt.aktiverSpieler().getId())+1)% welt.getSpielerListe().size()), GameActionEvent.GameActionEventType.ATTACK,welt));

        // Game over?
    }

    public void verteidung(GameCommand gC) throws IOException {
        gameServer.notifyListeners(new GameActionEvent(gC.getSpieler(),welt,gC.getPayload()));
    }

    private void updateClientWelt() throws IOException {
        gameServer.notifyListeners(new GameActionEvent(welt.aktiverSpieler(),GameActionEvent.GameActionEventType.UPDATE,welt));
    }

    public void erstePhase() throws IOException, ClassNotFoundException {
        System.out.println("will phase senden "+ welt.getPhase());
        out.writeObject(Integer.valueOf(0));
        out.flush();
        System.out.println("phase senden");
        if(buttonClicked == 0 && welt.aktiverSpieler().getEinheitskarten().size()>2) {
            out.writeObject(Integer.valueOf(0));
            out.flush();
            int kartenEntscheidung = 0;
            if(!welt.handkartenlimit(welt.aktiverSpieler())){
                out.writeObject(false);
                out.flush();
                kartenEntscheidung = (int)in.readObject();
            }else{
                out.writeObject(true);
                out.flush();
            }
            Einheitskarte karte1;
            Einheitskarte karte2;
            Einheitskarte karte3;
            if (kartenEntscheidung == 0 ){
                karte1 = (Einheitskarte)in.readObject();
                karte2 = (Einheitskarte)in.readObject();
                karte3 = (Einheitskarte)in.readObject();

                try {
                    einheiten = welt.armeeVerteilung() + welt.kartenEinlösen(karte1, karte2, karte3);
                    out.writeObject(true);
                    out.flush();
                    buttonClicked++;
                    out.writeObject(einheiten);
                    out.flush();
                } catch (NotYourCardException | SymbolException | DoppelteKarteException ex) {
                    out.writeObject(false);
                    out.flush();
                    out.writeObject(ex);
                    out.flush();
                }
            }else if(kartenEntscheidung == 1){
                buttonClicked++;
            }
        } else if (buttonClicked == 0) {
            out.writeObject(Integer.valueOf(1));
            out.flush();
            buttonClicked++;
        } else if (buttonClicked == 1) {
            out.writeObject(Integer.valueOf(2));
            out.flush();
            boolean countryClicked = (boolean)in.readObject();
            if (countryClicked) {
                String id =  (String) in.readObject();
                boolean stop = false;
                try {
                    welt.isIdGueltig(id);
                    out.writeObject(stop);
                    out.flush();
                } catch (IdException | NotYourLandException ex) {
                    stop = true;
                    out.writeObject(stop);
                    out.flush();
                    out.writeObject(ex);
                    out.flush();
                }
                if (!stop) {
                    int einheiten = (int)in.readObject();
                    int verschobenEinheiten =(int) in.readObject();
                    try {
                        welt.aktiverSpieler().setEinheitenRunde(welt.truppenPlatzieren(welt.aktiverSpieler().getEinheitenRunde()+einheiten, verschobenEinheiten, id));
                        buttonClicked = 0;
                        out.writeObject(true);
                        out.flush();
                        if (welt.aktiverSpieler().getEinheitenRunde() == 0) {
                            welt.naechstePhase();
                            out.writeObject(true);
                            out.flush();
                            updateClientWelt();
//                              gameServer.notifyListeners(new GameActionEvent(welt.aktiverSpieler(),GameActionEvent.GameActionEventType.NEXT_PHASE,welt));
                        }else{
                            out.writeObject(false);
                            updateClientWelt();
                            out.flush();
                        }
                    } catch (ArmeeException | IdException exc) {
                        out.writeObject(false);
                        out.flush();
                        out.writeObject(exc);
                        out.flush();
                    }
                }
            }
        }
    }

    public void zweitePhase() throws IOException, ClassNotFoundException {
        out.writeObject(Integer.valueOf(1));
        out.flush();
        if (buttonClicked == 0) {
            out.writeObject(Integer.valueOf(0));
            out.flush();
            int auswahl = (int) in.readObject();
            if (auswahl == 1) {
                welt.naechstePhase();
                updateClientWelt();
            } else {
                buttonClicked++;
            }
        } else if (buttonClicked == 1) {
            out.writeObject(Integer.valueOf(1));
            out.flush();
            boolean countryClicked = (boolean) in.readObject();
            if (countryClicked) {
                angriffsLand = (String) in.readObject();
                boolean stop = false;
                try {
                    welt.isIdGueltig(angriffsLand);
                    out.writeObject(stop);
                    out.flush();
                    buttonClicked++;
                } catch (IdException | NotYourLandException ex) {
                    stop = true;
                    out.writeObject(stop);
                    out.flush();
                    out.writeObject(ex);
                    out.flush();
                }
            }
        } else if (buttonClicked == 2) {
            out.writeObject(Integer.valueOf(2));
            out.flush();
            boolean countryClicked = (boolean) in.readObject();
            if (countryClicked) {
                zielLand = (String) in.readObject();
                boolean stop2 = false;
                try {
                    welt.isIdHostile(zielLand, angriffsLand);
                    out.writeObject(stop2);
                    out.flush();
                } catch (IdException | YourLandException | NachbarException exc) {
                    stop2 = true;
                    out.writeObject(stop2);
                    out.flush();
                    out.writeObject(exc);
                    out.flush();
                    buttonClicked = 1;
                }
                if (!stop2) {
                    boolean stop3 = false;
                    int einheitenAngriff = 0;
                    if (welt.idToLand(angriffsLand).getArmee() > 3) {
                        out.writeObject("Max:3 Einheiten.");
                    } else if (welt.idToLand(angriffsLand).getArmee() > 2) {
                        out.writeObject("Max:2 Einheiten.");
                    } else if (welt.idToLand(angriffsLand).getArmee() > 1) {
                        out.writeObject("Max:1 Einheiten.");
                    }
                    stop3 = (boolean) in.readObject();
                    if (!stop3) {
                        einheitenAngriff = (int) in.readObject();
                        try {
                            welt.angreifbar(einheitenAngriff, welt.idToLand(angriffsLand), welt.idToLand(zielLand));
                            out.writeObject(stop3);
                            out.flush();
                        } catch (ArmeeException | NachbarException excep) {
                            stop3 = true;
                            out.writeObject(stop3);
                            out.flush();
                            out.writeObject(excep);
                            out.flush();
                        }
                    }
                    if (!stop3) {
                        boolean stop4 = false;

                        String möglicheEinheiten = null;
                        if (welt.idToLand(zielLand).getArmee() > 1) {
                            möglicheEinheiten = "Max:2 Einheiten.";
                        } else {
                            möglicheEinheiten = "Max:1 Einheit.";
                        }
                        gameServer.notifyListeners(new GameActionEvent(welt.aktiverSpieler(), welt.idToLand(zielLand).getBesitzer(), möglicheEinheiten, zielLand, welt));

                        try {

                            int verteidigen = (Integer) in.readObject();
                            welt.verteidigen(verteidigen, welt.idToLand(zielLand));
                            out.writeObject(false);
                            out.flush();
                        } catch (NumberFormatException except) {
                            stop4 = true;
                            out.writeObject(stop4);
                            out.flush();
                            out.writeObject(except);
                            out.flush();
                        } catch (ArmeeException excepti) {
                            stop4 = true;
                            out.writeObject(stop4);
                            out.flush();
                            out.writeObject(excepti);
                            out.flush();
                        }


//                            if (!stop4) {
//                                java.util.List<Integer> angriffZahlen = welt.würfel(einheitenAngriff);
//                                java.util.List<Integer> verteidigungsZahlen = welt.würfel(verteidigen);
//                                JOptionPane.showMessageDialog(frame, welt.aktiverSpieler().getName() + " würfelt:" + angriffZahlen + "\n" + welt.idToLand(zielLand).getBesitzer().getName() + " würfelt:" + verteidigungsZahlen, "Würfel", JOptionPane.WARNING_MESSAGE);
//                                List<Integer> endTruppen = welt.Kampf(welt.idToLand(angriffsLand), einheitenAngriff, welt.idToLand(zielLand), verteidigen, angriffZahlen, verteidigungsZahlen);
//
//                                //Verschieben nach eroberung
//                                if (welt.idToLand(zielLand).getErobert()) {
//                                    welt.idToLand(zielLand).setErobert(false);
//
//                                    boolean fehler = true;
//                                    do {
//                                        try {
//                                            if ((welt.idToLand(angriffsLand).getArmee() - 1) != 0) {
//                                                welt.loserCheck();
//                                                if (!welt.winnerCheck().equals("")) {
//                                                    JOptionPane.showMessageDialog(frame, welt.winnerCheck(), "DU HAST GEWONNEN!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("win.png"));
//                                                    frame.dispose();
//                                                    break;
//                                                }
//                                                int verschieben = Integer.parseInt(JOptionPane.showInputDialog(frame, "Sie haben das Land eingenommen. Wie viele Einheiten sollen zusätzlich in das eroberte Land?" + "\nGib eine Zahl zwischen 0 und " + (welt.idToLand(angriffsLand).getArmee() - 1) + " ein:", "Gewonnen!", JOptionPane.PLAIN_MESSAGE));
//                                                welt.verschieben(welt.idToLand(angriffsLand), welt.idToLand(zielLand), verschieben, true);
//                                            }
//                                            fehler = false;
//                                            buttonClicked = 0;
//                                            angriffsLand = null;
//                                            zielLand = null;
//                                        } catch (ArmeeException | NachbarException | NotYourLandException exceptio) {
//                                            JOptionPane.showMessageDialog(frame, exceptio.getMessage(), "FEHLER!", JOptionPane.ERROR_MESSAGE);
//                                        } catch (NumberFormatException exception) {
//                                            JOptionPane.showMessageDialog(frame, "Es muss eine Zahl eingegeben werden!", "FEHLER!", JOptionPane.ERROR_MESSAGE);
//                                        }
//                                    } while (fehler);
//                                } else {
//                                    buttonClicked = 0;
//                                    angriffsLand = null;
//                                    zielLand = null;
//                                }
//                            }
//                        }
//                    }
//                }else {
//                    JOptionPane.showMessageDialog(frame, "Klicke erst ein Land an!", "FEHLER!", JOptionPane.ERROR_MESSAGE);
//                }
                    }
                }
            }
        }

    }

    public void drittePhase() throws IOException, ClassNotFoundException {
        out.writeObject(Integer.valueOf(2));
        out.flush();
        if(buttonClicked == 0){
            out.writeObject(Integer.valueOf(0));
            out.flush();
            int auswahl = (Integer) in.readObject();
            if (auswahl == 1) {
                try {
                    nextTurn();
                    einheiten = 0;
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                buttonClicked++;
            }
        }else if(buttonClicked == 1){
            out.writeObject(Integer.valueOf(1));
            out.flush();
            boolean countryClicked = (boolean) in.readObject();
            if (countryClicked) {
                angriffsLand = (String) in.readObject();
                boolean stop = false;
                try {
                    welt.isIdGueltig(angriffsLand);
                    out.writeObject(stop);
                    out.flush();
                    buttonClicked++;
                } catch (IdException | NotYourLandException ex) {
                    stop = true;
                    out.writeObject(stop);
                    out.flush();
                    out.writeObject(ex);
                    out.flush();
                }
            }
        } else if (buttonClicked == 2) {
            out.writeObject(Integer.valueOf(2));
            out.flush();
            boolean countryClicked = (boolean) in.readObject();
            if (countryClicked) {
                zielLand = (String) in.readObject();
                out.writeObject(Integer.valueOf((welt.idToLand(angriffsLand).getArmee() - welt.idToLand(angriffsLand).getBewegteTruppen()-1)));
                out.flush();
                out.writeObject(welt.idToLand(angriffsLand).getName());
                out.flush();
                int verschieben = (Integer) in.readObject();
                if(verschieben != -1){
                    try {
                        welt.verschieben(welt.idToLand(angriffsLand),welt.idToLand(zielLand),verschieben,false);
                        out.writeObject(false);
                        out.flush();
                        updateClientWelt();
                    } catch (ArmeeException | NotYourLandException | IdException | NachbarException excepti) {
                        out.writeObject(true);
                        out.flush();
                        out.writeObject(excepti);
                        out.flush();
                    }
                }
                buttonClicked = 0;
            }
        }
    }
}

