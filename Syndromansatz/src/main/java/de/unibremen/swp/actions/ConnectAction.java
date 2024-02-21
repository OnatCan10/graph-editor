package de.unibremen.swp.actions;

import de.unibremen.swp.GraphPanel;
import de.unibremen.swp.model.Edge;
import de.unibremen.swp.model.GraphElement;
import de.unibremen.swp.model.Node;
import de.unibremen.swp.model.Relationstyp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;



/**
 * Action Klasse für Kante anlegen.
 * @author Onat Can Vardareri
 */
public class ConnectAction extends AbstractAction {

    private Relationstyp relationstyp;
    private GraphPanel parent;


    public ConnectAction(Relationstyp rtyp, GraphPanel parent) {
        super(rtyp.toString());
        this.relationstyp = rtyp;
        this.parent = parent;
    }
    /**
     * Sobald die action ausgeführt wird, wird die methode aus der AbstractAction Klasse ausgeführt.
     * @param e Ausgeführte action
     */
    public void actionPerformed(ActionEvent e) {
        parent.edgesverbinden(relationstyp);
    }
}