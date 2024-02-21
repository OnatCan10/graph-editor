package de.unibremen.swp.model;


/**
 * Exception-Klasse für den Save/Loader
 * @author Michał Tkacz, Onat Can Vardareri, Mert Sendur
 */
public class SaverLoadException extends Exception {
    private static final long serialVersionUID = 5280421833743690760L;

    public SaverLoadException(String message) {
        super(message);
    }
}

