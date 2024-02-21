package de.unibremen.swp;

import java.awt.*;
import java.io.Serializable;

/**
 * Point Klasse in float.
 * @author Onat Can Vardareri
 */
public class FPoint implements Serializable {
	public float x = 0.f;
	public float y = 0.f;
	
	public FPoint(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	FPoint(Point p)
	{
		this.x = p.x;
		this.y = p.y;
	}
	
	public FPoint(FPoint copy)
	{
		this.x = copy.x;
		this.y = copy.y;
	}

    /**
     * subtraction von Fpoints
     * @param a das erste fpoint
     * @param b das zweite fpoint
     * @return subtracted fpoint
     */
	public static FPoint subtract(FPoint a, FPoint b) {
		return new FPoint(a.x - b.x,a.y - b.y);
	}

    /**
     * Normalisierung von FPoint
     * @param a Fpoint zum normalisieren
     * @return Normalisierter Fpoint
     */
	public static FPoint normalize(FPoint a) {
		if (a.x == 0 && a.y == 0) {
			return new FPoint(a);
		}
		float length = (float) Math.sqrt(a.x*a.x+a.y*a.y);
		return new FPoint(a.x / length, a.y / length);
	}
}
