package infovis.diagram.elements;

import java.awt.Color;
import java.awt.Graphics2D;

public class None implements Element {

	public boolean contains(double x, double y) {
		return false;
	}

	public Color getColor() {
		return Color.WHITE;
	}

	public double getX() {
		return 0;
	}

	public double getY() {
		return 0;
	}

	public void paint(Graphics2D g2D) {
	}

	public void setColor(Color color) {
	}

	public void setX(double x) {
	}

	public void setY(double y) {
	}

	public void updatePosition(double x, double y) {
	}

	public int getID() {
		return 0;
	}

	@Override
	public void setWidth(double width) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHeight(double height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

}
