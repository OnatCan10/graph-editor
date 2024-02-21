package de.unibremen.swp.actions;

import de.unibremen.swp.GraphPanel;
import de.unibremen.swp.model.Edge;
import de.unibremen.swp.model.Node;
import de.unibremen.swp.model.Sphere;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
/**
 * Action Klasse für Farbe bearbeiten.
 * @author Onat Can Vardareri
 */
public class ColorAction extends AbstractAction {

    private GraphPanel parent;
    private GraphPanel.ControlPanel control;

    public ColorAction(String name, GraphPanel parent, GraphPanel.ControlPanel controlPanel) {
        super(name);
        this.parent = parent;
        control = controlPanel;
    }
    /**
     * Sobald die action ausgeführt wird, wird die methode aus der AbstractAction Klasse ausgeführt.
     * @param e Ausgeführte action
     */
    public void actionPerformed(ActionEvent e) {
        Color color = control.hueIcon.getColor();
        color = JColorChooser.showDialog(parent, "Choose a color", color);
        if (color != null) {
            parent.farbeAendern(color);
        } else {
            JOptionPane.showMessageDialog(null, "Ungültige Farbe ausgewählt", "Warnung", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}