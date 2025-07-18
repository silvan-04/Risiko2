package Risiko.Client;

import java.io.*;
import java.net.Socket;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import Risiko.entities.*;
import Risiko.events.*;
import Risiko.commands.*;
import Risiko.domain.Welt;
import Risiko.ui.gui.Fenster.MenuFenster;
import Risiko.ui.gui.RisikoClientGUI;
import Risiko.ui.gui.panels.WarteFenster;

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
    public GameClient(boolean startServer,String name) throws IOException, ClassNotFoundException {
        // Setup socket
        socket = new Socket("localhost", SOCKET_PORT);
        socketOut = new ObjectOutputStream(socket.getOutputStream());
        socketIn = new ObjectInputStream(socket.getInputStream());

        System.out.println("client socket");
        // initialize UI
        initialize(startServer);
        frame.setVisible(true);
        ((WarteFenster)frame).getPlay().addActionListener(e ->  {
            sendCommand(new GameCommand(GameCommandType.GAME_START));
        });

        System.out.println("frame erstellt");
        frame.setTitle("Warte auf Spielbegin!");
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
            doppelt = (boolean)socketIn.readObject();
            System.out.println(doppelt);
            first=false;
        }while(!doppelt);
        System.out.println("nach schleife");
        socketOut.flush();
        player = (Spieler) socketIn.readObject();
        System.out.println(player.getName());

        // start listening to new game events received by socket server
        new Thread(this::listenForEvents).start();

    }

    /*
     * Initialize the contents of the frame.
     */
    private void initialize(boolean mode) throws IOException, ClassNotFoundException {
//        Welt welt = (Welt) socketIn.readObject();
//        System.out.println( welt);
        frame = new WarteFenster(mode);
        System.out.println("Hallo");
    }

    // Sends a commad to the server
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
                    handleGameEvent(event);
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
    public void handleGameEvent(GameEvent event) {
        if (event instanceof GameControlEvent gce) {
            switch (gce.getType()) {
                case PLAYER_JOINED:

                    ((WarteFenster)frame).updateAnzahl(gce.getPlayerCount());
                    break;
                case GAME_STARTED:
                    frame.dispose();
                    this.risikoFrame = new RisikoClientGUI(gce.getWelt(),this);
                    JOptionPane.showMessageDialog(frame,
                            "Das Spiel hat begonnen " + gce.getSpieler().getName() + " beginnt..",
                            "Game Started",
                            JOptionPane.INFORMATION_MESSAGE);
                    // break statement deliberately omitted,
                    // since game event also carries information on next turn
//                    break;
                case NEXT_TURN:
                    int phase = gce.getPhase();
                    System.out.println(phase);
                    Spieler currentSpieler = gce.getSpieler();
                    aktuallisiereSpieler(gce.getWelt().getSpielerListe());
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
            if (!gae.getSpieler().equals(this.player)) {
                // Event originates from other player and is relevant for me:
                switch (gae.getType()) {
                    case ATTACK:
                        JOptionPane.showMessageDialog(frame,
                                "You are attacked by player " + gae.getSpieler().getName() + ".",
                                "Attack!",
                                JOptionPane.WARNING_MESSAGE);
                        break;
                    case NEW_OWNER:
                        JOptionPane.showMessageDialog(frame,
                                "Some territory has been conquered by player " + gae.getSpieler().getName() + ".",
                                "UI Update!",
                                JOptionPane.INFORMATION_MESSAGE);
                        break;
                    default:
                }
            } else {
                // Event originates from me and is relevant for me:
                switch (gae.getType()) {
                    case BUY_ITEM:
                        JOptionPane.showMessageDialog(frame,
                                "Do you want to buy an item, " + gae.getSpieler().getName() + "?",
                                "Special Offer!!",
                                JOptionPane.QUESTION_MESSAGE);
                        break;
                    default:
                }
            }
        }
    }

    public void aktuallisiereSpieler(List<Spieler> list){
        for(Spieler spieler : list){
            if(this.player.getName().equals(spieler.getName())){
                this.player=spieler;
                break;
            }
        }
    }
}