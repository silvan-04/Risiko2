package Risiko.ui.gui.panels;

import Risiko.domain.*;
import Risiko.domain.exceptions.IdException;
import Risiko.entities.Land;
// import org.apache.batik.swing.JSVGCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class WeltPanel extends JPanel {
    private Welt welt;
    private Map<String,String> lands = new HashMap<>();
//    private JSVGCanvas weltCanvas;
    private final ImageIcon image;
    private final ImageIcon greyImage;
    private JLabel imageLabel;
    private JLabel greyLabel;
    private JPanel buttons;
    private JLayeredPane layeredPane;
    private String lastClickedCountry;
    private boolean countryClicked = false;
    private double ratio;
    private int maxHeight;
    private int maxWidth;


    public WeltPanel(Welt welt) {
        super();
        this.welt = welt;
        this.maxHeight = 972;
        this.maxWidth = 1728;
        this.ratio = 0.9;

        createGreyToIdMap();
//
        this.image = new ImageIcon("mapRisiko.png");
        this.greyImage = new ImageIcon("mapGrayscale.png");
        this.setBackground(Color.black);
        this.layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(this.maxWidth, this.maxHeight));
        this.imageLabel= new JLabel(image);
        imageLabel.setOpaque(false);
        imageLabel.setBounds(0, 0, maxWidth, maxHeight);

        layeredPane.add(imageLabel,Integer.valueOf(0));

        JPanel buttons = new LaenderInfoPanel(this.ratio,welt);
        this.buttons = buttons;
        buttons.setBounds(0,0,maxWidth,maxHeight);
        layeredPane.add(buttons,Integer.valueOf(2));
        this.greyLabel= new JLabel(greyImage);
        greyLabel.setOpaque(true);
        greyLabel.setBounds(0,0,maxWidth,maxHeight);
        greyLabel.setVisible(false);
        layeredPane.add(greyLabel,Integer.valueOf(3));
        this.add(layeredPane);
    }

    public String getLastClickedCountry() {
        return lastClickedCountry;
    }
    public boolean getCountryClicked() {
        return countryClicked;
    }
    public void setLastClickedCountry(String lastClickedCountry) {
        this.lastClickedCountry = lastClickedCountry;
    }
    public void setCountryClicked(boolean countryClicked) {
        this.countryClicked = countryClicked;
    }

    /**
     * Erstellt eine Map für die farbcodes der GreyMap welche auf die ids der länder zeigen, die map "lands" vom Weltpanel wird mit der neuen map gesettet
     */
    public void createGreyToIdMap(){
        this.lands.clear();
        this.lands.put("ff666666","ak");
        this.lands.put("ff6e6e6e","nt");
        this.lands.put("ff737373","ab");
        this.lands.put("ff717171","on");
        this.lands.put("ff7e7e7e","qb");
        this.lands.put("ff696969","gr");
        this.lands.put("ff767676","unw");
        this.lands.put("ff7b7b7b","une");
        this.lands.put("ff797979","cm");
        this.lands.put("ff868686","ve");
        this.lands.put("ff808080","pe");
        this.lands.put("ff888888","br");
        this.lands.put("ff838383","ar");
        this.lands.put("ffb7b7b7","is");
        this.lands.put("ffb9b9b9","sk");
        this.lands.put("ffb4b4b4","gb");
        this.lands.put("ffbebebe","eun");
        this.lands.put("ffbcbcbc","euw");
        this.lands.put("ffc1c1c1","eus");
        this.lands.put("ffc3c3c3","ua");
        this.lands.put("ffa7a7a7","an");
        this.lands.put("ffa5a5a5","eg");
        this.lands.put("ffa2a2a2","ao");
        this.lands.put("ffa0a0a0","ko");
        this.lands.put("ff9d9d9d","as");
        this.lands.put("ff9a9a9a","ma");
        this.lands.put("ff161616","af");
        this.lands.put("ff2a2a2a","ch");
        this.lands.put("ff2f2f2f","in");
        this.lands.put("ff212121","ir");
        this.lands.put("ff4b4b4b","jp");
        this.lands.put("ff2d2d2d","jp");
        this.lands.put("ff242424","ka");
        this.lands.put("ff272727","mo");
        this.lands.put("ff131313","me");
        this.lands.put("ff1b1b1b","mn");
        this.lands.put("ff181818","ur");
        this.lands.put("ff1e1e1e","ya");
        this.lands.put("ff323232","aso");
        this.lands.put("ff010101","id");
        this.lands.put("ff070707","ng");
        this.lands.put("ff020202","auw");
        this.lands.put("ff030303","auo");
    }

    /**
     * wird aufgerufen bei änderung der gui Größe, size von Weltpanel wird übergeben. größe der bilder, infos und layeredPane wird auf diese angepasst
     * auf den verfügbaren Platz und die maximale größe für die Bilder.
     * @param size
     */
    public void resizeImage(Dimension size) {
        this.layeredPane.setPreferredSize(size);
        double newHeight = Math.min((int)size.getHeight(), this.maxHeight);
        double newWidth = Math.min((int)size.getWidth(), this.maxWidth);
        double widthRatio = (double) newWidth/image.getIconWidth();
        double heightRatio = (double) newHeight/image.getIconHeight();
        if(widthRatio<heightRatio){
            newHeight = (widthRatio)*image.getIconHeight();
            this.ratio=widthRatio;
        }else{
            newWidth = (heightRatio)*image.getIconWidth();
            this.ratio=heightRatio;
        }
        Image skaliert = image.getImage().getScaledInstance((int)newWidth, (int)newHeight, Image.SCALE_SMOOTH);
        Image greySkaliert = greyImage.getImage().getScaledInstance((int)newWidth, (int)newHeight, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(skaliert));
        greyLabel.setIcon(new ImageIcon(greySkaliert));
        imageLabel.setBounds(0,0,(int)newWidth,(int)newHeight);
        greyLabel.setBounds(0,0,(int)newWidth,(int)newHeight);
        greyLabel.setVisible(false);
//        weltCanvas.setBounds(0,0,(int)newWidth,(int)newHeight);
        LaenderInfoPanel buttonsPanel = (LaenderInfoPanel)this.buttons;
        buttonsPanel.setRatio(this.ratio);
        this.buttons = buttonsPanel;
    }

    /**
     * returnt den String id vom geklickten Land, null wenn kein id in map gefunden wird
     * x und y sind koordinaten auf dem weltpanel, size die größe
     * @param x
     * @param y
     * @param size
     */
    public void clickedCountry(int x, int y, Dimension size){
        //Läd die greymap auf ein bufferedimage
        BufferedImage image = new BufferedImage((int)size.getWidth(), (int)size.getHeight(), BufferedImage.TYPE_INT_ARGB);
        JLabel greyCopy = greyLabel;
        this.layeredPane.remove(greyCopy);
        greyCopy.setVisible(true);
        greyCopy.paint(image.getGraphics());
//        holt die Farbe an der Stelle x,y
        Color farbe = new Color(image.getRGB(x,y));
        int rgb = farbe.getRGB();
        String hex = Integer.toHexString(rgb);
        //Farbe wird in map gegeben um id des landes zu bekommen
        String id = lands.get(hex);
        this.countryClicked = (id!=null);
        //Settet lastClickedCountry als string id aus map
        this.lastClickedCountry =  id;
    }

    public void updateWelt(Welt welt){
        this.welt=welt;
        ((LaenderInfoPanel)buttons).updateWelt(welt);
        revalidate();
        repaint();
    }
}

