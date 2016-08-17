import java.awt.*;

public class FlagPanel extends Panel {
	
	private int NUM_STRIPES = 13;
	
	private double BLUE_SCALE = 0.76;
	
	private Color OLD_GLORY_RED = new Color(187, 19, 62), 
			OLD_GLORY_BLUE = new Color(0, 33, 71);
	
	public FlagPanel(int width, int height) {
		setPreferredSize(new Dimension(width, height));
		init();
	}
	
	public void init() {
		setBackground(Color.WHITE);
		setVisible(true);
		repaint();
	}
	
	public void paint(Graphics g) {
		int height = getHeight();
		int stripeHeight = height / NUM_STRIPES + 1;
		
		//Red stripes
		g.setColor(OLD_GLORY_RED);
		for (int i = 0; i < 7; i++) {
			g.fillRect(0, i * 2 * stripeHeight, getWidth(), stripeHeight);
		}
		
		//Blue canton
		g.setColor(OLD_GLORY_BLUE);
		g.fillRect(0, 0, (int)(BLUE_SCALE * height), 7 * stripeHeight);
		
		//50 stars
		g.setColor(Color.WHITE);
		double starDiameter = height * 0.0616;
		for (int i = 0; i < 9; i++) {
			int numStars = 6 - i % 2;
			int y = (int) ((height * (i + 1) * 0.054) - starDiameter / 2);
			for (int j = 0; j < numStars; j++) {
				int x = (int) (height * ((i % 2 + 1) * 0.063 + j * 0.126) - starDiameter / 2);
				Star s = new Star(starDiameter, x, y);
				g.fillPolygon(s);
			}
		}
	}
	
}
