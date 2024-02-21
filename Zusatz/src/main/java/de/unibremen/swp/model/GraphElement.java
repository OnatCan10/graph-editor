package de.unibremen.swp.model;

import de.unibremen.swp.FPoint;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Graph Elemente , auch Sphäre oder Symptom genannt.
 * @author Onat Can Vardareri, Mert Sendur
 */
public class GraphElement implements Serializable   {


    public  FPoint transform;
    //Das sollte nicht als Point interpretiert werden sondern als ein 2-Tupel (Beinhaltet 1/2 width und 1/2 height vom Element)
    public  Point radius;
    public  GraphElement parent = null;
    //Aufsteigen sortiert nach der depth
    // depth ist nur für die Parentreferenz relevant und alle children werden auf dem Parent gezeichnet (selbst wen der Parent eine niedrigere depth hat)
    public  ArrayList<GraphElement> children;
    // Andere children (mit selben Parent) mit höherer depth werden hinter diesem Element gezeichnet
    public  int depth = 0;
    //Ob dieses Element die klick verschluckt, also dass kein anderes Element daunter gehittet(angeklickt) wird
    public boolean  consume = false;
    //Ob dieses Eleement an den potentiellen Parent attached wird und sich damit bewegt
    public boolean  attached = true;
    //Ob dieses Element zu den Bounds zum Parent angepasst wird (Toleranz mit "containTolerance")
    public boolean contained = false;
    //Die Anzahl an Pixel die dieses Element außerhalb der Parents Bounds bewegt werden kann (Nur wenn "contained" == true ist)
    public  int containTolerance = 20;
    // Ob dieses Element aktuell ausgewählt ist
    protected boolean selected = false;
    // Welche Edge zu erst selected wurde
    public long selectedTime;


    public GraphElement(FPoint transform, Point radius, GraphElement parent, int depth, boolean consume, boolean attached, boolean contained) {
        this.transform = transform;
        this.radius = radius;
        this.parent = parent;
        this.depth = depth;
        this.consume = consume;
        this.attached = attached;
        this.contained = contained;
        children = new ArrayList<GraphElement>();
    }

    /**
     * Gibt den parent zurück.
     * @return parent
     */
    public GraphElement getParent() {
        return parent;
    }

    /**
     * Gibt die childeren zurück.
     * @return eine liste von childeren
     */
    public ArrayList<GraphElement> getChildren() {
        return children;
    }



    /**
     * Testet ob der Point p (Pixel Coords) in dem Elements Bounds oder der Children liegt
     * @param p Der Punkt zum Prüfen
     * @param hitOne Ob der return sobald das erste Element gefunden wurde (true) oder ob es weiter an die Children propagiert wird nach einem "hit" (false)
     * @return Die Liste der geklicken GraphElements
     */
    public HitResult hitTest(FPoint p, boolean hitOne) {
        HitResult result = new HitResult();

        Rectangle bounds = getBounds();
        boolean hit = p.x >= bounds.x && p.x <= bounds.x + bounds.width && p.y >= bounds.y && p.y <= bounds.y + bounds.height;

        if (children != null) {

            // Erstmal an die Children propagieren, weil die Children immer vor den Parents sind
            for (GraphElement c : children) {

                //Erwarted, dass Childrent als erstes Sortiert ist, weil es im Vordergrund ist und sollte zu erst getestet werden (Nur, wenn consume == true)
                HitResult childResult = c.hitTest(p, hitOne);


                // Nur wenn wir einen einziegen Hit wollen oder wenn das Child den hit consumed - wird sofort return't ohne weitere Modifikationen
                if ((hitOne || childResult.consumed) && childResult.elementsHit.size() > 0) {
                    return childResult;
                }

                // Wenn die Hits nicht consumed werden, alle hits aneinander binden und weiter machen
                result.elementsHit.addAll(childResult.elementsHit);
            }
        }

        if (hit) {
            result.elementsHit.add(this);
            result.consumed = consume;
        }

        return result;
    }


    /**
     * Zum erhalten der Max. Depth von dem Children dieses Elementes
     * @return Maximale depth von dem Children dieses Elementes
     */
    public int getMaxDepth() {
        if (children == null || children.size() == 0) {
            return -1;
        }

        // Children wirld aufsteigend nach der depth sortiert, als dass das letzte Element die höchste depth hat
        return children.get(children.size() - 1).depth;
    }


    /**
     * Fügt einen Child hinzu mit manueller depth
     * @param c der Parent, an dem ein Child hinzugefügt werden soll
     */
    public void addChild(GraphElement c) {
        if (c == null) {
            new NullPointerException("Tried to add null element as child!");
        }

        c.parent = this;

        if (children == null) {
            children = new ArrayList<GraphElement>();
            children.add(c);
            return;
        }

        int i = 0;
        for (GraphElement ch : children) {

            //stop und fügt child vor "ch" (weil es eine geringere depth als "ch" hat)
            if (c.depth <= ch.depth) {
                break;
            }
            i++;
        }
        children.add(i, c);
    }


