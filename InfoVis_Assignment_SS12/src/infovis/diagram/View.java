package infovis.diagram;

import infovis.diagram.elements.Element;
import infovis.diagram.elements.Vertex;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import javax.swing.JPanel;



public class View extends JPanel{
	private Model model = null;
	private Color color = Color.BLUE;
	private double scale = 1;
	private double translateX= 0;
	private double translateY=0;
	private Rectangle2D marker = new Rectangle2D.Double();
	private Rectangle2D overviewRect = new Rectangle2D.Double();   

	public Model getModel() {
		return model;
	}
	public void setModel(Model model) {
		this.model = model;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}

	
	public void paint(Graphics g) {
		
		// Scalefactor von Slider lesen
		AffineTransform trans = new AffineTransform();
		AffineTransform old = new AffineTransform();
		old.scale(1,1);
		trans.scale(scale, scale);
		
		Graphics2D g2D = (Graphics2D) g;
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2D.clearRect(0, 0, getWidth(), getHeight());
		g2D.setTransform(trans);
		
		//draw whole image
		paintDiagram(g2D);
		
		double maxWidth = 0.0, maxHeight = 0.0;
		for (Element element: model.getElements()){
			if(maxWidth < element.getX())
				maxWidth = element.getX();
			if(maxHeight < element.getY())
				maxHeight = element.getY();
		}		
		maxWidth += Vertex.STD_WIDTH;
		maxHeight += Vertex.STD_HEIGHT;

		//draw overview
		g2D.setTransform(old);
		g2D.scale(.3, .3);
		g2D.setColor(Color.white);
		g2D.fillRect(0, 0, (int)maxWidth, (int)maxHeight);
		g2D.setColor(Color.red);
		g2D.drawRect(0, 0, (int)maxWidth, (int)maxHeight);
		paintDiagram(g2D);
		marker.setRect(0,0,maxWidth/scale,maxHeight/scale);
		//marker.
		g2D.draw(marker);
		
	}
	private void paintDiagram(Graphics2D g2D){
		for (Element element: model.getElements()){
			element.paint(g2D);
		}	
		/*Iterator<Element> iter = model.iterator();
		while (iter.hasNext()) {
		  Element element =  iter.next();
		  element.paint(g2D);
		}*/
	}
	
	public void setScale(double scale) {
		this.scale = scale;
	}
	public double getScale(){
		return scale;
	}
	public double getTranslateX() {
		return translateX;
	}
	public void setTranslateX(double translateX) {
		this.translateX = translateX;
	}
	public double getTranslateY() {
		return translateY;
	}
	public void setTranslateY(double tansslateY) {
		this.translateY = tansslateY;
	}
	public void updateTranslation(double x, double y){
		setTranslateX(x);
		setTranslateY(y);
	}	
	public void updateMarker(int x, int y){
		marker.setRect(x, y, 16, 10);
	}
	public Rectangle2D getMarker(){
		return marker;
	}
	public boolean markerContains(int x, int y){
		return marker.contains(x, y);
	}
}
 