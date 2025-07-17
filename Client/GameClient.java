package Risiko.Client;

import java.io.*;
import java.net.Socket;

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

    private final Spieler player;

    // UI
    private JFrame frame;
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
    public GameClient(boolean startServer) throws IOException, ClassNotFoundException {
        // Setup socket
        socket = new Socket("localhost", SOCKET_PORT);
        socketOut = new ObjectOutputStream(socket.getOutputStream());
        socketIn = new ObjectInputStream(socket.getInputStream());

        System.out.println("client socket");
        // initialize UI
        initialize(startServer);
        frame.setVisible(true);

        System.out.println("frame erstellt");
        // create and register new player
        String name = JOptionPane.showInputDialog(frame, "Enter your name:", "Add player", JOptionPane.QUESTION_MESSAGE);
        frame.setTitle(name);
        Spieler spieler = new Spieler(name);

        // Send new player object to socket server
        socketOut.writeObject(spieler);
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
                case GAME_STARTED:
                    JOptionPane.showMessageDialog(frame,
                            "The game has just begun... It's player " + gce.getSpieler().getName() + "'s turn.",
                            "Game Started",
                            JOptionPane.INFORMATION_MESSAGE);
                    // break statement deliberately omitted,
                    // since game event also carries information on next turn
                    //	  break;
                case NEXT_TURN:
                    Welt welt = gce.getWelt();
                    System.out.println(welt.getPhase());
                    Spieler currentSpieler = gce.getSpieler();
                    if (currentSpieler.equals(player)) {
                        // It is this player's turn!
                        // Update UI, e.g. enable UI elements such as buttons
                        System.out.println("Game Action: Spieler " + currentSpieler.getName() + " in Phase " + welt.getPhase());
                        lblStatus.setText("Game Action: Spieler " + currentSpieler.getName() + " in Phase " + welt.getPhase());
                        btnGameAction.setEnabled(true);
                        btnNextTurn.setEnabled(true);
                    } else {
                        // It is another player's turn!
                        // Nothing to do; just deactivate UI...
                        lblStatus.setText("Waiting for my turn...");
                        btnGameAction.setEnabled(false);
                        btnNextTurn.setEnabled(false);
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

}