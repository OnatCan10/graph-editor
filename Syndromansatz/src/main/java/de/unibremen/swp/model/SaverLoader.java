package de.unibremen.swp.model;

import java.io.*;
import java.util.List;

/**
 * Klasse um den graph speichern und laden.
 * https://github.com/michaltkacz/graph-editor
 *
 * @author Michał Tkacz, Onat Can Vardareri, Mert Sendur
 */
public class SaverLoader implements Serializable {

    private static final long serialVersionUID = 5673009196816218789L;
    //eine liste aus nodes
    private List<Node> nodesofgraph;
    //eine liste aus kanten
    private List<Edge> edgesofgraph;
    //eine liste aus sphären
    private List<Sphere> sphäresofgraph;

    public SaverLoader(List<Node> node,List<Edge> edge,List<Sphere> sphere) {
        this.nodesofgraph=node;
        this.edgesofgraph=edge;
        this.sphäresofgraph=sphere;
    }

    /**
     * getter für node list
     * @return gibt die node liste zurück
     */
    public List<Node> getNodesofgraph() {
        return nodesofgraph;
    }
    /**
     * getter für kante list
     * @return gibt die kante liste zurück
     */
    public List<Edge> getEdgesofgraph() {
        return edgesofgraph;
    }
    /**
     * getter für sphäre list
     * @return gibt die sphäre liste zurück
     */
    public List<Sphere> getSphäresofgraph() {
        return sphäresofgraph;
    }

    /**
     * https://github.com/michaltkacz/graph-editor
     * Methode um den graph zu serializieren.
     * @param file wo der graph gespeichert wird
     * @param graph graph,welches serializiert werden soll
     * @throws SaverLoadException error wenn eine falsche datei ausgewählt wurde oder der graph nicht serializiert werden könnte.
     */
    public static void serializeGraph(File file, SaverLoader graph) throws SaverLoadException {
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))){
            out.writeObject(graph);
        }catch(IOException e) {
            throw new SaverLoadException("Serialization error");
        }
    }

    /**
     * https://github.com/michaltkacz/graph-editor
     * Methode um den graph zu deserializieren.
     * @param file graph , der geladen wird
     * @return gibt den graph zurück.
     * @throws SaverLoadException error wenn eine falsche datei ausgewählt wurde oder der graph nicht deserializiert werden könnte.
     */
    public static SaverLoader deserializeGraph(File file) throws SaverLoadException {
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))){
            SaverLoader graph = (SaverLoader) in.readObject();
            return graph;
        }catch(IOException e) {
            throw new SaverLoadException("File fatal error!");
        } catch (ClassNotFoundException e) {
            throw new SaverLoadException("No such object was found!");
        }
    }


}

