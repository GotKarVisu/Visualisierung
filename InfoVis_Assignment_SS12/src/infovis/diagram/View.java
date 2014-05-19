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
	private double xMin = 0.0, yMin = 0.0;
	private double ratio = 0.3;

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
		
		marker.setRect(xMin, yMin, getWidth()*ratio/scale, getHeight()*ratio/scale);
		overviewRect.setRect(0,0,getBounds().width*ratio,getBounds().height*ratio);
		
		if(marker.getMaxX() > overviewRect.getMaxX()) {
			Rectangle2D rect = marker.getBounds2D();
			double max_rect = rect.getX() - (rect.getMaxX() - overviewRect.getMaxX());
			marker.setRect(max_rect,rect.getMinY(),rect.getWidth(),rect.getHeight());
		}
		else if(marker.getMinX() < overviewRect.getMinX()) {
			Rectangle2D rect = marker.getBounds2D();
			double min_rect = rect.getX() - (rect.getMinX() - overviewRect.getMinX());
			marker.setRect(min_rect,rect.getMinY(),rect.getWidth(),rect.getHeight());
		}
		if(marker.getMaxY() > overviewRect.getMaxY()) {
			Rectangle2D rect = marker.getBounds2D();
			double max_rect = rect.getY() - (rect.getMaxY() - overviewRect.getMaxY());
			marker.setRect(rect.getMinX(),max_rect,rect.getWidth(),rect.getHeight());
		}
		else if(marker.getMinY() < overviewRect.getMinY()) {
			Rectangle2D rect = marker.getBounds2D();
			double max_rect = rect.getY() - (rect.getMinY() - overviewRect.getMinY());
			marker.setRect(rect.getMinX(),max_rect,rect.getWidth(),rect.getHeight());
		}
		
		g2D.draw(marker);
		g2D.draw(overviewRect);
		g2D.scale(ratio, ratio);
		paintDiagram(g2D);
		g2D.scale(scale/ratio, scale/ratio);
		updateTranslation((overviewRect.getMinX()-marker.getMinX())*1/ratio, (overviewRect.getMinY()-marker.getMinY())*1/ratio);
		g2D.translate(translateX, translateY);
		paintDiagram(g2D);
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
	public void markerMin(double x,double y)
	{
		xMin = x;
		yMin = y;
	}
}
 