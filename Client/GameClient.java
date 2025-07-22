package Risiko.Client;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.*;

import Risiko.domain.exceptions.ArmeeException;
import Risiko.domain.exceptions.IdException;
import Risiko.domain.exceptions.NotYourLandException;
import Risiko.entities.*;
import Risiko.events.*;
import Risiko.commands.*;
import Risiko.ui.gui.RisikoClientGUI;
import Risiko.ui.gui.panels.WarteFenster;
import Risiko.ui.gui.panels.WeltPanel;

public class GameClient implements Serializable {

    @Serial
    private static final long serialVersionUID = -377202728391016114L;

    private static final int SOCKET_PORT = 8080;

    private Spieler player = null;

    // UI
    private JFrame frame;
    private JFrame risikoFrame;
    private JButton btnGameAction;
    private JButton btnNextTurn;
    private JLabel lblStatus;

    private final ObjectOutputStream socketOut;
    private final ObjectInputStream socketIn;
    private final Socket socket;
    private final BlockingQueue<Object> replyQueue = new LinkedBlockingQueue<>();

//    /**
//     * Launch the application.
//     */
//    public static void main(String[] args) {
//        try {
//            new GameClient();
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * Create the application.
     */
    public GameClient(boolean startServer,String name) throws IOException, ClassNotFoundException, InterruptedException {
        // Setup socket
        socket = new Socket("localhost", SOCKET_PORT);
        socketOut = new ObjectOutputStream(socket.getOutputStream());
        socketOut.flush();
        socketIn = new ObjectInputStream(socket.getInputStream());

        System.out.println("client socket");
        // initialize UI
        initialize(startServer,false);
        frame.setVisible(true);
        ((WarteFenster)frame).getPlay().addActionListener(e ->  {
            sendCommand(new GameCommand(GameCommandType.GAME_START));
        });

        System.out.println("frame erstellt");
        frame.setTitle("Warte auf Spielbeginn!");
        // create and register new player
        boolean doppelt = false;
        boolean first = true;
        do {
            Spieler spieler;
            if(!first){
                String name2 = JOptionPane.showInputDialog(frame, "Es existiert schon ein Spieler mit diesem Namen!\nGib einen neuen Namen ein!", "Doppelter Name!", JOptionPane.QUESTION_MESSAGE);
                spieler = new Spieler(name2);
            }else{
                spieler = new Spieler(name);
            }
            // Send new player object to socket server
            socketOut.writeObject(spieler);
            socketOut.flush();
            doppelt = (boolean)socketIn.readObject();
//            doppelt = (boolean)replyQueue.take();

            System.out.println(doppelt);
            first=false;
        }while(!doppelt);
        System.out.println("nach schleife");
        socketOut.flush();
        player = (Spieler) socketIn.readObject();
//        player = (Spieler)replyQueue.take();
        System.out.println(player.getName());

        // start listening to new game events received by socket server
        new Thread(this::listenForEvents).start();

    }

    //laden konstruktor
    public GameClient(boolean startServer) throws IOException, ClassNotFoundException, InterruptedException {
        // Setup socket
        socket = new Socket("localhost", SOCKET_PORT);
        socketOut = new ObjectOutputStream(socket.getOutputStream());
        socketOut.flush();
        socketIn = new ObjectInputStream(socket.getInputStream());

        System.out.println("client socket");
        // initialize UI
        initialize(startServer,true);
        frame.setVisible(true);
        ((WarteFenster)frame).getPlay().addActionListener(e ->  {
            sendCommand(new GameCommand(GameCommandType.GAME_START));
        });

        System.out.println("frame erstellt");
        frame.setTitle("Warte auf Spielbeginn!");
        // create and register new player
        boolean doppelt = false;
        boolean first = true;
        socketOut.writeObject(new GameCommand(GameCommandType.LADESPIELER));
        socketOut.flush();
        player =(Spieler)socketIn.readObject();
        System.out.println(player.getName());

        // start listening to new game events received by socket server
        new Thread(this::listenForEvents).start();

    }

    /*
     * Initialize the contents of the frame.
     */
    private void initialize(boolean mode,boolean laden) throws IOException, ClassNotFoundException {
//        Welt welt = (Welt) socketIn.readObject();
//        System.out.println( welt);
        frame = new WarteFenster(mode,laden);
        System.out.println("Hallo");
    }

