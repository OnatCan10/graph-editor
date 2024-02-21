package de.unibremen.swp;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import de.unibremen.swp.actions.*;
import de.unibremen.swp.model.*;


import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 * Basierend auf John B. Matthews Beispiel : https://sites.google.com/site/drjohnbmatthews/graphpanel
 * @author John B. Matthews, Onat Can Vardareri
 */
public class GraphPanel extends JComponent {

    //Höhe und Breite des graphs
    private static final int WIDE = 800;
    private static final int HIGH = 600;
    private static final int RADIUS = 30;
    private static final Random rnd = new Random();
    public ControlPanel control = new ControlPanel();
    private Point radius = new Point(RADIUS, RADIUS);
    //sphären des graphs
    private List<Sphere> spheres = new ArrayList<Sphere>();
    //nodes des graphs
    private List<Node> nodes = new ArrayList<Node>();
    //edges des graphs
    private List<Edge> edges = new ArrayList<Edge>();
    //ausgewählte edges
    private final List<Edge> selectedEdges = new ArrayList<Edge>();


    private Point mousePt = new Point(WIDE / 2, HIGH / 2);
    private Rectangle mouseRect = new Rectangle();
    private boolean mouseSelect = false;
    //object um das graph zu können
    private SaverLoader graph = new SaverLoader(nodes, edges, spheres);

    static JFrame f;


