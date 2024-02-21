package de.unibremen.swp.actions;

import de.unibremen.swp.FPoint;
import de.unibremen.swp.GraphPanel;
import de.unibremen.swp.model.Node;
import de.unibremen.swp.model.Sphere;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ListIterator;
/**
 * Action Klasse für Symptom Anlegen.
 * @author Onat Can Vardareri
 */
public class NewNodeAction extends AbstractAction {
    private GraphPanel parent;

    public NewNodeAction(String name,  GraphPanel parent) {
        super(name);
        this.parent = parent;
    }

    private JTextArea graphInfo;
    /**
     * Sobald die action ausgeführt wird, wird die methode aus der AbstractAction Klasse ausgeführt.
     * @param e Ausgeführte action
     */
    public void actionPerformed(ActionEvent e) {
        for (Sphere s : parent.getSpheres())
            if(s.isSelected()){
            final String knotenName = JOptionPane.showInputDialog(graphInfo, "Name des Symptoms", "Symptom Namen", JOptionPane.QUESTION_MESSAGE);
            if (knotenName != null && !knotenName.isBlank()) {
                parent.knotenAnlegen(knotenName, s);
            } else {
                JOptionPane.showMessageDialog(null, "Der Name ist leer", "Warnung", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
