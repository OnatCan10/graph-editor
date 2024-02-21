package de.unibremen.swp.actions;

import de.unibremen.swp.GraphPanel;
import de.unibremen.swp.model.Edge;
import de.unibremen.swp.model.GraphElement;
import de.unibremen.swp.model.Node;
import de.unibremen.swp.model.Sphere;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.ListIterator;
/**
 * Action Klasse für Graph Elemente löschen.
 * @author Onat Can Vardareri
 */
public class DeleteAction extends AbstractAction {
    private GraphPanel parent;

    public DeleteAction(String name, GraphPanel parent) {
        super(name);
        this.parent = parent;
    }
    /**
     * Sobald die action ausgeführt wird, wird die methode aus der AbstractAction Klasse ausgeführt.
     * @param e Ausgeführte action
     */
    public void actionPerformed(ActionEvent e) {
        parent.ausgewaehlteGraphElementeLoeschen();
    }


}