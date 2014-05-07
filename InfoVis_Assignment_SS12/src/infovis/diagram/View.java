package infovis.diagram;

import infovis.diagram.elements.Element;
import infovis.diagram.elements.Vertex;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

public class View extends JPanel{
	private Model model = null;
	private Color color = Color.BLUE;
	private double scale = 1;
	private double translateX = 0.0;
	private double translateY = 0.0;
	private double markerOriginX = 0.0;
	private double markerOriginY = 0.0;
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
			
		Graphics2D g2D = (Graphics2D) g;
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2D.clearRect(0, 0, getWidth(), getHeight());
		g2D.scale(scale, scale);
		
//		if(translateX-marker.getWidth()/2 > 0) {
//			if(translateX+marker.getWidth()/2 > overviewRect.getMaxX()) {
//				g2D.translate(-(overviewRect.getMaxX()-marker.getWidth()), 0);
//			}
//			else {
//				g2D.translate(-translateX+marker.getWidth()/2, 0);
//			}
//		}
//		if(translateY-marker.getHeight()/2 > 0) {
//			if(translateY+marker.getHeight()/2 > overviewRect.getMaxY()) {
//				g2D.translate(0, -(overviewRect.getMaxY()-marker.getHeight()));
//			}
//			else {
//				g2D.translate(0, -translateY+marker.getHeight()/2);
//			}
//		}		
		
		paintDiagram(g2D);
		paintNavigation(g2D);		
	}
	private void paintDiagram(Graphics2D g2D){
		for (Element element: model.getElements()){
			element.paint(g2D);
		}
	}
	
	private void paintNavigation(Graphics2D g2D) {
		// OVERVIEW
		double maxWidth = 0.0;
		double maxHeight = 0.0;
		for (Element element: model.getElements()){
			if(maxWidth < element.getX())
				maxWidth = element.getX();
			if(maxHeight < element.getY())
				maxHeight = element.getY();
		}
		maxWidth += Vertex.STD_WIDTH;
		maxHeight += Vertex.STD_HEIGHT;
		
		g2D.scale((1/scale)*.3, (1/scale)*.3);
		overviewRect.setRect(0, 0, maxWidth, maxHeight);
		g2D.setColor(Color.white);
		g2D.fillRect(0, 0, (int)maxWidth, (int)maxHeight);
		g2D.setColor(Color.red);
		paintDiagram(g2D);
		g2D.draw(overviewRect);

		// MARKER
		
		marker.setRect(markerOriginX, markerOriginY, maxWidth/scale, maxHeight/scale);
//		if(translateX-marker.getWidth()/2 > 0 ) {
//			if(translateX+marker.getWidth()/2 > overviewRect.getMaxX()) {
//				g2D.translate(overviewRect.getMaxX()-marker.getWidth(), 0); //move marker
//			}
//			else {
//				g2D.translate(translateX-marker.getWidth()/2, 0); //move marker
//			}
//		}
//		if(translateY-marker.getHeight()/2 > 0) {
//			if(translateY+marker.getHeight()/2 > overviewRect.getMaxY()) {
//				g2D.translate(0, overviewRect.getMaxY()-marker.getHeight()); //move marker
//			}
//			else {
//				g2D.translate(0, translateY-marker.getHeight()/2); //move marker
//			}
//		}
		g2D.draw(marker);
		
		
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
 