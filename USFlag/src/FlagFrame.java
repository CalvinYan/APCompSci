import java.awt.*;
import javax.swing.JFrame;

/**
 * The FlagFrame is a container for the panel that displays the flag.
 * It contains the title of the window as well as the minimize, maximize, and
 * exit buttons surrounding the flag.
 * The frame can be resized, and, when resized, changes the dimensions of the
 * panel as well.
 * 
 * Please note that the frame has no paint() method, as the only component that
 * needs to repaint itself is the panel.
 * @author Calvin Yan
 *
 */

public class FlagFrame extends JFrame {
	
	// The panel that draws the flag
	private FlagPanel panel = new FlagPanel(1330, 700);
	
	public FlagFrame() {
		// Set the title of the frame
		super("The American Flag!");
		init();
	}
	
	/**
	 * Configures the frame and panel and calls repaint().
	 */
	public void init() {
		add(panel);
		setResizable(true);
		pack();
		repaint();
	}
	
}
