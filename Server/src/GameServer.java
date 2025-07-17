package Risiko.Server.src;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Risiko.events.GameEvent;
import Risiko.commands.*;
import Risiko.persistence.*;
import Risiko.domain.*;
import Risiko.domain.exceptions.*;
import Risiko.entities.*;
import Risiko.ui.gui.RisikoClientGUI;
import Risiko.events.*;
import javax.swing.*;

public class GameServer implements Serializable {

    private static final long serialVersionUID = -6549201224167353182L;

    private static ServerSocket serverSocket;
    private static final int SOCKET_PORT = 8080;
    private static final int NO_OF_PLAYERS = 2;

    private final List<ObjectOutputStream> listeners;
    private Welt welt;

    private int noOfTurns;

    public GameServer(Welt welt) throws IOException {
        serverSocket = new ServerSocket(SOCKET_PORT);
        listeners = new ArrayList<>();
        this.welt = welt;
    }

    public void start() throws IOException {
        System.out.println("Game-Server läuft auf Port " + SOCKET_PORT);

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Neuer Client " + socket);

            new Thread(() -> handleClient(socket)).start();
        }
    }

    /*
     * Notifications for game clients
     *
     * (non-Javadoc)
     * @see prog2.vl.gameloop.common.ServerRemote#addGameEventListener(prog2.vl.gameloop.common.GameEventListener)
     */
    private void notifyListeners(GameEvent event) throws IOException {
        for (ObjectOutputStream out : listeners) {
            out.reset(); // hotfix, otherwise the client stays in PHASE1. (Because ObjectOutputStream caches GameEvent objects..)
            out.writeObject(event);
            out.flush();
        }
    }

    /*
     * Game-setup: new players
     * (starts game, after second player is registered)
     *
     * (non-Javadoc)
     * @see prog2.vl.gameloop.common.ServerRemote#addPlayer(prog2.vl.gameloop.common.Spieler)
     */

    public Spieler addPlayer(String spieler) throws IOException, DoppelterNameException {
        welt.spielerHinzufuegen(spieler);

        if (welt.getSpielerListe().size() > NO_OF_PLAYERS-1) {
            // signal an spieler1 (server erstellt) das er starten kann
            startGame();
        }
        return welt.getSpielerListe().getLast();
    }

    private void startGame() throws IOException {
        welt.setAmZug(0, true);
        welt.laenderErstellung();
        welt.laenderVerteilung();
        welt.kartenErstellung();
        welt.kontinentAktualisieren();
        welt.aktiverSpieler().setEinheitenRunde(welt.armeeVerteilung());
        // Notify clients: info "game has started"
        notifyListeners(new GameControlEvent(welt, GameControlEvent.GameControlEventType.GAME_STARTED));
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
        notifyListeners(new GameControlEvent(welt, GameControlEvent.GameControlEventType.NEXT_TURN));
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
        notifyListeners(new GameActionEvent(welt.aktiverSpieler(), type));

        // Game over?
        noOfTurns--;
        if (noOfTurns == 0) {
            // Game over, winner is current player
            notifyListeners(new GameControlEvent(welt, GameControlEvent.GameControlEventType.GAME_OVER));
        }
    }

    private void handleClient(Socket socket) {
        try (
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            listeners.add(out);
            out.writeObject(welt);
            while (true) {
                Object obj = in.readObject();
                if (obj instanceof GameCommand cmd) {
                    switch (cmd.getType()) {
                        case GameCommandType.NEXT_TURN -> nextTurn();
                        case GameCommandType.GAME_ACTION -> someGameAction();
                    }
                } else if (obj instanceof Spieler player) {
                    Spieler spieler = addPlayer(player.getName());
                    out.writeObject(spieler);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        GameServer server = new GameServer(new Welt());
        server.start();
    }
}

