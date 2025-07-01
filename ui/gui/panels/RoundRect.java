package Risiko.ui.gui.panels;

import Risiko.entities.*;

import javax.swing.*;
import java.awt.*;

public class RoundRect extends JPanel {
    private Spieler spieler;
    private boolean istAmZug;
    private Dimension dimension;
    public RoundRect(Spieler spieler,boolean istAmZug,Dimension dimension) {
        this.spieler = spieler;
        this.istAmZug = istAmZug;
        this.dimension = dimension;
    }

    /**
     * Spieler in werden in Runde angezeigt + aktiver Spieler weird gekennzeichnet
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        this.setBorder(BorderFactory.createLineBorder(Color.white,1));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        g2d.setColor(Color.decode(this.spieler.getFarbe()));
        g2d.fillRoundRect(0, 0,(int)this.dimension.getWidth() ,(int)this.dimension.getHeight(),(int)Math.round(this.dimension.getWidth()*0.2),(int)Math.round(this.dimension.getHeight()*0.2) );
        if(this.istAmZug) {
            g2d.setColor(Color.white);
            g2d.setStroke(new BasicStroke((int) (2)));
        }
        g2d.drawRoundRect(0, 0,(int)this.dimension.getWidth() ,(int)this.dimension.getHeight(),(int)Math.round(this.dimension.getWidth()*0.2),(int)Math.round(this.dimension.getHeight()*0.2));
        g2d.dispose();

    }
}
