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
import java.sql.SQLOutput;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


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
    private static int idZähler=0;
    private int id;
    private final BlockingQueue<Object> replyQueue = new LinkedBlockingQueue<>();
    private int einheitenAngriff = 0;


    public ClientRequestProcessor(Socket socket,Welt welt,GameServer gameServer) {
        id=idZähler++;
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

    public void handleClient() throws IOException, ClassNotFoundException, DoppelterNameException, InterruptedException {
        while (true) {
            Object obj = in.readObject();
            if (obj instanceof GameCommand cmd) {
                switch (cmd.getType()) {
                    case GameCommandType.GAME_START ->
                        new Thread(() -> {
                            try{
                                startGame();
                            }catch (IOException ioe){
                                ioe.printStackTrace();
                            }
                        }, "StartGame-Thread").start();
                    case GameCommandType.NEXT_TURN ->
                        new Thread(()->{
                            try{
                                nextTurn();
                            }catch(IOException ioe){
                                ioe.printStackTrace();
                            }
                        }, "nextTurn-Thread").start();
                    case GameCommandType.GAME_ACTION ->
                        new Thread(()->{
                            try{
                                someGameAction();
                            }catch (IOException | InterruptedException ioException){
                                ioException.printStackTrace();
                            }
                        },"GameAction-Thread").start();
                    case GameCommandType.VERTEIDIGUNG ->
                        new Thread(()->{
                            try{
                                verteidung(cmd);
                            }catch(IOException ioe){
                                ioe.printStackTrace();
                            }
                        },"Verteidigung-Thread").start();
                    case GameCommandType.LADESPIELER ->
                        new Thread (()->{
                            try {
                                ladeSpieler();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        },"LadeSpieler-Thread").start();
                    case GameCommandType.VERTERIDIGUNGANTWORT ->
                        replyQueue.put(cmd.getPayload());
                }
            } else if (obj instanceof Spieler player) {
                addPlayer(player.getName());
            } else{
                replyQueue.put(obj);
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

    public void ladeSpieler() throws IOException {
        Spieler spieler = welt.getSpielerListe().get(this.id);
        out.writeObject(spieler);
        gameServer.notifyListeners(new GameControlEvent(welt,GameControlEvent.GameControlEventType.PLAYER_JOINED,id+1));
        if(welt.getSpielerListe().size() == this.id+1) {
            gameServer.notifyListeners(new GameControlEvent(welt, GameControlEvent.GameControlEventType.GAME_STARTED));
        }
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
    public void someGameAction() throws IOException,InterruptedException {

        if(welt.getPhase()==0){
            erstePhase();
        } else if (welt.getPhase()==1) {
            zweitePhase();
        }else if (welt.getPhase()==2) {
            drittePhase();
        }
    }

    public void verteidung(GameCommand gC) throws IOException {
//        for(Spieler spieler : welt.getSpielerListe()){
//            if(gC.getSpieler().getName().equals(spieler.getName())){
//                spieler1 = spieler;
//                break;
//            }
//        }
        gameServer.notifyListeners(new GameActionEvent(welt.aktiverSpieler(),welt,gC.getPayload()));
    }

    private void updateClientWelt() throws IOException {
        gameServer.notifyListeners(new GameActionEvent(welt.aktiverSpieler(),GameActionEvent.GameActionEventType.UPDATE,welt));
    }

    public void erstePhase() throws IOException, InterruptedException {
        out.writeObject(Integer.valueOf(0));    // übergibt die 1. Phase
        out.flush();
        if(buttonClicked == 0 && welt.aktiverSpieler().getEinheitskarten().size()>2) {  // Spieler wird nur nach Karten Einlösen gefragt, wenn er mind. 3 Karten hat
            out.writeObject(Integer.valueOf(0));    // übergibt ersten Fall von buttonClicked == 0 && welt.aktiverSpieler().getEinheitskarten().size()>2
            out.flush();
            int kartenEntscheidung = 0;
            if(!welt.handkartenlimit(welt.aktiverSpieler())){
                out.writeObject(false);
                out.flush();
                kartenEntscheidung = (int)replyQueue.take();
            }else{
                out.writeObject(true);
                out.flush();
            }
            Einheitskarte karte1;
            Einheitskarte karte2;
            Einheitskarte karte3;
            if (kartenEntscheidung == 0 ){  // wenn der Spieler Karten einlösen möchte
                karte1 = (Einheitskarte)replyQueue.take();
                karte2 = (Einheitskarte)replyQueue.take();
                karte3 = (Einheitskarte)replyQueue.take();

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
            out.writeObject(Integer.valueOf(1));    // übergibt zweiten Fall von buttonClicked == 0
            out.flush();
            buttonClicked++;
        } else if (buttonClicked == 1) {
            out.writeObject(Integer.valueOf(2));    // übergibt dritten Fall von buttonClicked == 1
            out.flush();
            boolean countryClicked = (boolean)replyQueue.take();

            if (countryClicked) {
                String id =  (String) replyQueue.take();    // empfängt geklicktes Land
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
                    int einheiten = (int)replyQueue.take();     // empfängt maximale Zahl an Einheiten
                    int verschobenEinheiten =(int) replyQueue.take();   // empfängt wie viele Einheiten der Spieler einsetzen möchte
                    try {
                        welt.aktiverSpieler().setEinheitenRunde(welt.truppenPlatzieren(welt.aktiverSpieler().getEinheitenRunde() + einheiten, verschobenEinheiten, id));
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

    public void zweitePhase() throws IOException, InterruptedException {
        out.writeObject(Integer.valueOf(1));    // übergibt die 2. Phase
        out.flush();
        if (buttonClicked == 0) {
            out.writeObject(Integer.valueOf(0));    // übergibt den Fall von buttonClicked == 0
            out.flush();
            int auswahl = (int) replyQueue.take();      // empfängt, ob Spieler angreifen möchte oder nicht
            if (auswahl == 1) {
                welt.naechstePhase();
                updateClientWelt();
            } else {
                buttonClicked++;
            }
        } else if (buttonClicked == 1) {
            out.writeObject(Integer.valueOf(1));    // übergibt den Fall von buttonClicked == 1
            out.flush();
            boolean countryClicked = (boolean) replyQueue.take();
            if (countryClicked) {
                angriffsLand = (String) replyQueue.take();  // empfängt das Land mit dem der Spieler angreifen möchte
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
            out.writeObject(Integer.valueOf(2));    // übergibt den Fall von buttonClicked == 2
            out.flush();
            boolean countryClicked = (boolean) replyQueue.take();
            if (countryClicked) {
                zielLand = (String) replyQueue.take();  // empfängt das Land, das der Spieler angreifen möchte
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
                    if (welt.idToLand(angriffsLand).getArmee() > 3) {
                        out.writeObject("Max:3 Einheiten.");
                        out.flush();
                    } else if (welt.idToLand(angriffsLand).getArmee() > 2) {
                        out.writeObject("Max:2 Einheiten.");
                        out.flush();
                    } else if (welt.idToLand(angriffsLand).getArmee() > 1) {
                        out.writeObject("Max:1 Einheiten.");
                        out.flush();
                    }
                    stop3 = (boolean) replyQueue.take();
                    if (!stop3) {
                        einheitenAngriff = (int) replyQueue.take();     // empfängt mit wie vielen Truppen der Spieler angreifen möchte
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
                        int verteidigen = 0;
                        String möglicheEinheiten = null;
                        if (welt.idToLand(zielLand).getArmee() > 1) {
                            möglicheEinheiten = "Max:2 Einheiten.";
                        } else {
                            möglicheEinheiten = "Max:1 Einheit.";
                        }
//                        gameServer.notifyListeners(new GameActionEvent(welt.aktiverSpieler(), welt.idToLand(zielLand).getBesitzer(), möglicheEinheiten, zielLand, welt));   // Abfrage der Anzahl an verteidigenden Truppen

                        try {
//                             verteidigen = (Integer) replyQueue.take();    // empfängt die Anzahl an verteidigenden Truppen
                            if(welt.idToLand(zielLand).getArmee() > 1){
                                verteidigen=2;
                            }else{
                                verteidigen=1;
                            }
                            welt.verteidigen(verteidigen, welt.idToLand(zielLand));
                            out.writeObject(false);
                            out.flush();
                        } catch (NumberFormatException | ArmeeException except) {
                            stop4 = true;
                            out.writeObject(stop4);
                            out.flush();
                            out.writeObject(except);
                            out.flush();
                        }

//                        catch (ClassNotFoundException e) {
//                            throw new RuntimeException(e);
//                        }


                        if (!stop4) {
                            java.util.List<Integer> angriffZahlen = welt.würfel(einheitenAngriff);
                            out.writeObject(angriffZahlen);     // übergibt die Würfelergebnisse des Angreifers
                            out.flush();
                            java.util.List<Integer> verteidigungsZahlen = welt.würfel(verteidigen);
                            out.writeObject(verteidigungsZahlen);   // übergibt die Würfelergebnisse des Verteidigers
                            out.flush();
                            out.writeObject(welt.idToLand(zielLand).getBesitzer().getName());   // übergibt den Namen des Verteidigers
                            out.flush();
                            List<Integer> endTruppen = welt.Kampf(welt.idToLand(angriffsLand), einheitenAngriff, welt.idToLand(zielLand), verteidigen, angriffZahlen, verteidigungsZahlen);
                            einheitenAngriff = 0;
                            //Verschieben nach eroberung
                            out.writeObject(welt.idToLand(zielLand).getErobert());  // übergibt boolean, ob das Land erobert wurde
                            out.flush();
                            if (welt.idToLand(zielLand).getErobert()) {
                                welt.idToLand(zielLand).setErobert(false);

                                boolean fehler = true;
                                do {
                                    try {
                                        out.writeObject((welt.idToLand(angriffsLand).getArmee() - 1) != 0); // übergibt boolean, ob genug Einheiten zum Verschieben da sind
                                        out.flush();
                                            welt.loserCheck();
                                            out.writeObject(!welt.winnerCheck().equals(""));    // übergibt boolean, ob ein Spieler gewonnen hat
                                            out.flush();

                                            if (!welt.winnerCheck().equals("")) {
                                                out.writeObject(welt.winnerCheck());    // übergibt Siegesnachricht
                                                out.flush();
                                                gameServer.notifyListeners(new GameControlEvent(welt, GameControlEvent.GameControlEventType.GAME_OVER));
                                                break;
                                            }
                                        if ((welt.idToLand(angriffsLand).getArmee() - 1) != 0) {
                                            out.writeObject(Integer.valueOf((welt.idToLand(angriffsLand).getArmee() - 1)));
                                            out.flush();
                                            int verschieben = (int) replyQueue.take();
                                            welt.verschieben(welt.idToLand(angriffsLand), welt.idToLand(zielLand), verschieben, true);
                                            out.writeObject(false);
                                            out.flush();
                                        }
                                        fehler = false;
                                        buttonClicked = 0;
                                        angriffsLand = null;
                                        zielLand = null;
                                        updateClientWelt();
                                    } catch (ArmeeException | NachbarException | NotYourLandException exceptio) {
                                        out.writeObject(true);
                                        out.flush();
                                        out.writeObject(exceptio);
                                        out.flush();
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                } while (fehler);
                            } else {
                                buttonClicked = 0;
                                angriffsLand = null;
                                zielLand = null;
                                updateClientWelt();
                            }

                        }

                    }
                }
            }

        }

    }

    public void drittePhase() throws IOException, InterruptedException {
        out.writeObject(Integer.valueOf(2));
        out.flush();
        if(buttonClicked == 0){
            out.writeObject(Integer.valueOf(0));
            out.flush();
            int auswahl = (Integer) replyQueue.take();
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
            boolean countryClicked = (boolean) replyQueue.take();
            if (countryClicked) {
                angriffsLand = (String) replyQueue.take();  // empfängt das Land, aus dem Truppen verschoben werden sollen
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
            boolean countryClicked = (boolean) replyQueue.take();
            if (countryClicked) {
                zielLand = (String) replyQueue.take();      // empfängt das Land, in das Truppen verschoben werden sollen
                out.writeObject(Integer.valueOf((welt.idToLand(angriffsLand).getArmee() - welt.idToLand(angriffsLand).getBewegteTruppen()-1)));
                out.flush();
                out.writeObject(welt.idToLand(angriffsLand).getName());
                out.flush();
                int verschieben = (Integer) replyQueue.take();  // empfängt die Anzahl Truppen, die verschoben werden sollen
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

