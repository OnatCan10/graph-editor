package de.unibremen.swp;


import de.unibremen.swp.model.Node;
import de.unibremen.swp.model.Sphere;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class GraphPanelTest {

    private GraphPanel gp;
    private ColorIcon hueIcon;

    @BeforeEach
    void setUp() {
        gp = new GraphPanel();
        hueIcon = new ColorIcon(Color.green);
    }

    @Test
    void sphereErstellenTest() {
        Sphere s = new Sphere(new FPoint(5, 5), new Point(100, 100), Color.BLUE, "hallo", 1);
        assertEquals(Color.BLUE, s.getColor());
        assertEquals("hallo", s.getName());
    }

    @Test
    void sphereFarbeAendernTest() {
        Sphere s = new Sphere(new FPoint(5, 5), new Point(100, 100), Color.BLUE, "hallo", 1);
        assertEquals(Color.BLUE, s.getColor());
        s.setColor(Color.ORANGE);
        assertEquals(Color.ORANGE, s.getColor());
    }

    @Test
    void sphereNamenAendernTest() {
        Sphere s = new Sphere(new FPoint(5, 5), new Point(100, 100), Color.BLUE, "hallo", 1);
        assertEquals("hallo", s.getName());
        s.setName("test");
        assertEquals("test", s.getName());
    }

    @Test
    void sphereInListeVorhanden() {
        assertEquals(0, gp.getSpheres().size());
        Sphere s = new Sphere(new FPoint(5, 5), new Point(100, 100), Color.BLUE, "hallo", 1);
        gp.getSpheres().add(s);
        assertEquals(1, gp.getSpheres().size());
    }

    @Test
    void sphereAnlegenInListe() {
        assertEquals(0, gp.getSpheres().size());
        gp.sphereAnlegen("test");
        assertEquals(1, gp.getSpheres().size());
    }

    @Test
    void sphereAnlegenInListe2() {
        gp.sphereAnlegen("test");
        assertEquals(Color.GREEN, gp.getSpheres().get(0).getColor());
    }

    @Test
    void sphereAnlegenInListe3() {
        gp.sphereAnlegen("test");
        assertEquals("test", gp.getSpheres().get(0).getName());
    }

    @Test
    void sphereNamenAendernInListe() {
        gp.sphereAnlegen("test");
        assertEquals("test", gp.getSpheres().get(0).getName());
        gp.getSpheres().get(0).setName("test2");
        assertEquals("test2", gp.getSpheres().get(0).getName());
    }

    @Test
    void sphereFarbeAendernInListe() {
        List<Sphere> list = new ArrayList<Sphere>();
        Sphere s = new Sphere(new FPoint(5, 5), new Point(100, 100), Color.RED, "hallo", 1);
        assertEquals(Color.RED, s.getColor());
        s.setSelected(true);
        list.add(s);
        Sphere.updateColor(list, Color.BLUE);
        assertEquals(Color.BLUE, s.getColor());
    }

    @Test
    void sphereFarbeAendernAlleAusgewaehlten() {
        List<Sphere> list = new ArrayList<Sphere>();
        Sphere s = new Sphere(new FPoint(5, 5), new Point(100, 100), Color.RED, "hallo", 1);
        Sphere s2 = new Sphere(new FPoint(5, 5), new Point(100, 100), Color.GRAY, "hallo", 1);
        Sphere s3 = new Sphere(new FPoint(5, 5), new Point(100, 100), Color.BLUE, "hallo", 1);
        s.setSelected(true);
        s2.setSelected(true);
        s3.setSelected(true);
        list.add(s);
        list.add(s2);
        list.add(s3);
        Sphere.updateColor(list, Color.ORANGE);
        assertEquals(Color.ORANGE, s.getColor());
        assertEquals(Color.ORANGE, s2.getColor());
        assertEquals(Color.ORANGE, s3.getColor());
    }

    @Test
    void sphereFarbeAendernNurEinerAusgewaehlten() {
        List<Sphere> list = new ArrayList<Sphere>();
        Sphere s = new Sphere(new FPoint(5, 5), new Point(100, 100), Color.RED, "hallo", 1);
        Sphere s2 = new Sphere(new FPoint(5, 5), new Point(100, 100), Color.GRAY, "hallo", 1);
        Sphere s3 = new Sphere(new FPoint(5, 5), new Point(100, 100), Color.BLUE, "hallo", 1);
        s.setSelected(true);
        s2.setSelected(false);
        s3.setSelected(false);
        list.add(s);
        list.add(s2);
        list.add(s3);
        Sphere.updateColor(list, Color.ORANGE);
        assertEquals(Color.ORANGE, s.getColor());
        assertEquals(Color.GRAY, s2.getColor());
        assertEquals(Color.BLUE, s3.getColor());
    }

    @Test
    void sphereFarbeAendernWennNichtAusgewaehlt() {
        List<Sphere> list = new ArrayList<Sphere>();
        Sphere s = new Sphere(new FPoint(5, 5), new Point(100, 100), Color.RED, "hallo", 1);
        Sphere s2 = new Sphere(new FPoint(5, 5), new Point(100, 100), Color.GRAY, "hallo", 1);
        s.setSelected(false);
        s2.setSelected(false);
        list.add(s);
        list.add(s2);
        Sphere.updateColor(list, Color.ORANGE);
        assertEquals(Color.RED, s.getColor());
        assertEquals(Color.GRAY, s2.getColor());
    }

    @Test
    void sphereAnlegenFarbeAendernAlleAusgewaehlten() {
        assertEquals(0, gp.getSpheres().size());
        gp.sphereAnlegen("test");
        gp.sphereAnlegen("test");
        gp.sphereAnlegen("test");
        assertEquals(3, gp.getSpheres().size());
        gp.getSpheres().get(0).setSelected(true);
        gp.getSpheres().get(1).setSelected(true);
        gp.getSpheres().get(2).setSelected(true);
        Sphere.updateColor(gp.getSpheres(), Color.ORANGE);
        assertEquals(Color.ORANGE, gp.getSpheres().get(0).getColor());
        assertEquals(Color.ORANGE, gp.getSpheres().get(1).getColor());
        assertEquals(Color.ORANGE, gp.getSpheres().get(2).getColor());
    }

    @Test
    void sphereAnlegenNamenAendernAlleAusgewaehlten() {
        assertEquals(0, gp.getSpheres().size());
        gp.sphereAnlegen("test");
        gp.sphereAnlegen("test");
        gp.sphereAnlegen("test");
        assertEquals(3, gp.getSpheres().size());
        gp.getSpheres().get(0).setSelected(true);
        gp.getSpheres().get(1).setSelected(true);
        gp.getSpheres().get(2).setSelected(true);
        Sphere.updateName(gp.getSpheres(), "asd");
        assertEquals("asd", gp.getSpheres().get(0).getName());
        assertEquals("asd", gp.getSpheres().get(1).getName());
        assertEquals("asd", gp.getSpheres().get(2).getName());
    }

    @Test
    void sphereAnlegenFarbeAendernNurEinsAusgewaehlt() {
        assertEquals(0, gp.getSpheres().size());
        gp.sphereAnlegen("test");
        gp.sphereAnlegen("test");
        assertEquals(2, gp.getSpheres().size());
        gp.getSpheres().get(0).setSelected(true);
        gp.getSpheres().get(1).setSelected(false);
        Sphere.updateColor(gp.getSpheres(), Color.ORANGE);
        assertEquals(Color.ORANGE, gp.getSpheres().get(0).getColor());
        assertEquals(hueIcon.getColor(), gp.getSpheres().get(1).getColor());

    }

    @Test
    void sphereAnlegenNamenAendernNurEinsAusgewaehlt() {
        assertEquals(0, gp.getSpheres().size());
        gp.sphereAnlegen("test");
        gp.sphereAnlegen("test");
        assertEquals(2, gp.getSpheres().size());
        gp.getSpheres().get(0).setSelected(true);
        gp.getSpheres().get(1).setSelected(false);
        Sphere.updateName(gp.getSpheres(), "asd");
        assertEquals("asd", gp.getSpheres().get(0).getName());
        assertEquals("test", gp.getSpheres().get(1).getName());
    }

    //-----------------------------------------------------


    @Test
    void nodeErstellenTest() {
        Sphere s = new Sphere(new FPoint(5, 5), new Point(100, 100), Color.RED, "hallo", 1);
        Node n = new Node(new FPoint(s.transform), new Point(100,100), Color.BLUE, s, "test", s.getMaxDepth() + 1);
        assertEquals(Color.BLUE, n.getColor());
        assertEquals("test", n.getName());
    }

    @Test
    void nodeErstellenInNodesListeTest() {
        Sphere s = new Sphere(new FPoint(5, 5), new Point(100, 100), Color.RED, "hallo", 1);
        Node n = new Node(new FPoint(s.transform), new Point(100,100), Color.BLUE, s, "test", s.getMaxDepth() + 1);
        assertEquals(0, gp.getNodes().size());
        gp.getNodes().add(n);
        assertEquals(1, gp.getNodes().size());
        assertEquals(Color.BLUE, gp.getNodes().get(0).getColor());
        assertEquals("test", gp.getNodes().get(0).getName());
    }

    @Test
    void nodeFarbeAendernTest() {
        Sphere s = new Sphere(new FPoint(5, 5), new Point(100, 100), Color.RED, "hallo", 1);
        Node n = new Node(new FPoint(s.transform), new Point(100,100), Color.BLUE, s, "test", s.getMaxDepth() + 1);
        assertEquals(Color.BLUE, n.getColor());
        n.setColor(Color.ORANGE);
        assertEquals(Color.ORANGE, n.getColor());
    }

    @Test
    void nodeFarbeAendernInListeTest() {
        Sphere s = new Sphere(new FPoint(5, 5), new Point(100, 100), Color.RED, "hallo", 1);
        Node n = new Node(new FPoint(s.transform), new Point(100,100), Color.BLUE, s, "test", s.getMaxDepth() + 1);
        assertEquals(0, gp.getNodes().size());
        gp.getNodes().add(n);
        assertEquals(1, gp.getNodes().size());
        gp.getNodes().get(0).setColor(Color.ORANGE);
        assertEquals(Color.ORANGE,gp.getNodes().get(0).getColor());
    }


    @Test
    void nodeAnlegenInListe() {
        gp.sphereAnlegen("sphere");
        gp.getSpheres().get(0).setSelected(true);
        assertEquals(0, gp.getNodes().size());
        gp.knotenAnlegen("test", gp.getSpheres().get(0));
        assertEquals(1, gp.getNodes().size());
    }

    @Test
    void nodeAnlegenInListe2() {
        gp.sphereAnlegen("sphere");
        gp.getSpheres().get(0).setSelected(true);
        gp.knotenAnlegen("test", gp.getSpheres().get(0));
        assertEquals("test", gp.getNodes().get(0).getName());
    }

    @Test
    void nodeAnlegenNamenAendernInListe() {
        gp.sphereAnlegen("sphere");
        gp.knotenAnlegen("test", gp.getSpheres().get(0));
        assertEquals("test", gp.getNodes().get(0).getName());
        gp.getNodes().get(0).setName("asd");
        assertEquals("asd", gp.getNodes().get(0).getName());
    }

    @Test
    void nodeAnlegenFarbeAendernInListe() {
        gp.sphereAnlegen("sphere");
        gp.knotenAnlegen("test", gp.getSpheres().get(0));
        gp.getNodes().get(0).setColor(Color.BLACK);
        assertEquals(Color.BLACK, gp.getNodes().get(0).getColor());
    }



    @Test
    void nodeFarbeAendernInListe() {
        List<Node> list = new ArrayList<Node>();
        Sphere s = new Sphere(new FPoint(5, 5), new Point(100, 100), Color.RED, "hallo", 1);
        Node n = new Node(new FPoint(s.transform), new Point(100,100), Color.BLUE, s, "test", s.getMaxDepth() + 1);
        list.add(n);
        n.setSelected(true);
        Node.updateColor(list, Color.ORANGE);
        assertEquals(Color.ORANGE, n.getColor());
    }

    @Test
    void nodeFarbeAendernInListeNichtAusgewaehlt() {
        List<Node> list = new ArrayList<Node>();
        Sphere s = new Sphere(new FPoint(5, 5), new Point(100, 100), Color.RED, "hallo", 1);
        Node n = new Node(new FPoint(s.transform), new Point(100,100), Color.BLUE, s, "test", s.getMaxDepth() + 1);
        list.add(n);
        n.setSelected(false);
        Node.updateColor(list, Color.ORANGE);
        assertEquals(Color.BLUE, n.getColor());
    }

    @Test
    void nodeFarbeAendernAlleAusgewaehlten() {
        List<Node> list = new ArrayList<Node>();
        Sphere s = new Sphere(new FPoint(5, 5), new Point(100, 100), Color.RED, "hallo", 1);
        Node n = new Node(new FPoint(s.transform), new Point(100,100), Color.BLUE, s, "test", s.getMaxDepth() + 1);
        Node n2 = new Node(new FPoint(s.transform), new Point(100,100), Color.YELLOW, s, "test2", s.getMaxDepth() + 1);
        n.setSelected(true);
        n2.setSelected(true);
        list.add(n);
        list.add(n2);
        Node.updateColor(list, Color.ORANGE);
        assertEquals(Color.ORANGE, n.getColor());
        assertEquals(Color.ORANGE, n2.getColor());
    }


    @Test
    void nodeFarbeAendernNurEinsAusgewaehlten() {
        List<Node> list = new ArrayList<Node>();
        Sphere s = new Sphere(new FPoint(5, 5), new Point(100, 100), Color.RED, "hallo", 1);
        Node n = new Node(new FPoint(s.transform), new Point(100,100), Color.BLUE, s, "test", s.getMaxDepth() + 1);
        Node n2 = new Node(new FPoint(s.transform), new Point(100,100), Color.YELLOW, s, "test2", s.getMaxDepth() + 1);
        n.setSelected(true);
        n2.setSelected(false);
        list.add(n);
        list.add(n);
        Node.updateColor(list, Color.ORANGE);
        assertEquals(Color.ORANGE, n.getColor());
        assertEquals(Color.YELLOW, n2.getColor());
    }



    @Test
    void nodeAnlegenFarbeAendernNurZweitesAusgewaehlt() {
        Sphere s = new Sphere(new FPoint(5, 5), new Point(100, 100), Color.RED, "hallo", 1);
        s.setSelected(true);
        gp.knotenAnlegen("test", s);
        gp.knotenAnlegen("test2", s);
        gp.getNodes().get(0).setSelected(false);
        gp.getNodes().get(1).setSelected(true);
        Node.updateColor(gp.getNodes(), Color.ORANGE);
        assertEquals(Color.ORANGE, gp.getNodes().get(1).getColor());
    }


    @Test
    void nodeAnlegenNamenAendernNichtAusgewaehlt() {
        Sphere s = new Sphere(new FPoint(5, 5), new Point(100, 100), Color.RED, "hallo", 1);
        s.setSelected(true);
        gp.knotenAnlegen("test", s);
        gp.knotenAnlegen("test2", s);
        gp.getNodes().get(0).setSelected(false);
        gp.getNodes().get(1).setSelected(false);
        assertEquals("test", gp.getNodes().get(0).getName());
        assertEquals("test2", gp.getNodes().get(1).getName());
        Node.updateName(gp.getNodes(), "neu");
        assertEquals("test", gp.getNodes().get(0).getName());
        assertEquals("test2", gp.getNodes().get(1).getName());
    }

    @Test
    void nodeAnlegenNamenAendernNurEinsAusgewaehlt() {
        Sphere s = new Sphere(new FPoint(5, 5), new Point(100, 100), Color.RED, "hallo", 1);
        s.setSelected(true);
        gp.knotenAnlegen("test", s);
        gp.knotenAnlegen("test2", s);
        gp.getNodes().get(0).setSelected(true);
        gp.getNodes().get(1).setSelected(false);
        assertEquals("test", gp.getNodes().get(0).getName());
        assertEquals("test2", gp.getNodes().get(1).getName());
        Node.updateName(gp.getNodes(), "neu");
        assertEquals("neu", gp.getNodes().get(0).getName());
        assertEquals("test2", gp.getNodes().get(1).getName());
    }

    @Test
    void nodeAnlegenZugehoerigeSphaere() {
        Sphere s = new Sphere(new FPoint(5, 5), new Point(100, 100), Color.RED, "hallo", 1);
        s.setSelected(true);
        gp.knotenAnlegen("test", s);
        assertEquals(s, gp.getNodes().get(0).getParent());
    }
}