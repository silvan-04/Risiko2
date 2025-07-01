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
