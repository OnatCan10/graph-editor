package de.unibremen.swp;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * Klasse für Farbe bearbeiten icon.
 * @author John B. Matthews
 */
public class ColorIcon implements Icon , Serializable {

    //widgt der icon
    private static final int WIDE = 20;
    //height der icon
    private static final int HIGH = 20;
    //farbe der icon
    private Color color;

    public ColorIcon(Color color) {
        this.color = color;
    }

    /**
     * getter für icon farbe
     * @return icon farbe
     */
    public Color getColor() {
        return color;
    }

    /**
     * setter für icon farbe
     * @param color neue farbe
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Icon zeichen
     * @param c component
     * @param g Die Graphics, worauf des gezeichnet werden soll
     * @param x x koordinate
     * @param y y koordinate
     */
    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(color);
        g.fillRect(x, y, WIDE, HIGH);
    }

    /**
     * getter für icon width
     * @return icon width
     */
    public int getIconWidth() {
        return WIDE;
    }
    /**
     * getter für icon Height
     * @return icon Height
     */
    public int getIconHeight() {
        return HIGH;
    }
}