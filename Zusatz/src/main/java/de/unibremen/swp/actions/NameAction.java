package de.unibremen.swp.actions;

import de.unibremen.swp.GraphPanel;
import javax.swing.*;
import java.awt.event.ActionEvent;
/**
 * Action Klasse für Graph Element Namen bearbeiten.
 * @author Onat Can Vardareri
 */
public class NameAction extends AbstractAction {
    private GraphPanel parent;

    public NameAction(String name, GraphPanel parent) {
        super(name);
        this.parent = parent;
    }

    private JTextArea graphInfo;
    /**
     * Sobald die action ausgeführt wird, wird die methode aus der AbstractAction Klasse ausgeführt.
     * @param e Ausgeführte action
     */
    public void actionPerformed(ActionEvent e) {
        final String name = JOptionPane.showInputDialog(graphInfo, "Neue name", "Neue name", JOptionPane.QUESTION_MESSAGE);
        if (name != null) {
            parent.namenAendern(name);
        } else {
            JOptionPane.showMessageDialog(null, "Ungültigen Relationstypen ausgewählt", "Warnung", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}