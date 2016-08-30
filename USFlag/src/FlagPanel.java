import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
	
	// 50 stars
	private Star[] stars = new Star[50];
	
	private double rotation = 0;
	
	private int flagWidth = 0, flagHeight = 0;
	
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
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				//rotation = Math.PI / 2;
				//repaint();
				for (int i = 1; i <= 90; i++) {
					rotation = i * Math.PI / 45;
					paint(getGraphics());
					try {
						Thread.sleep(5);
					} catch(InterruptedException e1){
						e1.printStackTrace();
					}
				}
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {}

			@Override
			public void mouseExited(MouseEvent arg0) {}

			@Override
			public void mousePressed(MouseEvent arg0) {}

			@Override
			public void mouseReleased(MouseEvent arg0) {}
		});
		repaint();
	}
	
	/**
	 * Adds the components of the flag in three parts. The red stripes are
	 * rectangles, while the white stripes are simply gaps in between (as the
	 * panel itself is white). The blue union is a rectangle, and the stars are
	 * represented by a subclass of java.awt.Polygon (see Star.java).
	 */
	public void paint(Graphics g) {
		System.out.println(rotation);
		flagHeight = (int) Math.min(getHeight(), getWidth() / 1.9);
		flagWidth = (int) Math.min(getWidth(), getHeight() * 1.9);
		
		// Height of each of the 13 stripes
		int stripeHeight = flagHeight / NUM_STRIPES + 1;
		
		// Red stripes
		g.setColor(OLD_GLORY_RED);
		for (int i = 0; i < 7; i++) {
			g.fillRect(0, i * 2 * stripeHeight, flagWidth, stripeHeight);
		}
		
		// Blue rectangle
		g.setColor(OLD_GLORY_BLUE);
		g.fillRect(0, 0, (int)(BLUE_SCALE * flagHeight), 7 * stripeHeight);
		
		// 50 stars
		g.setColor(Color.WHITE);
		double starDiameter = flagHeight * 0.0616;
		int counter = 0;
		// Add the stars using a double for-loop 
		for (int i = 0; i < 9; i++) {
			// Number of stars in the row (alternates between 6 and 5)
			int numStars = 6 - i % 2;
			int y = (int) ((flagHeight * (i + 1) * 0.054) - starDiameter / 2);
			for (int j = 0; j < numStars; j++) {
				int x = (int) (flagHeight * ((i % 2 + 1) * 0.063 + j * 0.126) - starDiameter / 2);
				// Add a star at the specified coordinates
				Star s = new Star(starDiameter, x, y, rotation);
				g.fillPolygon(s);
				stars[counter++] = s;
			}
		}
	}
	
}