    // Sends a command to the server
    private void sendCommand(GameCommand command) {
        try {
            socketOut.writeObject(command);
            socketOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Listen for game events and handle them via handleGameEvent()
    private void listenForEvents() {
        try {
            while (true) {
                Object obj = socketIn.readObject();

                if (obj instanceof GameEvent event) {
                    SwingUtilities.invokeAndWait(() -> {
                        try {
                            handleGameEvent(event);
                        } catch (IOException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });

                }else{
                    replyQueue.put(obj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Handling of server-side game events
     *
     * (non-Javadoc)
     * @see prog2.vl.gameloop.common.GameEventListener#handleGameEvent(prog2.vl.gameloop.common.events.GameEvent)
     */
    public void handleGameEvent(GameEvent event) throws IOException, InterruptedException {
        if (event instanceof GameControlEvent gce) {
            switch (gce.getType()) {
                case PLAYER_JOINED:
                    ((WarteFenster)frame).updateAnzahl(gce);
                    break;
                case GAME_STARTED:
                    frame.dispose();
                    this.risikoFrame = new RisikoClientGUI(gce.getWelt(),this);
                    aktualisiereSpieler(gce.getWelt().getSpielerListe());
                    ((RisikoClientGUI)risikoFrame).getActionButton().addActionListener(e -> {
                        actionMethode();
                    });
                    JOptionPane.showMessageDialog(frame,
                            "Das Spiel hat begonnen " + gce.getSpieler().getName() + " beginnt..",
                            "Game Started",
                            JOptionPane.INFORMATION_MESSAGE);
                    // break statement deliberately omitted,
                    // since game event also carries information on next turn
//                    break;
                case NEXT_TURN:
                    aktualisiereSpieler(gce.getWelt().getSpielerListe());
                    int phase = gce.getPhase();
                    System.out.println(phase);
                    Spieler currentSpieler = gce.getWelt().aktiverSpieler();
                    System.out.println(currentSpieler.getName() + " ist gerade dran.");
                    System.out.println(player.getIstAmZug());
                    if (currentSpieler.equals(player)) {
                        ((RisikoClientGUI)risikoFrame).setButton(true);
                        // It is this player's turn!
                        // Update UI, e.g. enable UI elements such as buttons
                        System.out.println("Game Action: Spieler " + currentSpieler.getName() + " in Phase " + phase);
                    } else {
                        ((RisikoClientGUI)risikoFrame).setButton(false);
                        // It is another player's turn!
                        // Nothing to do; just deactivate UI...

                    }
                    break;
                case GAME_OVER:
                    aktualisiereSpieler(gce.getWelt().getSpielerListe());
                    btnGameAction.setEnabled(false);
                    btnNextTurn.setEnabled(false);
                    JOptionPane.showMessageDialog(frame,
                            "Game over. Winner is " + gce.getSpieler().getName() + ".",
                            "Game Over",
                            JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                    break;
                default:
            }
        } else if (event instanceof GameActionEvent gae) {
            aktualisiereSpieler(gae.getWelt().getSpielerListe());
//            if (!gae.getSpieler().getName().equals(this.player.getName())) {
            if (!gae.getSpieler().getName().equals(this.player.getName())) {
                // Event originates from other player and is relevant for me:
                switch (gae.getType()) {
                    case UPDATE:
                        ((RisikoClientGUI)risikoFrame).neueWelt(gae.getWelt());
                        break;
                    case ATTACK:
                        if(gae.getVerteidiger().getName().equals(this.player.getName())){
                            String zielLand = gae.getZielLand();
                            int verteidigung;
                            try{
                                int verteidigen = Integer.parseInt(JOptionPane.showInputDialog(risikoFrame, player.getName() + " dein Land " + gae.getWelt().idToLand(zielLand).getName() + ", mit " + gae.getWelt().idToLand(zielLand).getArmee() + " Einheiten, wird von "+ gae.getSpieler().getName() + " angegriffen! \nGib die Zahl der Einheiten zur Verteidigung ein!" + gae.getMessage(), "Angriff", JOptionPane.PLAIN_MESSAGE));
                                socketOut.writeObject(new GameCommand(GameCommandType.VERTEIDIGUNG,verteidigen,gae.getSpieler()));
                                socketOut.flush();
                                boolean fehler = (boolean)replyQueue.take();
                                if(fehler){
                                    Exception ex = (Exception) replyQueue.take();
                                    if(ex instanceof NumberFormatException){
                                        JOptionPane.showMessageDialog(risikoFrame, "Es muss eine Zahl eingegeben werden!", "FEHLER!", JOptionPane.ERROR_MESSAGE);
                                    }else{
                                        JOptionPane.showMessageDialog(risikoFrame, ex.getMessage(), "FEHLER!", JOptionPane.ERROR_MESSAGE);
                                    }

                                }
                            }catch(NumberFormatException e){
                                socketOut.writeObject(new GameCommand(GameCommandType.VERTEIDIGUNG,-1,gae.getSpieler()));
                                socketOut.flush();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }

                        }
                        break;
                    default:
                        System.out.println("default gae von anderen");
                       break;
                }
            } else {
                // Event originates from me and is relevant for me:
                switch (gae.getType()) {
                    case UPDATE:
                        ((RisikoClientGUI)risikoFrame).neueWelt(gae.getWelt());
                        aktualisiereSpieler(gae.getWelt().getSpielerListe());
                        break;
                    case VERTEIDIGUNG:
                        System.out.println("2 verteidigung");
                        System.out.println("Verteidigung: " + gae.getVerteidigen());
//                        socketOut.writeObject(new GameCommand(GameCommandType.VERTERIDIGUNGANTWORT,gae.getVerteidigen(),gae.getSpieler()));
                        socketOut.writeObject(Integer.valueOf(gae.getVerteidigen()));
                        socketOut.flush();
//                        angriffWeiter();
                        System.out.println("Verteidigung: " + gae.getVerteidigen()+ "gesendet");

                        break;
                    default:
                        System.out.println("default gae von selber");
                        break;
                }
            }
        }
    }

    private void actionMethode() {
        try {
            System.out.println("in action methode");
            sendCommand(new GameCommand(GameCommandType.GAME_ACTION));
            System.out.println("wartet auf phase");
//            int phase = (int)socketIn.readObject();
            int phase = (Integer) replyQueue.take();
            System.out.println("nach phase lesen: " + phase);
            switch (phase) {
                case 0:
                    int einheiten = 0;
//                    int action = (int) socketIn.readObject();
                    int action = (int) replyQueue.take();
                    System.out.println("nach action lesen" + action);
                    if (action == 0) {
//                            boolean handkartenLimit = (boolean)socketIn.readObject();
                        boolean handkartenLimit = (boolean) replyQueue.take();
                        System.out.println();
                        int kartenEntscheidung = 0;
                        if (!handkartenLimit) {
                            kartenEntscheidung = JOptionPane.showConfirmDialog(risikoFrame, "Möchtest du Karten einlösen?", "Armee verteilen!", JOptionPane.YES_NO_OPTION);
                            socketOut.writeObject(Integer.valueOf(kartenEntscheidung));
                            socketOut.flush();
                        }
                        if (kartenEntscheidung == 0) {
                            String[] karten = new String[player.getEinheitskarten().size()];
                            for (int i = 0; i < player.getEinheitskarten().size(); i++) {
                                karten[i] = player.getEinheitskarten().get(i).toString();
                            }
                            int kartenAuswahl1 = JOptionPane.showOptionDialog(risikoFrame, "Wähle die erste Karte", "Karte 1", 0, 3, null, karten, karten[0]);
                            Einheitskarte karte1 = switch (kartenAuswahl1) {
                                case 0 -> player.getEinheitskarten().get(0);
                                case 1 -> player.getEinheitskarten().get(1);
                                case 2 -> player.getEinheitskarten().get(2);
                                case 3 -> player.getEinheitskarten().get(3);
                                case 4 -> player.getEinheitskarten().get(4);
                                default -> null;
                            };
                            socketOut.writeObject(karte1);
                            socketOut.flush();
                            int kartenAuswahl2 = JOptionPane.showOptionDialog(risikoFrame, "Wähle die zweite Karte", "Karte 2", 0, 3, null, karten, karten[0]);
                            Einheitskarte karte2 = switch (kartenAuswahl2) {
                                case 0 -> player.getEinheitskarten().get(0);
                                case 1 -> player.getEinheitskarten().get(1);
                                case 2 -> player.getEinheitskarten().get(2);
                                case 3 -> player.getEinheitskarten().get(3);
                                case 4 -> player.getEinheitskarten().get(4);
                                default -> null;
                            };
                            socketOut.writeObject(karte2);
                            socketOut.flush();
                            int kartenAuswahl3 = JOptionPane.showOptionDialog(risikoFrame, "Wähle die dritte Karte", "Karte 3", 0, 3, null, karten, karten[0]);
                            Einheitskarte karte3 = switch (kartenAuswahl3) {
                                case 0 -> player.getEinheitskarten().get(0);
                                case 1 -> player.getEinheitskarten().get(1);
                                case 2 -> player.getEinheitskarten().get(2);
                                case 3 -> player.getEinheitskarten().get(3);
                                case 4 -> player.getEinheitskarten().get(4);
                                default -> null;
                            };
                            socketOut.writeObject(karte3);
                            socketOut.flush();
//                                boolean richtig = (boolean)socketIn.readObject();
                            boolean richtig = (boolean) replyQueue.take();
                            if (richtig) {
//                                    einheiten = (int)socketIn.readObject();
                                einheiten = (int) replyQueue.take();
                                JOptionPane.showMessageDialog(frame, "Du kannst " + (player.getEinheitenRunde() + einheiten) + " Einheiten verteilen! \nKlicke das Land an, welches verstärkt werden soll!", " Armee verteilen!", JOptionPane.INFORMATION_MESSAGE);
                            } else {
//                                    Exception ex = (Exception) socketIn.readObject();
                                Exception ex = (Exception) replyQueue.take();
                                JOptionPane.showMessageDialog(risikoFrame, ex.getMessage(), "FEHLER!", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame, "Du kannst " + (player.getEinheitenRunde() + einheiten) + " Einheiten verteilen! \nKlicke das Land an, welches verstärkt werden soll!", " Armee verteilen!", JOptionPane.INFORMATION_MESSAGE);
                        }

                    } else if (action == 1) {
                        JOptionPane.showMessageDialog(risikoFrame, "Du kannst " + (player.getEinheitenRunde() + einheiten) + " Einheiten verteilen! \nKlicke das Land an, welches verstärkt werden soll!", " Armee verteilen!", JOptionPane.INFORMATION_MESSAGE);
                    } else if (action == 2) {

                        if (((WeltPanel) ((RisikoClientGUI) risikoFrame).getWeltPanel()).getCountryClicked()) {
                            System.out.println("vor true client");
                            socketOut.writeObject(true);
                            System.out.println("nach true client");

                            socketOut.flush();
                            String id = ((WeltPanel) ((RisikoClientGUI) risikoFrame).getWeltPanel()).getLastClickedCountry();
                            socketOut.writeObject(id);
                            socketOut.flush();
//                            boolean stop = (boolean) socketIn.readObject();
                            boolean stop = (boolean) replyQueue.take();
                            if (!stop) {
                                try {
                                    int verschobenEinheiten = Integer.parseInt(JOptionPane.showInputDialog(risikoFrame, "Wie viele Einheiten möchtest du einsetzen? \nMax: " + (player.getEinheitenRunde() + einheiten) + " Einheiten.", "Armee verteilen!", JOptionPane.QUESTION_MESSAGE));
                                    socketOut.writeObject(Integer.valueOf(einheiten));
                                    socketOut.flush();
                                    socketOut.writeObject(Integer.valueOf(verschobenEinheiten));
                                    socketOut.flush();
//                                    boolean eingabeGültig= (boolean)socketIn.readObject();
                                    boolean eingabeGültig = (boolean) replyQueue.take();
                                    if (eingabeGültig) {
                                        ((WeltPanel) ((RisikoClientGUI) risikoFrame).getWeltPanel()).setLastClickedCountry(null);
                                        ((WeltPanel) ((RisikoClientGUI) risikoFrame).getWeltPanel()).setCountryClicked(false);
//                                        boolean naechstePhase = (boolean)socketIn.readObject();
                                        boolean naechstePhase = (boolean) replyQueue.take();
                                        if (naechstePhase) {
                                            risikoFrame.revalidate();
                                            risikoFrame.repaint();
                                            ((RisikoClientGUI) risikoFrame).getActionButton().setText("Angreifen");
                                        }
                                    } else {
//                                        Exception exc = (Exception)socketIn.readObject();
                                        Exception exc = (Exception) replyQueue.take();
                                        JOptionPane.showMessageDialog(risikoFrame, exc.getMessage(), "FEHLER!", JOptionPane.ERROR_MESSAGE);
                                    }
                                } catch (NumberFormatException exce) {
                                    JOptionPane.showMessageDialog(frame, "Es muss eine Zahl eingegeben werden!", "FEHLER!", JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
//                                Exception ex =(Exception)socketIn.readObject();
                                Exception ex = (Exception) replyQueue.take();
                                JOptionPane.showMessageDialog(risikoFrame, ex.getMessage(), "FEHLER!", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            socketOut.writeObject(false);
                            socketOut.flush();
                            JOptionPane.showMessageDialog(risikoFrame, "Klicke erst ein Land an!", "FEHLER!", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    break;
                case 1 :
                    System.out.println("phase 1 case");
                    int action1 = (int) replyQueue.take();
                    System.out.println(action1 +"action");
                    switch (action1) {
                        case 0:
                            String [] options = {"Angreifen!","Nächste Phase!"};
                            int auswahl = JOptionPane.showOptionDialog(risikoFrame, "Was möchtest du tun ?", "Angriff!", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                            System.out.println(auswahl+"auswahl");
                            socketOut.writeObject(Integer.valueOf(auswahl));
                            socketOut.flush();
                            if (auswahl == 1) {
                                System.out.println("in auswahl 1 ");
                                ((RisikoClientGUI) risikoFrame).getActionButton().setText("Armee Verschieben");
                            } else {
                                JOptionPane.showMessageDialog(risikoFrame, "Klicke das Land an, mit dem du angreifen möchtest", "Angriff!", JOptionPane.INFORMATION_MESSAGE);
                            }
                            break;
                        case 1:
                            System.out.println("action 1 case");
                            boolean countryClicked = ((WeltPanel) ((RisikoClientGUI) risikoFrame).getWeltPanel()).getCountryClicked();
                            socketOut.writeObject(countryClicked);
                            socketOut.flush();
                            System.out.println("country clicked gesendt");
                            if (countryClicked) {
                                String angriffsland = ((WeltPanel) ((RisikoClientGUI) risikoFrame).getWeltPanel()).getLastClickedCountry();
                                ((WeltPanel) ((RisikoClientGUI) risikoFrame).getWeltPanel()).setLastClickedCountry(null);
                                ((WeltPanel) ((RisikoClientGUI) risikoFrame).getWeltPanel()).setCountryClicked(false);
                                socketOut.writeObject(angriffsland);
                                socketOut.flush();
                                System.out.println("angriffsland gesendet");

                                boolean stop = (boolean) replyQueue.take();
                                if (stop) {
                                    Exception ex = (Exception) replyQueue.take();
                                    JOptionPane.showMessageDialog(risikoFrame, ex.getMessage(), "FEHLER!", JOptionPane.ERROR_MESSAGE);
                                } else {
                                    JOptionPane.showMessageDialog(frame, "Klicke das Land an, welches du angreifen möchtest", "Angriff!", JOptionPane.INFORMATION_MESSAGE);
                                }
                            } else {
                                JOptionPane.showMessageDialog(risikoFrame, "Klicke erst ein Land an!", "FEHLER!", JOptionPane.ERROR_MESSAGE);
                            }
                            break;
                        case 2:
                            boolean countryClicked1 = ((WeltPanel) ((RisikoClientGUI) risikoFrame).getWeltPanel()).getCountryClicked();
                            socketOut.writeObject(countryClicked1);
                            socketOut.flush();
                            if (countryClicked1) {
                                String zielland = ((WeltPanel) ((RisikoClientGUI) risikoFrame).getWeltPanel()).getLastClickedCountry();
                                ((WeltPanel) ((RisikoClientGUI) risikoFrame).getWeltPanel()).setLastClickedCountry(null);
                                ((WeltPanel) ((RisikoClientGUI) risikoFrame).getWeltPanel()).setCountryClicked(false);
                                socketOut.writeObject(zielland);
                                socketOut.flush();

                                boolean stop2 = (boolean) replyQueue.take();
                                if (stop2) {
                                    Exception ex = (Exception) replyQueue.take();
                                    JOptionPane.showMessageDialog(risikoFrame, ex.getMessage(), "FEHLER!", JOptionPane.ERROR_MESSAGE);
                                } else {
                                    String möglicheEinheiten = (String) replyQueue.take();
                                    einheiten = 0;
                                    try {
                                        einheiten = Integer.parseInt(JOptionPane.showInputDialog(frame, "Gib die Anzahl der Einheiten ein, welche angreifen sollen! \nMögliche Einheiten: " + möglicheEinheiten, "Angriff", JOptionPane.PLAIN_MESSAGE));
                                        socketOut.writeObject(false);
                                        socketOut.flush();
                                        socketOut.writeObject(einheiten);
                                        socketOut.flush();

                                    } catch (NumberFormatException e) {
                                        socketOut.writeObject(true);
                                        socketOut.flush();

                                        JOptionPane.showMessageDialog(risikoFrame, "Es muss eine Zahl eingegeben werden!", "FEHLER!", JOptionPane.ERROR_MESSAGE);
                                    }
                                    boolean stop3 = (boolean) replyQueue.take();
                                    if (stop3) {
                                        Exception excep = (Exception) replyQueue.take();
                                        JOptionPane.showMessageDialog(risikoFrame, excep.getMessage(), "FEHLER!", JOptionPane.ERROR_MESSAGE);
                                    }else{
                                        System.out.println("vor stop4");
                                        boolean stop4 = (boolean) replyQueue.take();
                                        System.out.println("nach stop4");
                                        if (stop4) {
                                            Exception ex = (Exception) replyQueue.take();
                                            JOptionPane.showMessageDialog(risikoFrame, ex.getMessage(), "FEHLER!", JOptionPane.ERROR_MESSAGE);
                                        } else {
                                            java.util.List<Integer> angriffZahlen = (List<Integer>) replyQueue.take();
                                            java.util.List<Integer> verteidigungsZahlen = (List<Integer>) replyQueue.take();
                                            String verteidiger = (String) replyQueue.take();
                                            JOptionPane.showMessageDialog(risikoFrame, player.getName() + " würfelt:" + angriffZahlen + "\n" + verteidiger + " würfelt:" + verteidigungsZahlen, "Würfel", JOptionPane.WARNING_MESSAGE);
                                            boolean erobert=(boolean) replyQueue.take();
                                            if(erobert){
                                                boolean verschiebenFehler = false;
                                                do {
                                                    boolean verschiebenMöglich = (boolean)replyQueue.take();
                                                    if(verschiebenMöglich){
                                                        boolean gewinner = (boolean) replyQueue.take();
                                                        if (gewinner) {
                                                            String winnerText = (String) replyQueue.take();
                                                            JOptionPane.showMessageDialog(risikoFrame, winnerText, "DU HAST GEWONNEN!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("win.png"));
                                                            risikoFrame.dispose();
                                                        }
                                                        int möglicheTruppen = (int) replyQueue.take();
                                                        int verschieben = 0;
                                                        boolean fehler = false;
                                                        do {
                                                            try {
                                                                verschieben = Integer.parseInt(JOptionPane.showInputDialog(risikoFrame, "Sie haben das Land eingenommen. Wie viele Einheiten sollen zusätzlich in das eroberte Land?" + "\nGib eine Zahl zwischen 0 und " + möglicheTruppen + " ein:", "Gewonnen!", JOptionPane.PLAIN_MESSAGE));
                                                                fehler = false;
                                                            } catch (NumberFormatException e) {
                                                                fehler = true;
                                                                JOptionPane.showMessageDialog(risikoFrame, "Es muss eine Zahl eingegeben werden!", "FEHLER!", JOptionPane.ERROR_MESSAGE);
                                                            }
                                                        } while (fehler);
                                                        socketOut.writeObject(Integer.valueOf(verschieben));
                                                        socketOut.flush();
                                                        verschiebenFehler = (boolean) replyQueue.take();
                                                        if(verschiebenFehler){
                                                            Exception ex = (Exception) replyQueue.take();
                                                            JOptionPane.showMessageDialog(risikoFrame, ex.getMessage(), "FEHLER!", JOptionPane.ERROR_MESSAGE);
                                                        }
                                                    }
                                                }while(verschiebenFehler);
                                            }
                                        }
                                    }
                                }
                                break;
                            } else {
                                JOptionPane.showMessageDialog(risikoFrame, "Klicke erst ein Land an!", "FEHLER!", JOptionPane.ERROR_MESSAGE);
                                break;
                            }
                    }
                    break;
                case 2:
                    action = (int) replyQueue.take();
                    switch (action) {
                        case 0:
                            String[] options = {"Verschieben!", "Nächster Spieler!"};
                             int auswahl = JOptionPane.showOptionDialog(risikoFrame, "Was möchtest du tun ?", "Verschieben!", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                            socketOut.writeObject(auswahl);
                            socketOut.flush();

                            if (auswahl == 1) {
                                ((RisikoClientGUI) risikoFrame).getActionButton().setText("Armee Verteilen!");
                            } else {
                                JOptionPane.showMessageDialog(risikoFrame, "Du kannst nun Truppen verschieben! \nKlicke das Land an, aus dem die Truppen kommen.", "Armee verschieben!", JOptionPane.INFORMATION_MESSAGE);
                            }
                            break;
                        case 1:
                            boolean countryClicked = ((WeltPanel) ((RisikoClientGUI) risikoFrame).getWeltPanel()).getCountryClicked();
                            socketOut.writeObject(countryClicked);
                            socketOut.flush();

                            if (!countryClicked) {
                                JOptionPane.showMessageDialog(risikoFrame, "Klicke erst ein Land an!", "FEHLER!", JOptionPane.ERROR_MESSAGE);
                            } else {
                                String angriffsLand = ((WeltPanel) ((RisikoClientGUI) risikoFrame).getWeltPanel()).getLastClickedCountry();
                                ((WeltPanel) ((RisikoClientGUI) risikoFrame).getWeltPanel()).setLastClickedCountry(null);
                                ((WeltPanel) ((RisikoClientGUI) risikoFrame).getWeltPanel()).setCountryClicked(false);
                                socketOut.writeObject(angriffsLand);
                                socketOut.flush();

                                boolean stop = (boolean) replyQueue.take();
                                if (stop) {
                                    Exception ex = (Exception) replyQueue.take();
                                    JOptionPane.showMessageDialog(risikoFrame, ex.getMessage(), "FEHLER!", JOptionPane.ERROR_MESSAGE);
                                } else {
                                    JOptionPane.showMessageDialog(risikoFrame, "Klicke das Land an, in welches die Truppen verschoben werden sollen.", "Armee verschieben!", JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
                            break;
                        case 2:
                            boolean countryClicked3 = ((WeltPanel) ((RisikoClientGUI) risikoFrame).getWeltPanel()).getCountryClicked();
                            socketOut.writeObject(countryClicked3);
                            socketOut.flush();
                            if (!countryClicked3) {
                                JOptionPane.showMessageDialog(risikoFrame, "Klicke erst ein Land an!", "FEHLER!", JOptionPane.ERROR_MESSAGE);
                            } else {
                                String zielLand = ((WeltPanel) ((RisikoClientGUI) risikoFrame).getWeltPanel()).getLastClickedCountry();
                                ((WeltPanel) ((RisikoClientGUI) risikoFrame).getWeltPanel()).setLastClickedCountry(null);
                                ((WeltPanel) ((RisikoClientGUI) risikoFrame).getWeltPanel()).setCountryClicked(false);
                                socketOut.writeObject(zielLand);
                                socketOut.flush();

                                int truppen = (int) replyQueue.take();
                                String name = (String) replyQueue.take();
                                try {
                                    int verschieben = Integer.parseInt(JOptionPane.showInputDialog(risikoFrame, "Gib an wie viele Truppen verschieben möchtest. Du kannst bis zu " + truppen + " Truppen aus " + name + " verschieben!", "Verschieben!", JOptionPane.PLAIN_MESSAGE));
                                    socketOut.writeObject(verschieben);
                                    socketOut.flush();
                                } catch (NumberFormatException except) {
                                    socketOut.writeObject(Integer.valueOf(-1));
                                    socketOut.flush();
                                    JOptionPane.showMessageDialog(risikoFrame, "Es muss eine Zahl eingegeben werden!", "FEHLER!", JOptionPane.ERROR_MESSAGE);
                                }

                                boolean stop = (boolean) replyQueue.take();
                                if (stop) {
                                    Exception ex = (Exception) replyQueue.take();
                                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "FEHLER!", JOptionPane.ERROR_MESSAGE);
                                } else {
                                    JOptionPane.showMessageDialog(risikoFrame, "Die Truppen wurden erfolgreich verschoben!", "Armee verschieben!", JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
                            break;
                    }
                    break;
            }

        } catch(IOException | InterruptedException ex){
            throw new RuntimeException(ex);
        }
    }
//    public void angriffWeiter() throws InterruptedException, IOException {
//        System.out.println("vor stop4");
//        boolean stop4 = (boolean) replyQueue.take();
//        System.out.println("nach stop4");
//        if (stop4) {
//            Exception ex = (Exception) replyQueue.take();
//            JOptionPane.showMessageDialog(risikoFrame, ex.getMessage(), "FEHLER!", JOptionPane.ERROR_MESSAGE);
//        } else {
//            java.util.List<Integer> angriffZahlen = (List<Integer>) replyQueue.take();
//            java.util.List<Integer> verteidigungsZahlen = (List<Integer>) replyQueue.take();
//            System.out.println(angriffZahlen + "\n"+verteidigungsZahlen);
//            String verteidiger = (String) replyQueue.take();
//            JOptionPane.showMessageDialog(risikoFrame, player.getName() + " würfelt:" + angriffZahlen + "\n" + verteidiger + " würfelt:" + verteidigungsZahlen, "Würfel", JOptionPane.WARNING_MESSAGE);
//            boolean erobert=(boolean) replyQueue.take();
//            if(erobert){
//                boolean verschiebenFehler = false;
//                do {
//                    boolean verschiebenMöglich = (boolean)replyQueue.take();
//                    if(verschiebenMöglich){
//                        boolean gewinner = (boolean) replyQueue.take();
//                        if (gewinner) {
//                            String winnerText = (String) replyQueue.take();
//                            JOptionPane.showMessageDialog(risikoFrame, winnerText, "DU HAST GEWONNEN!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("win.png"));
//                            risikoFrame.dispose();
//                        }
//                        int möglicheTruppen = (int) replyQueue.take();
//                        int verschieben = 0;
//                        boolean fehler = false;
//                        do {
//                            try {
//                                verschieben = Integer.parseInt(JOptionPane.showInputDialog(risikoFrame, "Sie haben das Land eingenommen. Wie viele Einheiten sollen zusätzlich in das eroberte Land?" + "\nGib eine Zahl zwischen 0 und " + möglicheTruppen + " ein:", "Gewonnen!", JOptionPane.PLAIN_MESSAGE));
//                                fehler = false;
//                            } catch (NumberFormatException e) {
//                                fehler = true;
//                                JOptionPane.showMessageDialog(risikoFrame, "Es muss eine Zahl eingegeben werden!", "FEHLER!", JOptionPane.ERROR_MESSAGE);
//                            }
//                        } while (fehler);
//                        socketOut.writeObject(Integer.valueOf(verschieben));
//                        socketOut.flush();
//                        verschiebenFehler = (boolean) replyQueue.take();
//                        if(verschiebenFehler){
//                            Exception ex = (Exception) replyQueue.take();
//                            JOptionPane.showMessageDialog(risikoFrame, ex.getMessage(), "FEHLER!", JOptionPane.ERROR_MESSAGE);
//                        }
//                    }
//                }while(verschiebenFehler);
//            }
//        }
//    }
    public void aktualisiereSpieler(List<Spieler> list){
        for(Spieler spieler : list){
            if(this.player.getName().equals(spieler.getName())){
                this.player=spieler;
                break;
            }
        }
        ((RisikoClientGUI)risikoFrame).setSpieler(player);
    }
    public Spieler getSpieler(){
        return player;
    }
}

