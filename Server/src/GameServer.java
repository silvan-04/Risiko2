package Risiko.Server.src;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import Risiko.events.GameEvent;
import Risiko.domain.*;
import Risiko.domain.exceptions.*;

public class GameServer implements Serializable {

    private static final long serialVersionUID = -6549201224167353182L;

    private static ServerSocket serverSocket;
    private static final int SOCKET_PORT = 8080;
    private static final int NO_OF_PLAYERS = 2;
    private static final int MAX_PLAYERS = 6;

    private final List<ObjectOutputStream> listeners;
    private Welt welt;
    private boolean up = false;

    private int noOfTurns;

    public boolean isServerUp(){
        return up;
    }

    public GameServer(Welt welt) throws IOException {
        serverSocket = new ServerSocket(SOCKET_PORT);
        listeners = new ArrayList<>();
        this.welt = welt;
    }

    public void start() throws IOException {
        System.out.println("Game-Server läuft auf Port " + SOCKET_PORT);

        while (listeners.size() < MAX_PLAYERS ) {
            Socket socket = serverSocket.accept();
            System.out.println("Neuer Client " + socket);
            ClientRequestProcessor c = new ClientRequestProcessor(socket,welt,this);
            listeners.add(c.getOut());
            new Thread(() -> {
                try {
                    c.handleClient();
                    up = true;
                } catch (IOException | ClassNotFoundException | DoppelterNameException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }

    /*
     * Notifications for game clients
     *
     * (non-Javadoc)
     * @see prog2.vl.gameloop.common.ServerRemote#addGameEventListener(prog2.vl.gameloop.common.GameEventListener)
     */
    public void notifyListeners(GameEvent event) throws IOException {
        for (ObjectOutputStream out : listeners) {
            out.reset(); // hotfix, otherwise the client stays in PHASE1. (Because ObjectOutputStream caches GameEvent objects..)
            out.writeObject(event);
            out.flush();
        }
    }

    public static void main(String[] args) throws IOException {
        GameServer server = new GameServer(new Welt());
        server.start();
    }
}

