package infovis.paracoords;

import infovis.scatterplot.Model;

import java.awt.Color;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseController implements MouseListener, MouseMotionListener {
	private View view = null;
	private Model model = null;
	Shape currentShape = null;
	private int x = 0, y = 0;
	private int w = 0, h = 0;
	
	public void mouseClicked(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {
		x = e.getX();
		y = e.getY();
	}

	public void mouseReleased(MouseEvent e) {

//		int anzahl_rect = model.getRanges().size();
//		int width_rect = 0;
//		if(view.getWidth()<view.getHeight()) {
//			width_rect = (int)Math.floor((view.getWidth()-view.getPadding())/anzahl_rect);
//		}
//		else {
//			width_rect = (int)Math.floor((view.getHeight()-view.getPadding())/anzahl_rect);
//		}
//		
//		int rectX = (int) ((x-view.getPadding())/width_rect);
//		int rectY = (int) ((y-view.getPadding())/width_rect);
//		
//		for (Data d : model.getList()) {
//			double minX = model.getRanges().get(rectX).getMin()-1.0;
//			double maxX = model.getRanges().get(rectX).getMax()+1.0;
//			double absX = maxX-minX;
//			double valX = d.getValue(rectX);
//			double prozX = (100.0/absX)*(valX-minX);
//			double posX = width_rect/100.0*prozX;
//			
//			double minY = model.getRanges().get(rectY).getMin()-1.0;
//			double maxY = model.getRanges().get(rectY).getMax()+1.0;
//			double absY = maxY-minY;
//			double valY = d.getValue(rectY);
//			double prozY = (100.0/absY)*(valY-minY);
//			double posY = width_rect/100.0*prozY;
//			
//			boolean inBox = 
//				x     <= view.getPadding()+width_rect*rectX+posX   &&
//				(x+w) >= view.getPadding()+width_rect*rectX+posX+2 &&
//				y     <= view.getPadding()+width_rect*rectY+posY   &&
//				(y+h) >= view.getPadding()+width_rect*rectY+posY+2;
//			if(inBox) {
//				d.setColor(Color.red);
//			} else {
//				d.setColor(Color.black);
//			}
//			view.repaint();

	}

	public void mouseDragged(MouseEvent e) {
		w = e.getX()-x;
		h = e.getY()-y;

		getView().getMarkerRectangle().setRect(x,y,w,h);
		getView().repaint();
	}

	public void mouseMoved(MouseEvent e) {

	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

}
