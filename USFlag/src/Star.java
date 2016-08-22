import java.awt.*;

/**
 * A utility class for drawing a perfect 5-point star. The class draws a star
 * by calculating the coordinates of each of the 10 points; it does this by
 * determining the angle and Euclidean distance from each point to the star's
 * center and finding their position using sines and cosines.
 * @author Calvin Yan
 *
 */

public class Star extends Polygon {
	
	private double outerDiameter = 0, innerDiameter = 0;
	
	// Location of the top left corner of the star's bounding box.
	private int originX = 0, originY = 0;
	
	public Star(double diameter, int originX, int originY) {
		this.outerDiameter = diameter;
		this.originX = originX;
		this.originY = originY;
		/* Use the Law of Sines to get the distance from each inner point to
		 * the center.
		 */
		innerDiameter = Math.sin(Math.PI / 10) * diameter / 
			Math.sin(7 * Math.PI / 10);
		initPoints();
	}
	
	/**
	 * Calculates the 10 points of the star and adds them.
	 */
	public void initPoints() {
		double outerRadius = outerDiameter / 2, innerRadius = innerDiameter / 2,
			centerX = originX + outerRadius, centerY = originY + outerRadius;
		// The angular distance between each point (36 degrees)
		double angleIncrement = Math.PI / 5;
		for (int i = 0; i < 10; i++) {
			// Alternate between outer and inner points
			double radius = (i % 2 == 0) ? outerRadius : innerRadius;
			// Derive x and y using the angle and radius
			int x = (int) (centerX - Math.cos(Math.PI / 2 - i * angleIncrement) * radius),
				y = (int) (centerY - Math.sin(Math.PI / 2 - i * angleIncrement) * radius);
			addPoint(x, y);
		}
	}
}
