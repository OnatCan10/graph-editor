package de.unibremen.swp.model;

import de.unibremen.swp.FPoint;

import java.awt.*;
import java.awt.geom.Line2D;
import java.io.Serializable;

/**
 * Ein Edge zwischen zwei Knoten (Node's), auch Kanten genannt
 * @author Onat Can Vardareri, Mert Sendur
 */
public class Edge implements Serializable {

    //starknoten einer kante
    public Node startKnoten;
    //endknoten einer kante
    public Node endKnoten;
    //farbe einer kante
    private Color color;
    //relationstype einer kante
    private Relationstyp relationstyp;
    //wurde die kante ausgewählt
    private boolean selected = false;


    public Edge(Node startKnoten, Node endKnoten) {
        this.startKnoten = startKnoten;
        this.endKnoten = endKnoten;
        this.color = Color.BLUE;
        Relationstyp relationstyp = this.relationstyp;
    }

    /**
     * Prüft ob, Rechtangle sich schneidet.
     * @param r Das Rechtangle,welches geprüft wird.
     * @return True wenn es schneidet.False wenn nicht.
     */
    public boolean intersects(Rectangle r) {
        FPoint dir = new FPoint(endKnoten.transform.x - startKnoten.transform.x, endKnoten.transform.y - startKnoten.transform.y);
        dir = FPoint.normalize(dir);

        float tmin = (r.x - startKnoten.transform.x) / dir.x;
        float tmax = (r.x + r.width - startKnoten.transform.x) / dir.x;

        if (tmin > tmax) {
            float temp = tmax;
            tmax = tmin;
            tmin = temp;
        }


        float tymin = (r.y - startKnoten.transform.y) / dir.y;
        float tymax = (r.y + r.height - startKnoten.transform.y) / dir.y;

        if (tymin > tymax) {
            float temp = tymax;
            tymax = tymin;
            tymin = temp;
        }

        if ((tmin > tymax) || (tymin > tmax)) {
            return false;
        }

        return true;
    }

    /**
     * Errechnet den Punkt,an dem die Boundry intersected wird. Der Abstand lässt sich mit Radius setzen.
     * @param radius Radius der Abstand zur Schneidung.
     * @return Der gewünschte Punkt.
     */
    public FPoint getIntersection(float radius) {
        Rectangle r = endKnoten.getBounds();
        r.x -= radius;
        r.y -= radius;
        r.height += radius*2;
        r.width += radius*2;
        FPoint dir = new FPoint(endKnoten.transform.x - startKnoten.transform.x, endKnoten.transform.y - startKnoten.transform.y);
        dir = FPoint.normalize(dir);
        boolean ydominates = Math.abs(dir.y) > Math.abs(dir.x);
        float rad = ydominates ? r.height * 0.5f : r.width * 0.5f;
        float l = rad / Math.max(Math.abs(dir.y), Math.abs(dir.x));
        FPoint res = new FPoint(endKnoten.transform.x - dir.x * l, endKnoten.transform.y - dir.y * l);
        return res;
    }

    /**
     * Return true if this node is selected.
     */
    public boolean isSelected() {
        return selected;
    }


    /**
     * Mark this node as selected.
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Boundrys eines Rectangles
     * @return Gibt den Boundry zurück
     */
    public Rectangle getBounds() {
        float xMin = Math.min(startKnoten.transform.x, endKnoten.transform.x);
        float xMax = Math.max(startKnoten.transform.x, endKnoten.transform.x);
        float yMin = Math.min(startKnoten.transform.y, endKnoten.transform.y);
        float yMax = Math.max(startKnoten.transform.y, endKnoten.transform.y);

        Rectangle r = new Rectangle();
        r.x = (int) xMin;
        r.y = (int) yMin;
        r.width = (int) xMax - (int) xMin;
        r.height = (int) yMax - (int) yMin;
        return r;
    }

    /**
     * setter für kante relationstyp
     * @param relationstyp neu relationstyp
     */
    public void setRelationstyp(Relationstyp relationstyp) {
        this.relationstyp = relationstyp;
    }

    /**
     * setter für kante farbe
     * @param color neue farbe
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Die draw methode zum zeichen einer Kante.
     * @param g Das Graphics Objekt auf das gezeichnet wird.
     */
    public void draw(Graphics g) {
        Point p1 = new Point((int) startKnoten.transform.x, (int) startKnoten.transform.y);
        Point p2 = new Point((int) endKnoten.transform.x, (int) endKnoten.transform.y);
        FPoint end = getIntersection(0);
        FPoint end2 = getIntersection(10);
        FPoint end3 = getIntersection(20);


        if (this.relationstyp == Relationstyp.Verstärkend) {
            g.setColor(this.color);
            g.drawLine(p1.x, p1.y, (int) end.x, (int) end.y);
            drawArrowHead((Graphics2D) g, end, p1, g.getColor());

        } else if (this.relationstyp == Relationstyp.Abschwächend) {
            g.setColor(this.color);
            g.drawLine(p1.x, p1.y, (int) end.x, (int) end.y);
            g.fillOval((int) end2.x-10, (int) end2.y-10, 20, 20);

        } else {
            g.setColor(this.color);
            g.drawLine(p1.x, p1.y, (int) end3.x, (int) end3.y);
            g.drawString("?", (int) end3.x-5, (int) end3.y+10);

        }

        if (selected) {
            g.setColor(Color.DARK_GRAY);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            g.drawString("Ausgewählt", (int) ((startKnoten.transform.x + endKnoten.transform.x) / 2), (int) ((startKnoten.transform.y + endKnoten.transform.y) / 2));
        }
    }

    /**
     * https://coderanch.com/t/340443/java/Draw-arrow-head-line
     * Methode für verstränke relationstype um ein arrowhead zeichen zu können
     * @param g2 Das Graphics Objekt auf das gezeichnet wird.
     * @param tip endpunkt
     * @param tail startpunkt
     * @param color farbe des arrowheads
     */
    private void drawArrowHead(Graphics2D g2, FPoint tip, Point tail, Color color) {
        g2.setPaint(color);
        double phi = Math.toRadians(40);
        int barb = 20;
        double dy = tip.y - tail.y;
        double dx = tip.x - tail.x;
        double theta = Math.atan2(dy, dx);
        double x, y, rho = theta + phi;
        for (int j = 0; j < 2; j++) {
            x = tip.x - barb * Math.cos(rho);
            y = tip.y - barb * Math.sin(rho);
            g2.draw(new Line2D.Double(tip.x, tip.y, x, y));
            rho = theta - phi;
        }
    }
}
