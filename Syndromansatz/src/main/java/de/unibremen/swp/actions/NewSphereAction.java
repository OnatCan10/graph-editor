package de.unibremen.swp.actions;

import de.unibremen.swp.FPoint;
import de.unibremen.swp.GraphPanel;
import de.unibremen.swp.model.Sphere;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Action Klasse für Sphären anlegen
 * @author Onat Can Vardareri
 */
public class NewSphereAction extends AbstractAction {
    private GraphPanel parent;

    public NewSphereAction(String name, GraphPanel parent) {
        super(name);
        this.parent = parent;
    }

    /**
     * Sobald die action ausgeführt wird, wird die methode aus der AbstractAction Klasse ausgeführt.
     * @param e Ausgeführte action
     */
    public void actionPerformed(ActionEvent e) {
        final String sphereename = JOptionPane.showInputDialog(null, "Name der Sphäre", "Sphäre Namen", JOptionPane.QUESTION_MESSAGE);
        if (sphereename != null && !sphereename.isBlank()) {
            parent.sphereAnlegen(sphereename);
        } else {
            JOptionPane.showMessageDialog(null, "Der Namen ist leer", "Warnung", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}