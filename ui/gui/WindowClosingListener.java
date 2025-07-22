package Risiko.ui.gui;

import Risiko.domain.Welt;
import Risiko.domain.exceptions.KeinSpeicherstandException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class WindowClosingListener extends WindowAdapter {
    private Welt welt;
    public WindowClosingListener(Welt welt) {
        super();
        this.welt=welt;
    }

    /**
     * Wird ausgeführt, wenn der Benutzer das Fenster schließen möchte.
     * Je nach Phase des Spiels wird unterschiedlich verfahren:
     * In Phase 0 (noch keine Züge begonnen) wird ohne Nachfrage geschlossen und das Programm beendet.
     * n Phasen 1 und 2 wird eine Bestätigungsabfrage angezeigt, weil nur zu Beginn eines Zuges gespeichert werden kann.
     * @param e the event to be processed
     */
    @Override
    public void windowClosing(WindowEvent e) {
        Window window = e.getWindow();
        int phase = welt.getPhase();
        if(phase==0) {
            window.dispose();
            System.exit(0);
        }else {
            int result = JOptionPane.showConfirmDialog(window, "Wollen Sie die Anwendung wirklich schließen? Es kann nur am Anfang eines Zuges gespeichert werden. ");
            if (result == 0) {
                window.dispose();
                System.exit(0);
            }
        }

    }
}
