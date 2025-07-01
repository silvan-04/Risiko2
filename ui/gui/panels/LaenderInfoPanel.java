package Risiko.ui.gui.panels;

import Risiko.domain.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class LaenderInfoPanel extends JPanel {
    private double ratio;
    private Welt welt;
    public LaenderInfoPanel(double ratio,Welt welt) {
        this.ratio = ratio;
        this.welt = welt;
        this.setOpaque(false);
    }

    /**
     * Bei änderung der ratio wird diese neu gesettet und das Panel repaintet
     * @param ratio
     */
    public void setRatio(double ratio) {
        this.ratio = ratio;
        repaint();
    }




    /**
     * Zeichnet Kreise mit Farbe der Besitzer und anzahl der Einheiten des jeweiligen Landes.
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        this.removeAll();
        this.revalidate();
        Color umrandung = Color.white;
        int randBreite = 2;
        //Farben der Land besitzer werden gegettet
        // Nordamerika
        Color alaska = Color.decode(welt.idToLand("ak").getBesitzer().getFarbe());
        Color nordwestTerritorien = Color.decode(welt.idToLand("nt").getBesitzer().getFarbe());
        Color alberta = Color.decode(welt.idToLand("ab").getBesitzer().getFarbe());
        Color ontario = Color.decode(welt.idToLand("on").getBesitzer().getFarbe());
        Color quebec = Color.decode(welt.idToLand("qb").getBesitzer().getFarbe());
        Color groenland = Color.decode(welt.idToLand("gr").getBesitzer().getFarbe());
        Color weststaaten = Color.decode(welt.idToLand("unw").getBesitzer().getFarbe());
        Color oststaaten = Color.decode(welt.idToLand("une").getBesitzer().getFarbe());
        Color mittelamerika = Color.decode(welt.idToLand("cm").getBesitzer().getFarbe());

        // Südamerika
        Color venezuela = Color.decode(welt.idToLand("ve").getBesitzer().getFarbe());
        Color peru = Color.decode(welt.idToLand("pe").getBesitzer().getFarbe());
        Color brasilien = Color.decode(welt.idToLand("br").getBesitzer().getFarbe());
        Color argentinien = Color.decode(welt.idToLand("ar").getBesitzer().getFarbe());

        // Europa
        Color island = Color.decode(welt.idToLand("is").getBesitzer().getFarbe());
        Color skandinavien = Color.decode(welt.idToLand("sk").getBesitzer().getFarbe());
        Color grossbritannien = Color.decode(welt.idToLand("gb").getBesitzer().getFarbe());
        Color nordeuropa = Color.decode(welt.idToLand("eun").getBesitzer().getFarbe());
        Color westeuropa = Color.decode(welt.idToLand("euw").getBesitzer().getFarbe());
        Color suedeuropa = Color.decode(welt.idToLand("eus").getBesitzer().getFarbe());
        Color ukraine = Color.decode(welt.idToLand("ua").getBesitzer().getFarbe());

        // Afrika
        Color nordafrika = Color.decode(welt.idToLand("an").getBesitzer().getFarbe());
        Color aegypten = Color.decode(welt.idToLand("eg").getBesitzer().getFarbe());
        Color ostafrika = Color.decode(welt.idToLand("ao").getBesitzer().getFarbe());
        Color kongo = Color.decode(welt.idToLand("ko").getBesitzer().getFarbe());
        Color suedafrika = Color.decode(welt.idToLand("as").getBesitzer().getFarbe());
        Color madagaskar = Color.decode(welt.idToLand("ma").getBesitzer().getFarbe());

        // Asien
        Color afghanistan = Color.decode(welt.idToLand("af").getBesitzer().getFarbe());
        Color china = Color.decode(welt.idToLand("ch").getBesitzer().getFarbe());
        Color indien = Color.decode(welt.idToLand("in").getBesitzer().getFarbe());
        Color irkutsk = Color.decode(welt.idToLand("ir").getBesitzer().getFarbe());
        Color japan = Color.decode(welt.idToLand("jp").getBesitzer().getFarbe());
        Color kamtschatka = Color.decode(welt.idToLand("ka").getBesitzer().getFarbe());
        Color mongolei = Color.decode(welt.idToLand("mo").getBesitzer().getFarbe());
        Color mittlererOsten = Color.decode(welt.idToLand("me").getBesitzer().getFarbe());
        Color sibirien = Color.decode(welt.idToLand("mn").getBesitzer().getFarbe());
        Color ural = Color.decode(welt.idToLand("ur").getBesitzer().getFarbe());
        Color jakutien = Color.decode(welt.idToLand("ya").getBesitzer().getFarbe());
        Color suedostasien = Color.decode(welt.idToLand("aso").getBesitzer().getFarbe());

        // Australien
        Color indonesien = Color.decode(welt.idToLand("id").getBesitzer().getFarbe());
        Color neuguinea = Color.decode(welt.idToLand("ng").getBesitzer().getFarbe());
        Color westaustralien = Color.decode(welt.idToLand("auw").getBesitzer().getFarbe());
        Color ostaustralien = Color.decode(welt.idToLand("auo").getBesitzer().getFarbe());



        int durchmesser = (int) (32 * this.ratio + 1); //Durchmesser wird mit ratio berechnet


        g2d.setColor(alaska); //Farbe vom besitzer des jeweiligen landes wird gesettet
        g2d.fillOval((int) (99.2 * this.ratio), (int) (259.9 * this.ratio), durchmesser, durchmesser); // Kreis wird mit Koordinaten von orignalen Karte mal der jetzigen ratio gezeichnet
        g2d.setColor(umrandung); //Farbe der Umrandung wird gesettet
        g2d.setStroke(new BasicStroke((int) (randBreite))); //Umrandung
        g2d.drawOval((int) (99.2 * this.ratio), (int) (259.9 * this.ratio), durchmesser, durchmesser); // Kreis wird gemalt
        String einheitenStr =Integer.toString(welt.idToLand("ak").getArmee());// einheiten vom Land 
        JLabel einheiten = new JLabel(einheitenStr); 

        einheiten.setFont(new Font("Arial", Font.BOLD, (int) (durchmesser*0.6))); //Font und Größe werden von Jlabel eingestellt
        einheiten.setForeground(Color.white);
        //Breite vom text wird berechnet
        FontMetrics metrics = einheiten.getFontMetrics(einheiten.getFont());
        int textBreite = metrics.stringWidth(einheitenStr);
        //JLabel wird mit koordinaten vom orignal bild mal ratio plaziert (+durchmesser/2 - textbreite um zu zentrieren)
        einheiten.setBounds((int) ((99.2 * this.ratio)+((double) durchmesser /2)-((double) textBreite /2)), (int) (259.9 * this.ratio),durchmesser,durchmesser );
        this.add(einheiten);

        g2d.setColor(nordwestTerritorien);
        g2d.fillOval((int)(220.1 * this.ratio), (int)(258.2 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(220.1 * this.ratio), (int)(258.2 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_nt = Integer.toString(welt.idToLand("nt").getArmee());
        JLabel einheiten_nt = new JLabel(einheitenStr_nt);
        einheiten_nt.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_nt.setForeground(Color.white);
        FontMetrics metrics_nt = einheiten_nt.getFontMetrics(einheiten_nt.getFont());
        int textBreite_nt = metrics_nt.stringWidth(einheitenStr_nt);
        einheiten_nt.setBounds((int)((220.1 * this.ratio) + (durchmesser / 2.0) - (textBreite_nt / 2.0)), (int)(258.2 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_nt);

        g2d.setColor(alberta);
        g2d.fillOval((int)(271.2 * this.ratio), (int)(319.8 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(271.2 * this.ratio), (int)(319.8 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_ab = Integer.toString(welt.idToLand("ab").getArmee());
        JLabel einheiten_ab = new JLabel(einheitenStr_ab);
        einheiten_ab.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_ab.setForeground(Color.white);
        FontMetrics metrics_ab = einheiten_ab.getFontMetrics(einheiten_ab.getFont());
        int textBreite_ab = metrics_ab.stringWidth(einheitenStr_ab);
        einheiten_ab.setBounds((int)((271.2 * this.ratio) + (durchmesser / 2.0) - (textBreite_ab / 2.0)), (int)(319.8 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_ab);

        g2d.setColor(ontario);
        g2d.fillOval((int)(379.0 * this.ratio), (int)(335.7 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(379.0 * this.ratio), (int)(335.7 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_on = Integer.toString(welt.idToLand("on").getArmee());
        JLabel einheiten_on = new JLabel(einheitenStr_on);
        einheiten_on.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_on.setForeground(Color.white);
        FontMetrics metrics_on = einheiten_on.getFontMetrics(einheiten_on.getFont());
        int textBreite_on = metrics_on.stringWidth(einheitenStr_on);
        einheiten_on.setBounds((int)((379.0 * this.ratio) + (durchmesser / 2.0) - (textBreite_on / 2.0)), (int)(335.7 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_on);

        g2d.setColor(quebec);
        g2d.fillOval((int)(524.4 * this.ratio), (int)(345.4 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(524.4 * this.ratio), (int)(345.4 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_qb = Integer.toString(welt.idToLand("qb").getArmee());
        JLabel einheiten_qb = new JLabel(einheitenStr_qb);
        einheiten_qb.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_qb.setForeground(Color.white);
        FontMetrics metrics_qb = einheiten_qb.getFontMetrics(einheiten_qb.getFont());
        int textBreite_qb = metrics_qb.stringWidth(einheitenStr_qb);
        einheiten_qb.setBounds((int)((524.4 * this.ratio) + (durchmesser / 2.0) - (textBreite_qb / 2.0)), (int)(345.4 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_qb);

        g2d.setColor(weststaaten);
        g2d.fillOval((int)(321.0 * this.ratio), (int)(427.3 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(321.0 * this.ratio), (int)(427.3 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_unw = Integer.toString(welt.idToLand("unw").getArmee());
        JLabel einheiten_unw = new JLabel(einheitenStr_unw);
        einheiten_unw.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_unw.setForeground(Color.white);
        FontMetrics metrics_unw = einheiten_unw.getFontMetrics(einheiten_unw.getFont());
        int textBreite_unw = metrics_unw.stringWidth(einheitenStr_unw);
        einheiten_unw.setBounds((int)((321.0 * this.ratio) + (durchmesser / 2.0) - (textBreite_unw / 2.0)), (int)(427.3 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_unw);

        g2d.setColor(oststaaten);
        g2d.fillOval((int)(420.3 * this.ratio), (int)(439.4 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(420.3 * this.ratio), (int)(439.4 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_une = Integer.toString(welt.idToLand("une").getArmee());
        JLabel einheiten_une = new JLabel(einheitenStr_une);
        einheiten_une.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_une.setForeground(Color.white);
        FontMetrics metrics_une = einheiten_une.getFontMetrics(einheiten_une.getFont());
        int textBreite_une = metrics_une.stringWidth(einheitenStr_une);
        einheiten_une.setBounds((int)((420.3 * this.ratio) + (durchmesser / 2.0) - (textBreite_une / 2.0)), (int)(439.4 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_une);

        g2d.setColor(mittelamerika);
        g2d.fillOval((int)(366.2 * this.ratio), (int)(537.0 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(366.2 * this.ratio), (int)(537.0 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_cm = Integer.toString(welt.idToLand("cm").getArmee());
        JLabel einheiten_cm = new JLabel(einheitenStr_cm);
        einheiten_cm.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_cm.setForeground(Color.white);
        FontMetrics metrics_cm = einheiten_cm.getFontMetrics(einheiten_cm.getFont());
        int textBreite_cm = metrics_cm.stringWidth(einheitenStr_cm);
        einheiten_cm.setBounds((int)((366.2 * this.ratio) + (durchmesser / 2.0) - (textBreite_cm / 2.0)), (int)(537.0 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_cm);

        g2d.setColor(venezuela);
        g2d.fillOval((int)(516.0 * this.ratio), (int)(637.5 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(516.0 * this.ratio), (int)(637.5 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_ve = Integer.toString(welt.idToLand("ve").getArmee());
        JLabel einheiten_ve = new JLabel(einheitenStr_ve);
        einheiten_ve.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_ve.setForeground(Color.white);
        FontMetrics metrics_ve = einheiten_ve.getFontMetrics(einheiten_ve.getFont());
        int textBreite_ve = metrics_ve.stringWidth(einheitenStr_ve);
        einheiten_ve.setBounds((int)((516.0 * this.ratio) + (durchmesser / 2.0) - (textBreite_ve / 2.0)), (int)(637.5 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_ve);

        g2d.setColor(brasilien);
        g2d.fillOval((int)(619.6 * this.ratio), (int)(701.0 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(619.6 * this.ratio), (int)(701.0 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_br = Integer.toString(welt.idToLand("br").getArmee());
        JLabel einheiten_br = new JLabel(einheitenStr_br);
        einheiten_br.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_br.setForeground(Color.white);
        FontMetrics metrics_br = einheiten_br.getFontMetrics(einheiten_br.getFont());
        int textBreite_br = metrics_br.stringWidth(einheitenStr_br);
        einheiten_br.setBounds((int)((619.6 * this.ratio) + (durchmesser / 2.0) - (textBreite_br / 2.0)), (int)(701.0 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_br);

        g2d.setColor(peru);
        g2d.fillOval((int)(524.5 * this.ratio), (int)(735.3 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(524.5 * this.ratio), (int)(735.3 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_pe = Integer.toString(welt.idToLand("pe").getArmee());
        JLabel einheiten_pe = new JLabel(einheitenStr_pe);
        einheiten_pe.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_pe.setForeground(Color.white);
        FontMetrics metrics_pe = einheiten_pe.getFontMetrics(einheiten_pe.getFont());
        int textBreite_pe = metrics_pe.stringWidth(einheitenStr_pe);
        einheiten_pe.setBounds((int)((524.5 * this.ratio) + (durchmesser / 2.0) - (textBreite_pe / 2.0)), (int)(735.3 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_pe);

        g2d.setColor(argentinien);
        g2d.fillOval((int)(545.5 * this.ratio), (int)(830.3 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(545.5 * this.ratio), (int)(830.3 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_ar = Integer.toString(welt.idToLand("ar").getArmee());
        JLabel einheiten_ar = new JLabel(einheitenStr_ar);
        einheiten_ar.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_ar.setForeground(Color.white);
        FontMetrics metrics_ar = einheiten_ar.getFontMetrics(einheiten_ar.getFont());
        int textBreite_ar = metrics_ar.stringWidth(einheitenStr_ar);
        einheiten_ar.setBounds((int)((545.5 * this.ratio) + (durchmesser / 2.0) - (textBreite_ar / 2.0)), (int)(830.3 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_ar);

        g2d.setColor(groenland);
        g2d.fillOval((int)(687.5 * this.ratio), (int)(145.5 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(687.5 * this.ratio), (int)(145.5 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_gr = Integer.toString(welt.idToLand("gr").getArmee());
        JLabel einheiten_gr = new JLabel(einheitenStr_gr);
        einheiten_gr.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_gr.setForeground(Color.white);
        FontMetrics metrics_gr = einheiten_gr.getFontMetrics(einheiten_gr.getFont());
        int textBreite_gr = metrics_gr.stringWidth(einheitenStr_gr);
        einheiten_gr.setBounds((int)((687.5 * this.ratio) + (durchmesser / 2.0) - (textBreite_gr / 2.0)), (int)(145.5 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_gr);

        g2d.setColor(island);
        g2d.fillOval((int)(1040.7 * this.ratio), (int)(144.0 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(1040.7 * this.ratio), (int)(144.0 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_is = Integer.toString(welt.idToLand("is").getArmee());
        JLabel einheiten_is = new JLabel(einheitenStr_is);
        einheiten_is.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_is.setForeground(Color.white);
        FontMetrics metrics_is = einheiten_is.getFontMetrics(einheiten_is.getFont());
        int textBreite_is = metrics_is.stringWidth(einheitenStr_is);
        einheiten_is.setBounds((int)((1040.7 * this.ratio) + (durchmesser / 2.0) - (textBreite_is / 2.0)), (int)(144.0 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_is);

        g2d.setColor(skandinavien);
        g2d.fillOval((int)(1017.0 * this.ratio), (int)(238.0 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(1017.0 * this.ratio), (int)(238.0 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_sk = Integer.toString(welt.idToLand("sk").getArmee());
        JLabel einheiten_sk = new JLabel(einheitenStr_sk);
        einheiten_sk.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_sk.setForeground(Color.white);
        FontMetrics metrics_sk = einheiten_sk.getFontMetrics(einheiten_sk.getFont());
        int textBreite_sk = metrics_sk.stringWidth(einheitenStr_sk);
        einheiten_sk.setBounds((int)((1017.0 * this.ratio) + (durchmesser / 2.0) - (textBreite_sk / 2.0)), (int)(238.0 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_sk);

        g2d.setColor(ukraine);
        g2d.fillOval((int)(1078.8 * this.ratio), (int)(328.3 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(1078.8 * this.ratio), (int)(328.3 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_ua = Integer.toString(welt.idToLand("ua").getArmee());
        JLabel einheiten_ua = new JLabel(einheitenStr_ua);
        einheiten_ua.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_ua.setForeground(Color.white);
        FontMetrics metrics_ua = einheiten_ua.getFontMetrics(einheiten_ua.getFont());
        int textBreite_ua = metrics_ua.stringWidth(einheitenStr_ua);
        einheiten_ua.setBounds((int)((1078.8 * this.ratio) + (durchmesser / 2.0) - (textBreite_ua / 2.0)), (int)(328.3 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_ua);

        g2d.setColor(nordeuropa);
        g2d.fillOval((int)(944.0 * this.ratio), (int)(367.9 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(944.0 * this.ratio), (int)(367.9 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_eun = Integer.toString(welt.idToLand("eun").getArmee());
        JLabel einheiten_eun = new JLabel(einheitenStr_eun);
        einheiten_eun.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_eun.setForeground(Color.white);
        FontMetrics metrics_eun = einheiten_eun.getFontMetrics(einheiten_eun.getFont());
        int textBreite_eun = metrics_eun.stringWidth(einheitenStr_eun);
        einheiten_eun.setBounds((int)((944.0 * this.ratio) + (durchmesser / 2.0) - (textBreite_eun / 2.0)), (int)(367.9 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_eun);

        g2d.setColor(suedeuropa);
        g2d.fillOval((int)(955.2 * this.ratio), (int)(417.2 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(955.2 * this.ratio), (int)(417.2 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_eus = Integer.toString(welt.idToLand("eus").getArmee());
        JLabel einheiten_eus = new JLabel(einheitenStr_eus);
        einheiten_eus.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_eus.setForeground(Color.white);
        FontMetrics metrics_eus = einheiten_eus.getFontMetrics(einheiten_eus.getFont());
        int textBreite_eus = metrics_eus.stringWidth(einheitenStr_eus);
        einheiten_eus.setBounds((int)((955.2 * this.ratio) + (durchmesser / 2.0) - (textBreite_eus / 2.0)), (int)(417.2 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_eus);

        g2d.setColor(westeuropa);
        g2d.fillOval((int)(871.0 * this.ratio), (int)(445.7 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(871.0 * this.ratio), (int)(445.7 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_euw = Integer.toString(welt.idToLand("euw").getArmee());
        JLabel einheiten_euw = new JLabel(einheitenStr_euw);
        einheiten_euw.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_euw.setForeground(Color.white);
        FontMetrics metrics_euw = einheiten_euw.getFontMetrics(einheiten_euw.getFont());
        int textBreite_euw = metrics_euw.stringWidth(einheitenStr_euw);
        einheiten_euw.setBounds((int)((871.0 * this.ratio) + (durchmesser / 2.0) - (textBreite_euw / 2.0)), (int)(445.7 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_euw);

        g2d.setColor(grossbritannien);
        g2d.fillOval((int)(807.5 * this.ratio), (int)(329.3 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(807.5 * this.ratio), (int)(329.3 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_gb = Integer.toString(welt.idToLand("gb").getArmee());
        JLabel einheiten_gb = new JLabel(einheitenStr_gb);
        einheiten_gb.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_gb.setForeground(Color.white);
        FontMetrics metrics_gb = einheiten_gb.getFontMetrics(einheiten_gb.getFont());
        int textBreite_gb = metrics_gb.stringWidth(einheitenStr_gb);
        einheiten_gb.setBounds((int)((807.5 * this.ratio) + (durchmesser / 2.0) - (textBreite_gb / 2.0)), (int)(329.3 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_gb);

        g2d.setColor(nordafrika);
        g2d.fillOval((int)(903.0 * this.ratio), (int)(546.3 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(903.0 * this.ratio), (int)(546.3 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_an = Integer.toString(welt.idToLand("an").getArmee());
        JLabel einheiten_an = new JLabel(einheitenStr_an);
        einheiten_an.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_an.setForeground(Color.white);
        FontMetrics metrics_an = einheiten_an.getFontMetrics(einheiten_an.getFont());
        int textBreite_an = metrics_an.stringWidth(einheitenStr_an);
        einheiten_an.setBounds((int)((903.0 * this.ratio) + (durchmesser / 2.0) - (textBreite_an / 2.0)), (int)(546.3 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_an);

        g2d.setColor(aegypten);
        g2d.fillOval((int)(1023.9 * this.ratio), (int)(519.6 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(1023.9 * this.ratio), (int)(519.6 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_eg = Integer.toString(welt.idToLand("eg").getArmee());
        JLabel einheiten_eg = new JLabel(einheitenStr_eg);
        einheiten_eg.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_eg.setForeground(Color.white);
        FontMetrics metrics_eg = einheiten_eg.getFontMetrics(einheiten_eg.getFont());
        int textBreite_eg = metrics_eg.stringWidth(einheitenStr_eg);
        einheiten_eg.setBounds((int)((1023.9 * this.ratio) + (durchmesser / 2.0) - (textBreite_eg / 2.0)), (int)(519.6 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_eg);

        g2d.setColor(ostafrika);
        g2d.fillOval((int)(1058.35 * this.ratio), (int)(603.45 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(1058.35 * this.ratio), (int)(603.45 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_ao = Integer.toString(welt.idToLand("ao").getArmee());
        JLabel einheiten_ao = new JLabel(einheitenStr_ao);
        einheiten_ao.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_ao.setForeground(Color.white);
        FontMetrics metrics_ao = einheiten_ao.getFontMetrics(einheiten_ao.getFont());
        int textBreite_ao = metrics_ao.stringWidth(einheitenStr_ao);
        einheiten_ao.setBounds((int)((1058.35 * this.ratio) + (durchmesser / 2.0) - (textBreite_ao / 2.0)), (int)(603.45 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_ao);

        g2d.setColor(kongo);
        g2d.fillOval((int)(1003.05 * this.ratio), (int)(653.95 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(1003.05 * this.ratio), (int)(653.95 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_ko = Integer.toString(welt.idToLand("ko").getArmee());
        JLabel einheiten_ko = new JLabel(einheitenStr_ko);
        einheiten_ko.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_ko.setForeground(Color.white);
        FontMetrics metrics_ko = einheiten_ko.getFontMetrics(einheiten_ko.getFont());
        int textBreite_ko = metrics_ko.stringWidth(einheitenStr_ko);
        einheiten_ko.setBounds((int)((1003.05 * this.ratio) + (durchmesser / 2.0) - (textBreite_ko / 2.0)), (int)(653.95 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_ko);

        g2d.setColor(suedafrika);
        g2d.fillOval((int)(1013.35 * this.ratio), (int)(756.95 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(1013.35 * this.ratio), (int)(756.95 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_as = Integer.toString(welt.idToLand("as").getArmee());
        JLabel einheiten_as = new JLabel(einheitenStr_as);
        einheiten_as.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_as.setForeground(Color.white);
        FontMetrics metrics_as = einheiten_as.getFontMetrics(einheiten_as.getFont());
        int textBreite_as = metrics_as.stringWidth(einheitenStr_as);
        einheiten_as.setBounds((int)((1013.35 * this.ratio) + (durchmesser / 2.0) - (textBreite_as / 2.0)), (int)(756.95 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_as);

        g2d.setColor(madagaskar);
        g2d.fillOval((int)(1161.95 * this.ratio), (int)(764.65 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(1161.95 * this.ratio), (int)(764.65 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_ma = Integer.toString(welt.idToLand("ma").getArmee());
        JLabel einheiten_ma = new JLabel(einheitenStr_ma);
        einheiten_ma.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_ma.setForeground(Color.white);
        FontMetrics metrics_ma = einheiten_ma.getFontMetrics(einheiten_ma.getFont());
        int textBreite_ma = metrics_ma.stringWidth(einheitenStr_ma);
        einheiten_ma.setBounds((int)((1161.95 * this.ratio) + (durchmesser / 2.0) - (textBreite_ma / 2.0)), (int)(764.65 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_ma);

        g2d.setColor(indonesien);
        g2d.fillOval((int)(1431.45 * this.ratio), (int)(682.95 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(1431.45 * this.ratio), (int)(682.95 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_id = Integer.toString(welt.idToLand("id").getArmee());
        JLabel einheiten_id = new JLabel(einheitenStr_id);
        einheiten_id.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_id.setForeground(Color.white);
        FontMetrics metrics_id = einheiten_id.getFontMetrics(einheiten_id.getFont());
        int textBreite_id = metrics_id.stringWidth(einheitenStr_id);
        einheiten_id.setBounds((int)((1431.45 * this.ratio) + (durchmesser / 2.0) - (textBreite_id / 2.0)), (int)(682.95 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_id);

        g2d.setColor(neuguinea);
        g2d.fillOval((int)(1623.25 * this.ratio), (int)(664.35 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(1623.25 * this.ratio), (int)(664.35 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_ng = Integer.toString(welt.idToLand("ng").getArmee());
        JLabel einheiten_ng = new JLabel(einheitenStr_ng);
        einheiten_ng.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_ng.setForeground(Color.white);
        FontMetrics metrics_ng = einheiten_ng.getFontMetrics(einheiten_ng.getFont());
        int textBreite_ng = metrics_ng.stringWidth(einheitenStr_ng);
        einheiten_ng.setBounds((int)((1623.25 * this.ratio) + (durchmesser / 2.0) - (textBreite_ng / 2.0)), (int)(664.35 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_ng);

        g2d.setColor(westaustralien);
        g2d.fillOval((int)(1532.35 * this.ratio), (int)(777.05 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(1532.35 * this.ratio), (int)(777.05 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_auw = Integer.toString(welt.idToLand("auw").getArmee());
        JLabel einheiten_auw = new JLabel(einheitenStr_auw);
        einheiten_auw.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_auw.setForeground(Color.white);
        FontMetrics metrics_auw = einheiten_auw.getFontMetrics(einheiten_auw.getFont());
        int textBreite_auw = metrics_auw.stringWidth(einheitenStr_auw);
        einheiten_auw.setBounds((int)((1532.35 * this.ratio) + (durchmesser / 2.0) - (textBreite_auw / 2.0)), (int)(777.05 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_auw);

        g2d.setColor(ostaustralien);
        g2d.fillOval((int)(1648.35 * this.ratio), (int)(811.15 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(1648.35 * this.ratio), (int)(811.15 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_auo = Integer.toString(welt.idToLand("auo").getArmee());
        JLabel einheiten_auo = new JLabel(einheitenStr_auo);
        einheiten_auo.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_auo.setForeground(Color.white);
        FontMetrics metrics_auo = einheiten_auo.getFontMetrics(einheiten_auo.getFont());
        int textBreite_auo = metrics_auo.stringWidth(einheitenStr_auo);
        einheiten_auo.setBounds((int)((1648.35 * this.ratio) + (durchmesser / 2.0) - (textBreite_auo / 2.0)), (int)(811.15 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_auo);

        g2d.setColor(ural);
        g2d.fillOval((int)(1190.45 * this.ratio), (int)(293.75 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(1190.45 * this.ratio), (int)(293.75 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_ur = Integer.toString(welt.idToLand("ur").getArmee());
        JLabel einheiten_ur = new JLabel(einheitenStr_ur);
        einheiten_ur.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_ur.setForeground(Color.white);
        FontMetrics metrics_ur = einheiten_ur.getFontMetrics(einheiten_ur.getFont());
        int textBreite_ur = metrics_ur.stringWidth(einheitenStr_ur);
        einheiten_ur.setBounds((int)((1190.45 * this.ratio) + (durchmesser / 2.0) - (textBreite_ur / 2.0)), (int)(293.75 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_ur);

        g2d.setColor(sibirien);
        g2d.fillOval((int)(1317.65 * this.ratio), (int)(281.35 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(1317.65 * this.ratio), (int)(281.35 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_mn = Integer.toString(welt.idToLand("mn").getArmee());
        JLabel einheiten_mn = new JLabel(einheitenStr_mn);
        einheiten_mn.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_mn.setForeground(Color.white);
        FontMetrics metrics_mn = einheiten_mn.getFontMetrics(einheiten_mn.getFont());
        int textBreite_mn = metrics_mn.stringWidth(einheitenStr_mn);
        einheiten_mn.setBounds((int)((1317.65 * this.ratio) + (durchmesser / 2.0) - (textBreite_mn / 2.0)), (int)(281.35 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_mn);

        g2d.setColor(afghanistan);
        g2d.fillOval((int)(1226.55 * this.ratio), (int)(420.05 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(1226.55 * this.ratio), (int)(420.05 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_af = Integer.toString(welt.idToLand("af").getArmee());
        JLabel einheiten_af = new JLabel(einheitenStr_af);
        einheiten_af.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_af.setForeground(Color.white);
        FontMetrics metrics_af = einheiten_af.getFontMetrics(einheiten_af.getFont());
        int textBreite_af = metrics_af.stringWidth(einheitenStr_af);
        einheiten_af.setBounds((int)((1226.55 * this.ratio) + (durchmesser / 2.0) - (textBreite_af / 2.0)), (int)(420.05 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_af);

        g2d.setColor(mittlererOsten);
        g2d.fillOval((int)(1111.45 * this.ratio), (int)(467.65 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(1111.45 * this.ratio), (int)(467.65 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_me = Integer.toString(welt.idToLand("me").getArmee());
        JLabel einheiten_me = new JLabel(einheitenStr_me);
        einheiten_me.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_me.setForeground(Color.white);
        FontMetrics metrics_me = einheiten_me.getFontMetrics(einheiten_me.getFont());
        int textBreite_me = metrics_me.stringWidth(einheitenStr_me);
        einheiten_me.setBounds((int)((1111.45 * this.ratio) + (durchmesser / 2.0) - (textBreite_me / 2.0)), (int)(467.65 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_me);

        g2d.setColor(indien);
        g2d.fillOval((int)(1278.95 * this.ratio), (int)(510.95 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(1278.95 * this.ratio), (int)(510.95 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_in = Integer.toString(welt.idToLand("in").getArmee());
        JLabel einheiten_in = new JLabel(einheitenStr_in);
        einheiten_in.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_in.setForeground(Color.white);
        FontMetrics metrics_in = einheiten_in.getFontMetrics(einheiten_in.getFont());
        int textBreite_in = metrics_in.stringWidth(einheitenStr_in);
        einheiten_in.setBounds((int)((1278.95 * this.ratio) + (durchmesser / 2.0) - (textBreite_in / 2.0)), (int)(510.95 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_in);

        g2d.setColor(suedostasien);
        g2d.fillOval((int)(1409.55 * this.ratio), (int)(576.05 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(1409.55 * this.ratio), (int)(576.05 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_aso = Integer.toString(welt.idToLand("aso").getArmee());
        JLabel einheiten_aso = new JLabel(einheitenStr_aso);
        einheiten_aso.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_aso.setForeground(Color.white);
        FontMetrics metrics_aso = einheiten_aso.getFontMetrics(einheiten_aso.getFont());
        int textBreite_aso = metrics_aso.stringWidth(einheitenStr_aso);
        einheiten_aso.setBounds((int)((1409.55 * this.ratio) + (durchmesser / 2.0) - (textBreite_aso / 2.0)), (int)(576.05 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_aso);

        g2d.setColor(china);
        g2d.fillOval((int)(1422.25 * this.ratio), (int)(431.85 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(1422.25 * this.ratio), (int)(431.85 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_ch = Integer.toString(welt.idToLand("ch").getArmee());
        JLabel einheiten_ch = new JLabel(einheitenStr_ch);
        einheiten_ch.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_ch.setForeground(Color.white);
        FontMetrics metrics_ch = einheiten_ch.getFontMetrics(einheiten_ch.getFont());
        int textBreite_ch = metrics_ch.stringWidth(einheitenStr_ch);
        einheiten_ch.setBounds((int)((1422.25 * this.ratio) + (durchmesser / 2.0) - (textBreite_ch / 2.0)), (int)(431.85 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_ch);

        g2d.setColor(japan);
        g2d.fillOval((int)(1620.55 * this.ratio), (int)(469.05 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(1620.55 * this.ratio), (int)(469.05 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_jp = Integer.toString(welt.idToLand("jp").getArmee());
        JLabel einheiten_jp = new JLabel(einheitenStr_jp);
        einheiten_jp.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_jp.setForeground(Color.white);
        FontMetrics metrics_jp = einheiten_jp.getFontMetrics(einheiten_jp.getFont());
        int textBreite_jp = metrics_jp.stringWidth(einheitenStr_jp);
        einheiten_jp.setBounds((int)((1620.55 * this.ratio) + (durchmesser / 2.0) - (textBreite_jp / 2.0)), (int)(469.05 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_jp);

        g2d.setColor(mongolei);
        g2d.fillOval((int)(1454.35 * this.ratio), (int)(348.35 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(1454.35 * this.ratio), (int)(348.35 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_mo = Integer.toString(welt.idToLand("mo").getArmee());
        JLabel einheiten_mo = new JLabel(einheitenStr_mo);
        einheiten_mo.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_mo.setForeground(Color.white);
        FontMetrics metrics_mo = einheiten_mo.getFontMetrics(einheiten_mo.getFont());
        int textBreite_mo = metrics_mo.stringWidth(einheitenStr_mo);
        einheiten_mo.setBounds((int)((1454.35 * this.ratio) + (durchmesser / 2.0) - (textBreite_mo / 2.0)), (int)(348.35 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_mo);

        g2d.setColor(irkutsk);
        g2d.fillOval((int)(1438.35 * this.ratio), (int)(277.75 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(1438.35 * this.ratio), (int)(277.75 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_ir = Integer.toString(welt.idToLand("ir").getArmee());
        JLabel einheiten_ir = new JLabel(einheitenStr_ir);
        einheiten_ir.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_ir.setForeground(Color.white);
        FontMetrics metrics_ir = einheiten_ir.getFontMetrics(einheiten_ir.getFont());
        int textBreite_ir = metrics_ir.stringWidth(einheitenStr_ir);
        einheiten_ir.setBounds((int)((1438.35 * this.ratio) + (durchmesser / 2.0) - (textBreite_ir / 2.0)), (int)(277.75 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_ir);

        g2d.setColor(jakutien);
        g2d.fillOval((int)(1406.45 * this.ratio), (int)(207.25 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(1406.45 * this.ratio), (int)(207.25 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_ya = Integer.toString(welt.idToLand("ya").getArmee());
        JLabel einheiten_ya = new JLabel(einheitenStr_ya);
        einheiten_ya.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_ya.setForeground(Color.white);
        FontMetrics metrics_ya = einheiten_ya.getFontMetrics(einheiten_ya.getFont());
        int textBreite_ya = metrics_ya.stringWidth(einheitenStr_ya);
        einheiten_ya.setBounds((int)((1406.45 * this.ratio) + (durchmesser / 2.0) - (textBreite_ya / 2.0)), (int)(207.25 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_ya);

        g2d.setColor(kamtschatka);
        g2d.fillOval((int)(1642.75 * this.ratio), (int)(245.85 * this.ratio), durchmesser, durchmesser);
        g2d.setColor(umrandung);
        g2d.setStroke(new BasicStroke(randBreite));
        g2d.drawOval((int)(1642.75 * this.ratio), (int)(245.85 * this.ratio), durchmesser, durchmesser);
        String einheitenStr_ka = Integer.toString(welt.idToLand("ka").getArmee());
        JLabel einheiten_ka = new JLabel(einheitenStr_ka);
        einheiten_ka.setFont(new Font("Arial", Font.BOLD, (int)(durchmesser * 0.6)));
        einheiten_ka.setForeground(Color.white);
        FontMetrics metrics_ka = einheiten_ka.getFontMetrics(einheiten_ka.getFont());
        int textBreite_ka = metrics_ka.stringWidth(einheitenStr_ka);
        einheiten_ka.setBounds((int)((1642.75 * this.ratio) + (durchmesser / 2.0) - (textBreite_ka / 2.0)), (int)(245.85 * this.ratio), durchmesser, durchmesser);
        this.add(einheiten_ka);
    }
}
