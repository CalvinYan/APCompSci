import java.awt.*;
import javax.swing.JFrame;

public class FlagFrame extends JFrame {
	
	private FlagPanel panel = new FlagPanel(1330, 700);
	
	public FlagFrame() {
		init();
	}
	
	public void init() {
		add(panel);
		setResizable(true);
		pack();
		repaint();
	}
	
	public void paint(Graphics g) {
		panel.repaint();
	}
	
	
}
