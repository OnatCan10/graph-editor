package de.unibremen.swp.model;


import de.unibremen.swp.FPoint;

import java.awt.*;
import java.io.Serializable;
import java.util.List;


/**
 * Die Klasse zum erstellen von Sphären
 * @author Onat Can Vardareri, Mert Sendur
 */
public class Sphere extends GraphElement implements Serializable {

    //farbe der sphäre
    private Color color;
    //name der sphäre
    private String name;

    /**
     * Neue Sphäre erstellen
     */
    public Sphere(FPoint p, Point r, Color color,String name, int depth) {
    	super(p, r, null, depth, true, false, false);
        this.color = color;
        this.name = name;
    }

    /**
     * setter für sphären farbe
     * @param color neue farbe
     */
    public void setColor(Color color) {
        this.color = color;
    }
    /**
     * setter für sphären name
     * @param name neue name
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * getter für sphäre farbe
     * @return farbe der sphäre
     */
    public Color getColor() {
        return color;
    }
    /**
     * getter für sphäre name
     * @return name der sphäre
     */
    public String getName() {
        return name;
    }


    /**
     * Sphäre Zeichnen
     * @param g Die Grpahics, worauf des gezeichnet werden soll
     */
    public void drawThis(Graphics g) {
        g.setColor(this.color);
        Rectangle b = getBounds();
        FontMetrics metrics = g.getFontMetrics(new Font("TimesRoman", Font.PLAIN, b.width/5));
        int q = b.x + (b.width - metrics.stringWidth(name)) / 2;
        g.fillRect(b.x, b.y, b.width, b.height);
        g.setColor(Color.BLACK);
        g.setFont(new Font("TimesRoman", Font.PLAIN, b.width/5));
        g.drawString(name, q, b.y+b.height/8);
        g.setColor(Color.darkGray);
        g.drawRect(b.x, b.y, b.width, b.height);
        if (selected) {
            g.setFont(new Font("TimesRoman", Font.PLAIN, b.width/5));
            g.drawString(name, q, b.y+b.height/8);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            g.drawString("Ausgewählt",b.x, b.y);
        }
    }

    /**
     * Zum Aktuallisieren des Radiuses
     * @param list Liste zum aktuallisieren der Sphären
     * @param radius Um diesen Radius aktuallisieren
     */
    public static void updateRadius(List<Sphere> list, Point radius) {
        for (Sphere s : list) {
            if (s.isSelected()) {
                s.radius = radius;
            }
        }
    }

    /**
     * Die Farbe der Knoten aktuallisieren
     * @param list Liste zum aktuallisieren der Sphären
     * @param color Die Farbe zum aktuallisieren
     */
    public static void updateColor(List<Sphere> list, Color color) {
        for (Sphere s : list) {
            if (s.isSelected()) {
                s.color = color;
            }
        }
    }
    /**
     * Die Farbe der Sphäre aktuallisieren
     * @param list Liste zum aktuallisieren der Sphären
     * @param name Der Name zum aktuallisieren
     */
    public static void updateName(List<Sphere> list, String name) {
        for (Sphere s : list) {
            if (s.isSelected()) {
                s.name = name;
            }
        }
    }






}