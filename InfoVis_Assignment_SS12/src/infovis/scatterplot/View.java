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
			
			//draw the rectangles of the scatterplot
			print_scatterplotMatrix(g2D);
		}
		public void setModel(Model model) {
			this.model = model;
		}
		public void print_scatterplotMatrix(Graphics2D g) {
			int anzahl_rect = model.getRanges().size();
			double padding = 50;
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
					Rectangle2D rect = new Rectangle2D.Double(hor,ver,width_rect,width_rect);
					g.draw(rect);
					hor += width_rect;
				}
				hor=padding;
				ver += width_rect;
			}
		}
}
