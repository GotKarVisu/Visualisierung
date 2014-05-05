package infovis.scatterplot;

import infovis.debug.Debug;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

public class View extends JPanel {
     private Model model = null;
     private Rectangle2D markerRectangle = new Rectangle2D.Double(0,0,0,0); 
     private double scale = 1;
     private double padding = 50;
	 public Rectangle2D getMarkerRectangle() {
		return markerRectangle;
	}
	
	@Override
	public void paint(Graphics g) {

        for (String l : model.getLabels()) {
			Debug.print(l);
			Debug.print(",  ");
			Debug.println("");
		}
		for (Range range : model.getRanges()) {
			Debug.print(range.toString());
			Debug.print(",  ");
			Debug.println("");
		}
		for (Data d : model.getList()) {
			Debug.print(d.toString());
			Debug.println("");
		}
		
		//create a Graphics Object
		Graphics2D g2D = (Graphics2D) g;
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2D.clearRect(0, 0, getWidth(), getHeight());
		g2D.scale(scale, scale);
		

		printLables(g2D);
		
		//draw the rectangles of the scatterplot
		printScatterplotMatrix(g2D);
	}
	public void setModel(Model model) {
		this.model = model;
	}
	public void printLables(Graphics2D g) {
		int anzahl_rect = model.getRanges().size();
		int width_rect = 0;
		if(getWidth()<getHeight()) {
			width_rect = (int)Math.floor((getWidth()-padding)/anzahl_rect);
		}
		else {
			width_rect = (int)Math.floor((getHeight()-padding)/anzahl_rect);
		}
		int i=0;
		for (String l : model.getLabels()) {
			g.drawString(l, (int) padding+width_rect*i+3, (int) (padding-5)-12*(i%2));
			i++;
		}
		
		/*g.drawString("Markteinf.", (int) padding, (int) (padding-5));
		g.drawString("Hubraum", (int) (padding+width_rect), (int) (padding-5));
		g.drawString("PS", (int) (padding+width_rect*2), (int) (padding-5));
		g.drawString("l/100km", (int) (padding+width_rect*3), (int) (padding-5));
		g.drawString("Vmax(km/h)", (int) (padding+width_rect*4), (int) (padding-5));
		g.drawString("Gewicht(t)", (int) (padding+width_rect*5), (int) (padding-5));
		g.drawString("Beschleunigung(0-100/s)", (int) (padding+width_rect*6), (int) (padding-5));
		
		g.drawString("Name", 10, 10);
		g.drawString("Markteinfuehrung", 10, 10);
		g.drawString("Hubraum", 10, 10);
		g.drawString("PS", 10, 10);
		g.drawString("l/100km", 10, 10);
		g.drawString("Vmax(km/h)", 10, 10);
		g.drawString("Gewicht(t)", 10, 10);
		g.drawString("Beschleunigung(0-100/s)", 10, 10);*/
	}
	public void printScatterplotMatrix(Graphics2D g) {
		int anzahl_rect = model.getRanges().size();
		double hor = padding, ver = padding;
		int width_rect = 0;
		if(getWidth()<getHeight()) {
			width_rect = (int)Math.floor((getWidth()-padding)/anzahl_rect);
		}
		else {
			width_rect = (int)Math.floor((getHeight()-padding)/anzahl_rect);
		}
		for(int i=0; i<anzahl_rect; ++i) {
			for(int j=0; j<anzahl_rect; ++j) {
				Rectangle2D rect = 
						new Rectangle2D.Double(hor,ver,width_rect,width_rect);
				g.draw(rect);
				hor += width_rect;
			}
			hor=padding;
			ver += width_rect;
		}
	}
}
