package de.unibremen.swp.actions;

import de.unibremen.swp.GraphPanel;
import de.unibremen.swp.model.Edge;
import de.unibremen.swp.model.Relationstyp;

import javax.swing.*;
import java.awt.event.ActionEvent;
/**
 * Action Klasse für RelationstypBearbeitenAction.
 * @author Onat Can Vardareri
 */
public class RelationstypBearbeitenAction extends AbstractAction {

    private GraphPanel parent;

    public RelationstypBearbeitenAction(String name, GraphPanel parent) {
        super(name);
        this.parent = parent;
    }
    /**
     * Sobald die action ausgeführt wird, wird die methode aus der AbstractAction Klasse ausgeführt.
     * @param e Ausgeführte action
     */
    public void actionPerformed(ActionEvent e) {
        Relationstyp selection = (Relationstyp) JOptionPane.showInputDialog(null, "Wähle den neuen Relationstypen aus", "Neuer Relationstyp", JOptionPane.INFORMATION_MESSAGE, null, Relationstyp.values(), Relationstyp.values()[0]);
        if (selection!=null) {
            parent.relationstypBearbeiten(selection);
        } else {
            JOptionPane.showMessageDialog(null, "Ungültigen Relationstypen ausgewählt", "Warnung", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