    /**
     * Returnt die Bounds des Elementes
     * @return Returnt die Bounds des Elementes
     */
    public Rectangle getBounds() {
        Rectangle result = new Rectangle();
        result.x = (int) transform.x - radius.x;
        result.width = radius.x * 2;
        result.y = (int) transform.y - radius.y;
        result.height = radius.y * 2;

        return result;
    }


    /**
     * Virtuelle Methode, welche von den Children-Klassen überschrieben wird
     * draw-Logik für das Element
     * @param g Die Grpahics, worauf des gezeichnet werden soll
     */
    public void drawThis(Graphics g) {

    }


    /**
     * Zum zeichnen dieses Elementes und wenn "propagate" == true, auch an dessen Children
     * @param g Die Grpahics, worauf des gezeichnet werden soll
     * @param propagate wenn "propagate" == true, auch an dessen Children zeichnen.
     */
    public void draw(Graphics g, boolean propagate) {
        drawThis(g);
        if (children == null || !propagate) {
            return;
        }
        // Zeichne von hinten nach vorne
        for (int i = children.size() - 1; i >= 0; i--) {
            children.get(i).draw(g, propagate);
        }
    }

     /**
     * draw only children, needed so we can draw edges before children
     * Die draw methode zum zeichen einer Kante.
     * @param g Das Graphics Objekt auf das gezeichnet wird.
     */
    public void drawChildren(Graphics g) {
        if (children == null) {
            return;
        }
        // draw back to front
        for (int i = children.size() - 1; i >= 0; i--) {
            children.get(i).draw(g, true);
        }
    }

    /**
     * Bewegt dieses Element und alle Children um delta.x und delta.y
     * @param sDelta das Delta als FPoint
     * @param collisionlist Die Kollisionsliste, damit es zu keiner Kollision kommt.
     */
    public void deltaTransform(FPoint sDelta, ArrayList<GraphElement> collisionlist) {
        if (children != null) {
            for (GraphElement c : children) {
                //if(c.attached || c.selected) {
                c.deltaTransform(sDelta, null);
                //}
            }
        }

        if (this.selected || (parent != null && parent.isSelected() && this.attached)) {
            this.transform.x += sDelta.x;
            this.transform.y += sDelta.y;

            // Die Bewegung des Elements einschränken, damit es nur in der parent Bounds sich bewegt
            if (parent != null && contained) {
                Rectangle pb = parent.getBounds();

                //Clamping
                //Benötigt die Parent Bounds (erweitert mit "containTolerance") um das Child einzuschränken
                this.transform.x = Math.max(pb.x - containTolerance + radius.x, Math.min(pb.x + pb.width + containTolerance - radius.x, this.transform.x));
                this.transform.y = Math.max(pb.y - containTolerance + radius.y, Math.min(pb.y + pb.height + containTolerance - radius.y, this.transform.y));
            }
            if (collisionlist != null) {
                for (GraphElement col : collisionlist) {
                    if (col.getBounds().intersects(this.getBounds())) {


                        FPoint delta = FPoint.subtract(this.transform, col.transform);
                        FPoint dir = FPoint.normalize(delta);
                        // collision auf x
                        if(Math.abs(dir.x) > Math.abs(dir.y)) {
                            transform.x += delta.x * 0.02;
                        }
                        //oder auf y
                        else {
                            transform.y += delta.y * 0.02;
                        }
                    }
                }
            }
        }
    }



    /**
     * Wähle dieses Element.
     * Logik sollte von den Child Klassen implementiert werden
     */
    public void select() {
        selectedTime = System.nanoTime();
        setSelected(true);
    }

    /**
     * Return true wenn dieses Element selected ist
     */
    public boolean isSelected() {
        return selected;
    }


    /**
     * Dieses Element als selected setzen
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    /**
     * Select umschalten (toggle)
     */
    public void toggleSelect() {
        this.selected = !this.selected;
    }


    /**
     * Unselected dieses Element und wenn "propagate" == true gesetzt wurde, wird es ebenso an die Childs weitergeleitet
     * @param propagate "propagate" == true gesetzt wurde, wird es ebenso an die Childs weitergeleitet
     */
    public void Unselect(boolean propagate) {
        this.selected = false;
        if (propagate && children != null) {
            for (GraphElement g : children) {
                g.Unselect(propagate);
            }
        }
    }


    /**
     * Kein Element auswählen
     */
    public static void selectNone(java.util.List<GraphElement> list) {
        for (GraphElement n : list) {
            n.setSelected(false);
        }
    }

    /**
     * Gibt alle ausgewählten Elemente als GraphElement-Liste (ArrayList) zurück.
     * @return Gibt alle ausgewählten Elemente als GraphElement-Liste (ArrayList) zurück.
     */
    public ArrayList<GraphElement> getAllSelected() {
        ArrayList<GraphElement> result = new ArrayList<GraphElement>();
        if (children != null) {
            for (GraphElement c : children) {
                ArrayList<GraphElement> cr = c.getAllSelected();
                result.addAll(cr);
            }
        }
        if (this.selected) {
            result.add(this);
        }

        return result;
    }

}
