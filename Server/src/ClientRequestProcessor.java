package Risiko.Server.src;

import Risiko.commands.GameCommand;
import Risiko.commands.GameCommandType;
import Risiko.domain.exceptions.DoppelterNameException;
import Risiko.entities.Spieler;
import Risiko.domain.Welt;
import Risiko.events.GameActionEvent;
import Risiko.events.GameControlEvent;
import Risiko.events.GameEvent;

import java.io.*;
import java.net.Socket;
import java.util.Random;


public class ClientRequestProcessor {
    private static final int NO_OF_PLAYERS = 2;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Welt welt;
    private GameServer gameServer;

    public ClientRequestProcessor(Socket socket,Welt welt,GameServer gameServer) {
        this.socket = socket;
        this.welt = welt;
        this.gameServer = gameServer;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
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

    public void handleClient() throws IOException, ClassNotFoundException, DoppelterNameException {
//        out.writeObject(welt);
        while (true) {
            Object obj = in.readObject();
            if (obj instanceof GameCommand cmd) {
                switch (cmd.getType()) {
                    case GameCommandType.GAME_START -> startGame();
                    case GameCommandType.NEXT_TURN -> nextTurn();
                    case GameCommandType.GAME_ACTION -> someGameAction();
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
            out.writeObject(welt.getSpielerListe().getLast());
            gameServer.notifyListeners(new GameControlEvent(welt,GameControlEvent.GameControlEventType.PLAYER_JOINED));
        }catch(DoppelterNameException e){
            out.writeObject(false);
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
    }

    /*
     * One of probably many game actions that are executed by the clients.
     *
     * (non-Javadoc)
     * @see prog2.vl.gameloop.common.ServerRemote#someGameAction()
     */
    public void someGameAction() throws IOException {

        // Simulate one (of three possible) actions
        int actionID = new Random().nextInt(3);
        GameActionEvent.GameActionEventType type = GameActionEvent.GameActionEventType.values()[actionID];
        // Hier: Aktion ausführen...
        gameServer.notifyListeners(new GameActionEvent(welt.aktiverSpieler(),welt.getSpielerListe().get(((welt.aktiverSpieler().getId())+1)% welt.getSpielerListe().size()), GameActionEvent.GameActionEventType.ATTACK,welt));

        // Game over?
    }
}