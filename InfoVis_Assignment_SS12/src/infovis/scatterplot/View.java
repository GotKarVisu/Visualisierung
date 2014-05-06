package infovis.scatterplot;

import infovis.debug.Debug;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;

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
		printData(g2D);
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
		i = model.getRanges().size()-1;

		g.rotate(-(90*java.lang.Math.PI)/180);
		g.translate(-getHeight(), 0);
		for (String l : model.getLabels()) {
			g.drawString(l, (int) width_rect*i+3, (int) (padding-5)-12*(i%2));
			i--;
		}
		g.translate(getHeight(), 0);
		g.rotate((90*java.lang.Math.PI)/180);

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
	
	public void printData(Graphics2D g) {
		int anzahl_rect = model.getRanges().size();
		int width_rect = 0;
		if(getWidth()<getHeight()) {
			width_rect = (int)Math.floor((getWidth()-padding)/anzahl_rect);
		}
		else {
			width_rect = (int)Math.floor((getHeight()-padding)/anzahl_rect);
		}
		for (Data d : model.getList()) {
			double val = d.getValue(0);
			double proz = (100.0/40.0)*(val-1970.0);
			Debug.println(String.valueOf(proz));
			double pos = width_rect/100.0*proz;
			g.drawRect((int)(padding+pos), (int)(padding+pos), 5, 5);
		}
	}
}
