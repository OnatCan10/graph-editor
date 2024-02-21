package de.unibremen.swp.model;

import de.unibremen.swp.FPoint;

import java.awt.*;
import java.io.Serializable;
import java.util.List;

/**
 * Repräsentiert einen Node (Knoten) auf dem Graphen
 * @author Onat Can Vardareri, Mert Sendur
 */
public class Node extends GraphElement implements Serializable{

    //farbe des nodes
    private Color color;
    //name des nodes
    private String name;

    /**
     * Neuen Node erstellen.
     */
    public Node(FPoint p, Point r, Color color, Sphere parentSphere, String name, int depth) {
    	super(p, r, parentSphere, depth, true, true, true);
        this.color = color;
        this.name = name;
        this.containTolerance = 20;
    }

    /**
     * getter für node farbe
     * @return knoten farbe
     */
    public Color getColor() {
        return color;
    }

    /**
     * setter für node farbe
     * @param color neue farbe
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * getter für node name
     * @return node name
     */
    public String getName() {
        return name;
    }

    /**
     * setter für node name
     * @param name neue name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Knoten Zeichnen
     * @param g Die Graphics, worauf des gezeichnet werden soll
     */
    public void drawThis(Graphics g) {
        g.setColor(this.color);
        Rectangle b = getBounds();
        FontMetrics metrics = g.getFontMetrics(new Font("TimesRoman", Font.PLAIN, b.width/5));
        int q = b.x + (b.width - metrics.stringWidth(name)) / 2;
        g.fillRoundRect(b.x, b.y, b.width, b.height, radius.x, radius.y);
        g.setColor(Color.BLACK);
        g.setFont(new Font("TimesRoman", Font.PLAIN, b.width/5));
        g.drawString(name, q, b.y+ b.height/2);
        g.setColor(Color.darkGray);
        g.drawRoundRect(b.x, b.y, b.width, b.height, radius.x, radius.y);
        if (selected) {
            g.setFont(new Font("TimesRoman", Font.PLAIN, b.width/5));
            g.drawString(name, q, b.y+ b.height/2);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
            g.drawString("Ausgewählt",b.x, b.y);
        }
    }


    /**
     * Zum Aktuallisieren des Radiuses
     * @param list Liste zum aktuallisieren der Knoten
     * @param radius Um diesen Radius aktuallisieren
     */
    public static void updateRadius(List<Node> list, Point radius) {
        for (Node n : list) {
            if (n.isSelected()) {
                n.radius = radius;
            }
        }
    }

    /**
     * Die Farbe der Knoten aktuallisieren
     * @param list Liste zum aktuallisieren der Knoten
     * @param color Die Farbe zum aktuallisieren
     */
    public static void updateColor(List<Node> list, Color color) {
        for (Node n : list) {
            if (n.isSelected()) {
                n.color = color;
            }
        }
    }

    /**
     * Der Name der Knoten aktuallisieren
     * @param list Liste zum aktuallisieren der Knoten
     * @param name Der Name zum aktuallisieren
     */
    public static void updateName(List<Node> list, String name) {
        for (Node n : list) {
            if (n.isSelected()) {
                n.name = name;
            }
        }
    }
}