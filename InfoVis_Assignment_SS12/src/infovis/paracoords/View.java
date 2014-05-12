package infovis.paracoords;

import infovis.debug.Debug;
import infovis.scatterplot.Model;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class View extends JPanel {
	private Model model = null;
	private int padding = 50;

	@Override
	public void paint(Graphics g) {
		
		Graphics2D g2D = (Graphics2D) g;
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2D.clearRect(0, 0, getWidth(), getHeight());
		drawCoordinateSystem(g2D);
	}
	public void drawCoordinateSystem(Graphics2D g2D) {
//		g2D.drawLine(padding, padding, padding, getHeight()-padding);
//		g2D.drawLine(getWidth()-padding, padding, getWidth()-padding, getHeight()-padding);
//		Debug.println(String.valueOf(model.getDim()));
		for(int i=0; i<model.getDim(); ++i) {
			int verschiebung = padding + i * (getWidth())/model.getDim();
			g2D.drawLine(verschiebung, padding, verschiebung, getHeight()-padding);
		}
		
		g2D.drawLine(padding, getHeight()-padding, model.getDim()*(getWidth()/model.getDim()), getHeight()-padding);
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
