package infovis.paracoords;

import infovis.debug.Debug;
import infovis.scatterplot.Model;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class View extends JPanel {
	private Model model = null;
	private int padding = 80;
	
	@Override
	public void paint(Graphics g) {
		
		Graphics2D g2D = (Graphics2D) g;
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2D.clearRect(0, 0, getWidth(), getHeight());
		drawCoordinateSystem(g2D);
		drawLables(g2D);
	}
	public void drawCoordinateSystem(Graphics2D g2D) {
		int coordWidth = (getWidth()-padding)/model.getDim();
		for(int i=0; i<model.getDim(); i++) {
			int verschiebung = padding + i * coordWidth;
			g2D.drawLine(verschiebung, padding, verschiebung, getHeight()-padding);
		}
		g2D.drawLine(padding, getHeight()-padding, padding+(model.getDim()-1)*coordWidth, getHeight()-padding);
	}
	public void drawLables(Graphics2D g2D) {
		int coordWidth = (getWidth()-padding)/model.getDim();
		int i = 0;
		for (String l : model.getLabels()) {
			g2D.drawString(l, (int) (padding+coordWidth*i+3), (int) (getHeight()-padding+13)+13*(i%2));
			i++;
		}
	}

	@Override
	public void update(Graphics g) {
		paint(g);
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}
	
}
