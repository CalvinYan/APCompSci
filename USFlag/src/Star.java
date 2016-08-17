import java.awt.*;

public class Star extends Polygon {
	
	private double outerDiameter = 0, innerDiameter = 0;
	
	private int originX = 0, originY = 0;
	
	public Star(double diameter, int originX, int originY) {
		this.outerDiameter = diameter;
		this.originX = originX;
		this.originY = originY;
		innerDiameter = Math.sin(Math.PI / 10) * diameter / 
			Math.sin(7 * Math.PI / 10);
		initPoints();
	}
	
	public void initPoints() {
		double outerRadius = outerDiameter / 2, innerRadius = innerDiameter / 2,
			centerX = originX + outerRadius, centerY = originY + outerRadius;
		double angleIncrement = Math.PI / 5;
		for (int i = 0; i < 10; i++) {
			double radius = (i % 2 == 0) ? outerRadius : innerRadius;
			int x = (int) (centerX - Math.cos(Math.PI / 2 - i * angleIncrement) * radius),
				y = (int) (centerY - Math.sin(Math.PI / 2 - i * angleIncrement) * radius);
			addPoint(x, y);
		}
	}
}
