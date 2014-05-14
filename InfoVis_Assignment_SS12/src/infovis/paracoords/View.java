package infovis.paracoords;

import infovis.scatterplot.Data;
import infovis.scatterplot.Model;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

public class View extends JPanel {
	private Model model = null;
	private int padding = 80;
    private Rectangle2D markerRectangle = new Rectangle2D.Double(0.0,0.0,0.0,0.0); 
	 public Rectangle2D getMarkerRectangle() {
		return markerRectangle;
	}
	
	@Override
	public void paint(Graphics g) {
		
		Graphics2D g2D = (Graphics2D) g;
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2D.clearRect(0, 0, getWidth(), getHeight());
		g2D.draw(markerRectangle);
		drawCoordinateSystem(g2D);
		drawLables(g2D);
		drawData(g2D);
	}
	public void drawCoordinateSystem(Graphics2D g2D) {
		int coordWidth = (getWidth()-padding)/model.getDim();
		for(int i=0; i<model.getDim(); ++i) {
			int verschiebung = padding + i * coordWidth;
			g2D.drawLine(verschiebung, padding, verschiebung, getHeight()-padding);
		}
		//g2D.drawLine(padding, getHeight()-padding, padding+(model.getDim()-1)*coordWidth, getHeight()-padding);
	}
	public void drawLables(Graphics2D g2D) {
		int coordWidth = (getWidth()-padding)/model.getDim();
		int i = 0;
		for (String l : model.getLabels()) {
			g2D.drawString(l, (int) (padding+coordWidth*i+3), (int) (getHeight()-padding+13)+13*(i%2));
			i++;
		}
	}
	public void drawData(Graphics2D g2D) {
		//Schleife über Alle Objekte
		int coordWidth = (getWidth()-padding)/model.getDim();
		for(int i=0; i<model.getList().size(); ++i) {
			Data d = model.getList().get(i);
			//Schleife über alle Values eines Objekts
			for(int j=0; j<model.getDim()-1; ++j) {
				double max = padding+10;
				double min = getHeight()-padding-10;
				
				double valLeft = d.getValue(j);
				double maxLeft = model.getRanges().get(j).getMax();
				double minLeft = model.getRanges().get(j).getMin();
				double prozLeft = (100/(maxLeft-minLeft))*(valLeft-minLeft);
				int coordYLeft = (int) ((((max-min)/100)*prozLeft)+min);
				
				double valRight = d.getValue(j+1);
				double maxRight = model.getRanges().get(j+1).getMax();
				double minRight = model.getRanges().get(j+1).getMin();
				double prozRight = (100/(maxRight-minRight))*(valRight-minRight);
				int coordYRight = (int) ((((max-min)/100)*prozRight)+min);
				
				g2D.drawLine(padding+j*coordWidth, coordYLeft, padding+(j+1)*coordWidth, coordYRight);
			}
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
