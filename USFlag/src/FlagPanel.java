import java.awt.*;

/**
 * The FlagPanel class is the most vital part of the program. It draws the US
 * flag in the paint() method, adding the stripes, canton, and stars (in that
 * order) with the proper dimensions. The flag is scaled based on the height
 * of the panel.
 * @author Calvin Yan
 *
 */

public class FlagPanel extends Panel {
	
	private int NUM_STRIPES = 13;
	
	// The ratio of the union's fly length(horizontal length) to the panel height
	private double BLUE_SCALE = 0.76;
	
	// The colors of the American flag
	private Color OLD_GLORY_RED = new Color(187, 19, 62), 
			OLD_GLORY_BLUE = new Color(0, 33, 71);
	
	public FlagPanel(int width, int height) {
		setPreferredSize(new Dimension(width, height));
		init();
	}
	
	/**
	 * Set the background color of the panel, display it, and call paint().
	 */
	public void init() {
		setBackground(Color.WHITE);
		setVisible(true);
		repaint();
	}
	
	/**
	 * Adds the components of the flag in three parts. The red stripes are
	 * rectangles, while the white stripes are simply gaps in between (as the
	 * panel itself is white). The blue union is a rectangle, and the stars are
	 * represented by a subclass of java.awt.Polygon (see Star.java).
	 */
	public void paint(Graphics g) {
		int height = getHeight();
		
		// Height of each of the 13 stripes
		int stripeHeight = height / NUM_STRIPES + 1;
		
		// Red stripes
		g.setColor(OLD_GLORY_RED);
		for (int i = 0; i < 7; i++) {
			g.fillRect(0, i * 2 * stripeHeight, getWidth(), stripeHeight);
		}
		
		// Blue rectangle
		g.setColor(OLD_GLORY_BLUE);
		g.fillRect(0, 0, (int)(BLUE_SCALE * height), 7 * stripeHeight);
		
		// 50 stars
		g.setColor(Color.WHITE);
		double starDiameter = height * 0.0616;
		// Add the stars using a double for-loop 
		for (int i = 0; i < 9; i++) {
			// Number of stars in the row (alternates between 6 and 5)
			int numStars = 6 - i % 2;
			int y = (int) ((height * (i + 1) * 0.054) - starDiameter / 2);
			for (int j = 0; j < numStars; j++) {
				int x = (int) (height * ((i % 2 + 1) * 0.063 + j * 0.126) - starDiameter / 2);
				// Add a star at the specified coordinates
				Star s = new Star(starDiameter, x, y);
				g.fillPolygon(s);
			}
		}
	}
	
}
