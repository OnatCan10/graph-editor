package de.unibremen.swp.model;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Zum erkennen von "hits", also Treffern
 * @author Onat Can Vardareri, Mert Sendur
 */
public class HitResult implements Serializable {
	// Ob es consumed wurde und keine weiteren Elemente getestet werden sollen
	public boolean consumed;

	// Alle Element hits in einer Liste
	public ArrayList<GraphElement> elementsHit;


	/**
	 * Konstruktor von HitResult
	 */
	public HitResult()
	{
		this.consumed = false;
		this.elementsHit = new ArrayList<GraphElement>();
	}
}
