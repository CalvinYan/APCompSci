import java.awt.*;
import javax.swing.JFrame;

/**
 * The CowFrame class represents a JFrame in which the cow is contained.
 * Instantiating a new CowFrame calls the init() method, which configures the
 * frame and calls repaint(). The paint() method draws the cow when called and
 * controls the cow's animation.
 * @author Calvin Yan
 * 
 */
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
	 * Draws the cow and background piece by piece.
	 */
	public void paint(Graphics g) {
		//Grass
		g.setColor(Color.GREEN);
		g.fillRect(0, 300, 700, 300);
		
		//Sky
		g.setColor(Color.CYAN);
		g.fillRect(0, 0, 700, 300);
		
		//Sun
		g.setColor(Color.YELLOW);
		g.fillOval(500, 100, 75, 75);
		
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
		
		//Animate cow to blink periodically
		try {
			Thread.sleep(3000);
			g.setColor(BROWN);
			//Remove the eyes
			g.fillOval(410, 180, 25, 25);
			g.fillOval(465, 180, 25, 25);
			
			//Add lines to represent closed eyes, then open eyes quickly
			g.setColor(Color.BLACK);
			g.drawLine(400, 193, 445, 193);
			g.drawLine(455, 193, 500, 193);
			Thread.sleep(100);
			g.setColor(BROWN);
			g.drawLine(400, 193, 445, 193);
			g.drawLine(455, 193, 500, 193);
			g.setColor(Color.BLACK);
			g.fillOval(410, 180, 25, 25);
			g.fillOval(465, 180, 25, 25);
		} catch (Exception e) {
			e.printStackTrace();
		}
		repaint();
	}
}