    public static void main(String[] args) throws Exception {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                f = new JFrame("Syndromansatz - Projekt: unbennant");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                GraphPanel gp = new GraphPanel();
                f.add(gp.control, BorderLayout.NORTH);
                f.add(new JScrollPane(gp), BorderLayout.CENTER);
                f.getRootPane().setDefaultButton(gp.control.nodeButton);
                f.getRootPane().setDefaultButton(gp.control.sphereButton);
                f.pack();
                f.setLocationRelativeTo(null);
                f.setVisible(true);
            }
        });
    }

    public GraphPanel() {
        this.setOpaque(true);
        this.addMouseListener(new MouseHandler());
        this.addMouseMotionListener(new MouseMotionHandler());
    }

    /**
     * getter für liste von sphären
     * @return liste von sphären
     */
    public List<Sphere> getSpheres() {
        return spheres;
    }
    /**
     * getter für liste von nodes
     * @return liste von nodes
     */
    public List<Node> getNodes() {
        return nodes;
    }
    /**
     * getter für liste von edges
     * @return liste von edges
     */
    public List<Edge> getEdges() {
        return edges;
    }

    /**
     * getter für mouse position
     * @return mouse position
     */
    public FPoint getMousePosF() {
        return new FPoint((float) mousePt.getLocation().x, (float) mousePt.getLocation().y);
    }

    /**
     * getter für gewünschte größe
     * @return gewünschte größe
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDE, HIGH);
    }

    /**
     * Componente painten.
     * @param g Die Graphics, worauf des gezeichnet werden soll
     */
    @Override
    public void paintComponent(Graphics g) {
        g.setColor(new Color(0x00f0f0f0));
        g.fillRect(0, 0, getWidth(), getHeight());
        //Reihenfolge für die Überlappung der Graphics. Sphäre -> Node -> Edge
        for (Sphere s : spheres) {
            s.draw(g, false);
        }

        for (Edge e : edges) {
            e.draw(g);
        }

        for (Sphere s : spheres) {
            s.drawChildren(g);
        }


        if (mouseSelect) {
            g.setColor(Color.darkGray);
            g.drawRect(mouseRect.x, mouseRect.y, mouseRect.width, mouseRect.height);
            g.setColor(Color.BLACK);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            g.drawString("Bitte Kanten auswählen !", mouseRect.x, mouseRect.y);
        }
    }

    /**
     * getter für ausgewählte graph elemente
     * @return ausgewählte graph elemente
     */
    public ArrayList<GraphElement> GetAllSelected() {
        ArrayList<GraphElement> result = new ArrayList<GraphElement>();
        for (Sphere s : spheres) {
            ArrayList<GraphElement> sr = s.getAllSelected();
            result.addAll(sr);
        }

        return result;
    }

    /**
     * Alle graph elemente nicht mehr auswählten
     */
    public void UnselectAll() {
        for (Sphere s : spheres) {
            s.Unselect(true);
        }
    }

    /**
     * Mouse Handler klasse um mouse actions festzulegen.
     */
    private class MouseHandler extends MouseAdapter {

        /**
         * was beim mouse release ausgeführt werden soll.
         * @param e mouseevent
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            mouseSelect = false;
            mouseRect.setBounds(0, 0, 0, 0);


            if (e.isPopupTrigger()) {
                showPopup(e);
            }

            e.getComponent().repaint();
        }
        /**
         * was beim mouse press ausgeführt werden soll.
         * @param e mouseevent
         */
        @Override
        public void mousePressed(MouseEvent e) {
            unselectAndClearAllEdges();
            mousePt = e.getPoint();
            HitResult hr = new HitResult();
            mouseSelect = true;
            for (Sphere s : spheres) {
                HitResult shr = s.hitTest(new FPoint(mousePt), true);
                // wir wollen nur eine Auswahl
                if (shr.elementsHit.size() > 0) {
                    hr.elementsHit.addAll(shr.elementsHit);
                    hr.consumed = true;
                    mouseSelect = false;
                    break;
                }
            }
            // Auswahl passiert nur, wenn mit links geklickt wird
            if (SwingUtilities.isLeftMouseButton(e)) {
                // Toggle alle Element hits
                if (e.isShiftDown()) {
                    for (GraphElement g : hr.elementsHit) {
                        g.toggleSelect();
                        mouseSelect = false;
                    }
                } else {
                    // Alle unselecten und nur einen Auswählen, wenn möglich
                    UnselectAll();
                    for (GraphElement g : hr.elementsHit) {
                        g.select();
                        mouseSelect = false;
                    }
                }
            }

            e.getComponent().repaint();
        }

        /**
         * popup menu zeigen
         * @param e mouseevent
         */
        private void showPopup(MouseEvent e) {
            control.popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }
    /**
     * Mouse Motion Handler klasse um mouse actions festzulegen.
     */
    private class MouseMotionHandler extends MouseMotionAdapter {

        Point delta = new Point();

        /**
         * was beim mouse drag ausgeführt werden soll.
         * @param e mouseevent
         */
        @Override
        public void mouseDragged(MouseEvent e) {
            //e.consume();
            if (mouseSelect) {
                mouseRect.setBounds(Math.min(mousePt.x, e.getX()), Math.min(mousePt.y, e.getY()), Math.abs(mousePt.x - e.getX()), Math.abs(mousePt.y - e.getY()));
                for (Edge ed : edges) {
                    if (!ed.isSelected()) {
                        if (ed.intersects(mouseRect)) {
                            unselectAndClearAllEdges();
                            selectedEdges.add(ed);
                            ed.setSelected(true);
                        }
                    }
                }
            } else {
                delta.setLocation(e.getX() - mousePt.x, e.getY() - mousePt.y);
                mousePt = e.getPoint();
                for (Sphere s : spheres) {
                    ArrayList<GraphElement> colList = new ArrayList<GraphElement>();
                    colList.addAll(spheres);
                    colList.remove(s);
                    FPoint sDelta = new FPoint(delta);
                    s.deltaTransform(sDelta, colList);
                }
            }
            e.getComponent().repaint();
        }
    }

    /**
     * Alle kanten nicht auswählen und dann löschen.
     */
    private void unselectAndClearAllEdges() {
        for (Edge sEd : selectedEdges) {
            sEd.setSelected(false);
        }
        selectedEdges.clear();
    }

    /**
     * getter für control panel
     * @return control panel
     */
    public JToolBar getControlPanel() {
        return control;
    }

    /**
     * Control Panel Klasse für unser Program.
     * Hier wird festgelegt welche menu buttons geben soll und was beim klicken auf diese buttons ausgeführt werden soll.
     */
    public class ControlPanel extends JToolBar {
        private Action newSphere = new NewSphereAction("Neue Sphäre", GraphPanel.this);
        private Action newNode = new NewNodeAction("Neues Symptom", GraphPanel.this);
        private Action clearAll = new ClearAction("Alles löschen", GraphPanel.this);
        private Action color = new ColorAction("Farbe bearbeiten", GraphPanel.this, this);
        private Action delete = new DeleteAction("Löschen", GraphPanel.this);
        private Action name = new NameAction("Namen bearbeiten", GraphPanel.this);
        private Action relationstypBearbeitenAction = new RelationstypBearbeitenAction("Relationstyp bearbeiten", GraphPanel.this);
        private Action kanteLoeschenAction = new KanteLoeschenAction("Ausgewählte Kante löschen", GraphPanel.this);
        private Action speichern = new SaveAction("Speichern", GraphPanel.this);
        private Action laden = new LoadAction("Laden", GraphPanel.this);
        private Action exportpdf = new PdfAction("PDF Export", GraphPanel.this);
        private JButton nodeButton = new JButton(newNode);
        private JButton sphereButton = new JButton(newSphere);
        private JButton relationstypBearbeiten = new JButton(relationstypBearbeitenAction);
        private JButton kanteLöschen = new JButton(kanteLoeschenAction);
        private JButton saveButton = new JButton(speichern);
        private JButton loadButton = new JButton(laden);
        public ColorIcon hueIcon = new ColorIcon(Color.green);
        private JPopupMenu popup = new JPopupMenu();

        ControlPanel() {
            this.setLayout(new FlowLayout(FlowLayout.LEFT));
            this.setBackground(Color.lightGray);
            this.add(sphereButton);
            this.add(nodeButton);
            this.add(new JButton(clearAll));
            this.add(new JButton(color));
            this.add(new JLabel(hueIcon));
            this.add(relationstypBearbeiten);
            this.add(kanteLöschen);
            this.add(saveButton);
            this.add(loadButton);
            this.add(exportpdf);
            JSpinner js = new JSpinner();
            js.setModel(new SpinnerNumberModel(RADIUS, 20, 150, 5));
            js.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    JSpinner s = (JSpinner) e.getSource();
                    int sval = (Integer) s.getValue();
                    radius = new Point(sval, sval);
                    Node.updateRadius(nodes, radius);
                    Sphere.updateRadius(spheres, radius);
                    GraphPanel.this.repaint();
                }
            });
            this.add(new JLabel("Größe:"));
            this.add(js);
            popup.add(new JMenuItem(newNode));
            popup.add(new JMenuItem(color));
            popup.add(new JMenuItem(delete));
            popup.add(new JMenuItem(name));
            JMenu subMenu = new JMenu("Kante hinzufügen");
            for (Relationstyp rTyp : Relationstyp.values()) {
                subMenu.add(new JMenuItem(new ConnectAction(rTyp, GraphPanel.this)));
            }
            popup.add(subMenu);
        }
    }

    /**
     * https://github.com/michaltkacz/graph-editor
     * Methode um Graph zu speichern
     */
    public void serializeGraph() {
        if (graph == null)
            return;
        JFileChooser fc = new JFileChooser(".");
        fc.setMultiSelectionEnabled(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Binary files *.bin", "bin");
        fc.addChoosableFileFilter(filter);
        fc.setFileFilter(filter);
        int choosenOption = fc.showSaveDialog(this);
        if (choosenOption == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fc.getSelectedFile();
            String fileName = selectedFile.getAbsolutePath();
            if (!fileName.endsWith(".bin")) {
                selectedFile = new File(fileName + ".bin");
            }
            try {
                SaverLoader.serializeGraph(selectedFile, graph);
                JOptionPane.showMessageDialog(null, "Saved to file " + selectedFile.getAbsolutePath());
                f.setTitle("Syndromansatz - Projekt: " + selectedFile.getName());
            } catch (SaverLoadException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * https://github.com/michaltkacz/graph-editor
     * Methode um Graph zu laden.
     */
    public void deserializeGraph() {
        if (graph == null)
            return;
        JFileChooser fc = new JFileChooser(".");
        fc.setMultiSelectionEnabled(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Binary files *.bin", "bin");
        fc.addChoosableFileFilter(filter);
        fc.setFileFilter(filter);
        int choosenOption = fc.showOpenDialog(this);
        if (choosenOption == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fc.getSelectedFile();
            try {
                graph = SaverLoader.deserializeGraph(selectedFile);
                JOptionPane.showMessageDialog(null, "Loaded from file " + selectedFile.getAbsolutePath());
                nodes = graph.getNodesofgraph();
                spheres = graph.getSphäresofgraph();
                edges = graph.getEdgesofgraph();
                f.setTitle("Syndromansatz - Projekt: " + selectedFile.getName());
                repaint();
            } catch (SaverLoadException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    /**
     * Methode um den Graph als PDF zu Exporten.
     */
    public void pdfExport(){
        final java.awt.Image image = jpaneltoImage(GraphPanel.this);
        JFileChooser fc = new JFileChooser(".");
        fc.setMultiSelectionEnabled(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Exported files *.pdf", "pdf");
        fc.addChoosableFileFilter(filter);
        fc.setFileFilter(filter);
        int choosenOption = fc.showSaveDialog(this);
        if (choosenOption == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fc.getSelectedFile();
            String fileName = selectedFile.getAbsolutePath();
            if (!fileName.endsWith(".pdf")) {
                selectedFile = new File(fileName + ".pdf");
            }
                JOptionPane.showMessageDialog(null, "Exported to file " + selectedFile.getAbsolutePath());
                pdfcreator(image, fileName + ".pdf");
        }
    }
    /**
     * https://stackoverflow.com/questions/4517907/how2-add-a-jpanel-to-a-document-then-export-to-pdf
     * https://www.baeldung.com/java-pdf-creation
     * Methode um Image in pdf datei zu schreiben.
     * @param image image, welches in die PDF geschrieben wird.
     * @param fileName Name der PDf-Datei
     */
    public void pdfcreator(Image image, String fileName) {
        try {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            com.itextpdf.text.Image img = com.itextpdf.text.Image.getInstance(writer, image, 1);
            img.setAbsolutePosition(0, 0);
            img.scalePercent(55);
            document.add(img);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * https://stackoverflow.com/questions/1349220/convert-jpanel-to-image
     * jpanel to image
     * @param component jpanel, welches zu image umgewandelt wird
     * @return Image
     */

    public  java.awt.Image jpaneltoImage(java.awt.Component component) {

        BufferedImage image = new BufferedImage(component.getWidth(),component.getHeight(), BufferedImage.TYPE_INT_RGB);
        component.paint(image.getGraphics());
        return image;
    }

    /**
     * Methode um kanten anzulegen.
     * @param relationstyp relationstyp der kante.
     */
    public void edgesverbinden(Relationstyp relationstyp) {
        ArrayList<GraphElement> selected = GetAllSelected();
        Node last = null;
        Color coloredge = control.hueIcon.getColor();
        for (GraphElement g : selected) {
            if (!(g instanceof Node)) {
                continue;
            }
            if (last != null) {
                Node start = last.selectedTime < g.selectedTime ? last : (Node) g;
                Node end = last.selectedTime >= g.selectedTime ? last : (Node) g;
                Edge newedge = new Edge(end, start);
                newedge.setColor(coloredge);
                newedge.setRelationstyp(relationstyp);
                edges.add(newedge);
            }
            last = (Node) g;
        }
        repaint();
    }

    /**
     * Methode um  ausgewählte graph elemente zu löschen.
     */
    public void ausgewaehlteGraphElementeLoeschen() {
        ArrayList<GraphElement> selected = GetAllSelected();
        for (GraphElement g : selected) {
            if (g instanceof Node) {
                deleteEdges(g);
                nodes.remove(g);
                g.getParent().getChildren().remove(g);
            } else if (g instanceof Sphere) {
                ArrayList<GraphElement> nodes = g.getChildren();
                for (GraphElement node : nodes) {
                    deleteEdges(node);
                }
                spheres.remove(g);
            }

        }
        repaint();
    }

    /**
     * Methode um kante eines knotens zu löschen.
     */
    public void deleteEdges(GraphElement n) {
        ListIterator<Edge> iter = edges.listIterator();
        while (iter.hasNext()) {
            Edge e = iter.next();
            if (e.startKnoten == n || e.endKnoten == n) {
                iter.remove();
            }
        }
    }

    /**
     * Methode um name der ausgewählte graph elemente zu bearbeiten.
     */
    public void namenAendern(String name) {
        Node.updateName(nodes, name);
        Sphere.updateName(spheres, name);
        control.repaint();
        repaint();
    }

    /**
     * Methode um farbe der ausgewählte graph elemente zu bearbeiten.
     */
    public void farbeAendern(Color color) {
        Node.updateColor(nodes, color);
        Sphere.updateColor(spheres, color);
        for (Edge ed : selectedEdges) {
            if (ed.isSelected()) {
                ed.setColor(color);
            }
        }
        control.hueIcon.setColor(color);
        control.repaint();
        repaint();
    }

    /**
     * Methode um ausgewählte kante zu löschen
     */
    public void kanteLoeschen() {
        for (Edge ed : selectedEdges) {
            if (ed != null) {
                edges.remove(ed);
                repaint();
            }
        }
    }

    /**
     * Methhode um alles auf dem graph zu löschen
     */
    public void allesLoeschen() {
        spheres.clear();
        nodes.clear();
        edges.clear();
        selectedEdges.clear();
        repaint();
    }

    /**
     * Methode um sphären anzulegen.
     * @param sphereename Name der Sphäre
     */
    public void sphereAnlegen(String sphereename) {
        for (Sphere s : spheres) {
            s.setSelected(false);
        }
        Color color = control.hueIcon.getColor();
        int d = spheres.size() == 0 ? 0 : spheres.get(spheres.size() - 1).depth + 1;
        Sphere s = new Sphere(getMousePosF(), new Point(100, 100), color, sphereename, d);
        s.setSelected(true);
        spheres.add(s);
        repaint();
    }

    /**
     * Methode um knoten anzulegen.
     * @param knotenName Name des Knotens
     * @param s Auf welche sphäre der knoten angelegt werden soll.
     */
    public void knotenAnlegen(String knotenName,Sphere s) {

        if (s.isSelected()) {
            FPoint p = s.transform;
            Color color = control.hueIcon.getColor();
            if (s.getColor() == color) {
                color = new Color((int) (Math.random() * 0x1000000));
            }
            int d = s.getMaxDepth();
            Node n = new Node(new FPoint(p), radius, color, s, knotenName, d + 1);
            nodes.add(n);
            s.addChild(n);
            repaint();
        }
    }

    /**
     * Methode um relationstyp einer kante zu bearbeiten
     * @param selection neu relationstyp
     */
    public void relationstypBearbeiten(Relationstyp selection) {
        for (Edge ed : selectedEdges) {
            if (ed.isSelected()) {
                if (ed != null) {
                    ed.setRelationstyp(selection);
                    repaint();
                }
            }
        }
    }
}

