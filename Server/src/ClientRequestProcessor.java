package Risiko.Server.src;

import Risiko.commands.GameCommand;
import Risiko.commands.GameCommandType;
import Risiko.entities.Spieler;

import java.io.*;
import java.net.Socket;

public class ClientRequestProcessor {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ClientRequestProcessor(Socket socket) {
        this.socket = socket;

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

    public void handleClient() throws IOException, ClassNotFoundException {
//        out.writeObject(welt);
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
    }

}