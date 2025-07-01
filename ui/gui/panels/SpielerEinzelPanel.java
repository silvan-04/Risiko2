package Risiko.ui.gui.panels;

import Risiko.domain.Welt;
import Risiko.domain.exceptions.DoppelterNameException;
import Risiko.entities.Land;
import Risiko.entities.Spieler;

import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpielerEinzelPanel extends JPanel {
    private Welt welt;
    private Spieler spieler;
    private int einheiten;
    private int laender;
    private int preferedHeight;
    private int preferedWidth;

    /**
     * Spieler Panel wird erstellt:
     * zeigt Spieler Namen, Einheiten und Länderanzahl
     * @param spieler
     * @param welt
     * @param size
     */
    public SpielerEinzelPanel(Spieler spieler, Welt welt, Dimension size)  {
        super();
        this.spieler = spieler;
        this.welt = welt;
        this.preferedHeight = 200;
        this.preferedWidth = 200;
//        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setLayout(null);
        this.setOpaque(false);
        List<Land> spielerLaender = welt.landAusgabeVonSpieler(spieler);
        einheiten=0;
        for(Land land:spielerLaender){
            einheiten= einheiten + land.getArmee();
        }
        laender = spieler.getAnzahlLaender();
        JLayeredPane jlp = getJLayeredPane(spieler);
        this.setPreferredSize(new Dimension(preferedWidth,preferedHeight));
        this.add(jlp);
        this.setVisible(true);
        this.revalidate();
        this.repaint();
    }

    /**
     * Erstellt JlayeredPane, welches alles für das einzelne Spielerpanel beinhaltet
     * @param spieler
     * @return
     */
    private JLayeredPane getJLayeredPane(Spieler spieler) {
        JLayeredPane jlp = new JLayeredPane();
        jlp.setPreferredSize(new Dimension(preferedWidth,preferedHeight));
        jlp.setOpaque(false);
        jlp.setBounds(0,0,preferedWidth,preferedHeight);
        RoundRect rect = new RoundRect(this.spieler, this.spieler.getIstAmZug(),new Dimension(preferedWidth*2,preferedHeight*2));
        rect.setOpaque(false);
        rect.setBounds(0,0,preferedWidth,preferedHeight);
        jlp.add(rect,Integer.valueOf(0));
        ImageIcon img;
        //img ist schwarz weiß wenn spieler nicht am leben
        if(spieler.getLebendig()) {
            img = new ImageIcon(spieler.getCharBild());
        }else {
            img = bildToGray(new ImageIcon(spieler.getCharBild()));
        }
        //skalliert bild auf panel größe
        double newWidth = Math.min(img.getIconWidth(),preferedWidth);
        double newHeight = Math.min(img.getIconHeight(),preferedHeight);
        double widthRatio = (double) newWidth/img.getIconWidth();
        double heightRatio = (double) newHeight/img.getIconHeight();
        if(widthRatio<heightRatio){
            newHeight = (widthRatio)*img.getIconHeight();
        }else{
            newWidth = (heightRatio)*img.getIconWidth();
        }
        Image scaled = img.getImage().getScaledInstance((int)newWidth, (int)newHeight, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaled));
        imageLabel.setOpaque(false);
        imageLabel.setBounds(preferedWidth/4,0,preferedWidth,preferedHeight);
        jlp.add(imageLabel,Integer.valueOf(1));
        JLabel name = new JLabel(spieler.getName());
        name.setForeground(Color.white);
        name.setFont(name.getFont().deriveFont(Font.BOLD));
        name.setBounds(10,0,preferedWidth,preferedHeight/3);
        name.setOpaque(false);
        jlp.add(name,Integer.valueOf(2));
        JLabel einheitenLabel = new JLabel("Einheiten: "+einheiten);
        einheitenLabel.setForeground(Color.white);
        einheitenLabel.setFont(einheitenLabel.getFont().deriveFont(Font.BOLD));
        einheitenLabel.setBounds(10,0,preferedWidth,preferedHeight*2/3);
        einheitenLabel.setOpaque(false);
        jlp.add(einheitenLabel,Integer.valueOf(3));
        JLabel laenderLabel = new JLabel("Länder: "+laender);
        laenderLabel.setForeground(Color.white);
        laenderLabel.setFont(laenderLabel.getFont().deriveFont(Font.BOLD));
        laenderLabel.setBounds(10,0,preferedWidth,preferedHeight);
        laenderLabel.setOpaque(false);
        jlp.add(laenderLabel,Integer.valueOf(2));
        return jlp;
    }

    /**
     * ändert die größe der einzelnen spielerPanels
     * @param preferredWidth
     * @param preferredHeight
     */
    public void setPreferredHeight(int preferredWidth, int preferredHeight) {
        this.preferedHeight = preferredHeight;
        this.preferedWidth = preferredWidth;
        this.setPreferredSize(new Dimension(preferedWidth,preferedHeight));
        this.removeAll();
        JLayeredPane jlp = getJLayeredPane(spieler);
        add(jlp);
        revalidate();
        repaint();
    }

    /**
     * gibt das mitgegebene ImageIcon als schwarzweiß version zurück
     * @param img
     * @return
     */
    public ImageIcon bildToGray(ImageIcon img) {
        BufferedImage source = new BufferedImage(img.getIconWidth(),img.getIconHeight(),BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = source.createGraphics();

        g2d.setComposite(AlphaComposite.SrcOver);
        g2d.drawImage(img.getImage(),0,0,null);
        g2d.setComposite(AlphaComposite.SrcAtop);
        g2d.setColor(new Color(128,128,128,190));
        g2d.fillRect(0,0,source.getWidth(),source.getHeight());
        g2d.dispose();
        return new ImageIcon(source);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.removeAll();
        List<Land> spielerLaender = welt.landAusgabeVonSpieler(spieler);
        einheiten=0;
        for(Land land:spielerLaender){
            einheiten= einheiten + land.getArmee();
        }
        laender = spieler.getAnzahlLaender();
        JLayeredPane jlp = getJLayeredPane(spieler);
        this.setPreferredSize(new Dimension(preferedWidth,preferedHeight));
        this.add(jlp);
    }
}
