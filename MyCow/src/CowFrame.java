import java.awt.*;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class CowFrame extends JFrame {
	private final Color BISQUE = new Color(0xcdb79e),
			BROWN = new Color(0x663300);
	
	public CowFrame () {
		init();
	}
	
	/**
	 * Creates the window in which the cow is drawn.
	 */
	public void init() {
		setSize(700, 600);
		setBackground(Color.WHITE);
		repaint();
	}
	
	/**
	 * Draws the cow piece by piece.
	 */
	public void paint(Graphics g) {
		
		//Body
		g.setColor(BISQUE);
		g.fillRect(50, 200, 400, 200);
		
		//Ears
		g.setColor(Color.PINK);
		g.fillOval(340, 140, 60, 30);
		g.fillOval(500, 140, 60, 30);
		
		//Head
		g.setColor(BROWN);
		g.fillOval(375, 120, 150, 160);
		
		//Mouth
		g.setColor(Color.PINK);
		g.fillOval(375, 225, 150, 75);
		
		//Nostrils
		g.setColor(Color.BLACK);
		g.fillOval(410, 240, 25, 50);
		g.fillOval(465, 240, 25, 50);
		
		//Eyes
		g.fillOval(410, 180, 25, 25);
		g.fillOval(465, 180, 25, 25);
		
		//Legs
		g.setColor(BROWN);
		g.fillRect(100, 400, 30, 100);
		g.fillRect(300, 400, 30, 100);
		g.fillRect(150, 400, 30, 100);
		g.fillRect(350, 400, 30, 100);
		
		//Spots
		g.fillOval(100, 240, 100, 75);
		g.fillOval(300, 320, 40, 60);
		g.fillOval(200, 240, 75,  30);
		g.fillOval(175, 310, 50, 50);
		g.fillOval(280, 210, 80, 80);
	}
}
