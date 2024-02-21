package de.unibremen.swp.actions;

import de.unibremen.swp.GraphPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class PdfAction extends AbstractAction {
    private GraphPanel parent;

    public PdfAction(String name, GraphPanel parent) {
        super(name);
        this.parent = parent;
    }
    /**
     * Sobald die action ausgeführt wird, wird die methode aus der AbstractAction Klasse ausgeführt.
     * @param e Ausgeführte action
     */
    public void actionPerformed(ActionEvent e) {
        parent.pdfExport();
    }

}

