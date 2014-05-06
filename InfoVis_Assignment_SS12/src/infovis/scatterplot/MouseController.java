package infovis.scatterplot;

import infovis.debug.Debug;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;

public class MouseController implements MouseListener, MouseMotionListener {

	private Model model = null;
	private View view = null;
	private double x = 0.0, y = 0.0;
	private double w = 0.0, h = 0.0;

	public void mouseClicked(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent arg0) {
		x = arg0.getX();
		y = arg0.getY();
		if(w<x && h<y) {
			view.getMarkerRectangle().setRect(w,h,x-w,y-h);
		}
		else if (w<x){
			view.getMarkerRectangle().setRect(w,y,x-w,h-y);
		}
		else if (h<y){
			view.getMarkerRectangle().setRect(x,h,w-x,y-h);
		}
		else {
			view.getMarkerRectangle().setRect(x,y,w-x,h-y);
		}
	}

	public void mouseReleased(MouseEvent arg0) {
		int anzahl_rect = model.getRanges().size();
		int width_rect = 0;
		if(view.getWidth()<view.getHeight()) {
			width_rect = (int)Math.floor((view.getWidth()-view.getPadding())/anzahl_rect);
		}
		else {
			width_rect = (int)Math.floor((view.getHeight()-view.getPadding())/anzahl_rect);
		}
		
		int rectX = (int) ((x-view.getPadding())/width_rect);
		int rectY = (int) ((y-view.getPadding())/width_rect);
//		Debug.println(String.valueOf(posX));
//		Debug.println(String.valueOf(posY));
		
		for (Data d : model.getList()) {
			double minX = model.getRanges().get(rectX).getMin()-1.0;
			double maxX = model.getRanges().get(rectX).getMax()+1.0;
			double absX = maxX-minX;
			double valX = d.getValue(rectX);
			double prozX = (100.0/absX)*(valX-minX);
			double posX = width_rect/100.0*prozX;
			
			double minY = model.getRanges().get(rectY).getMin()-1.0;
			double maxY = model.getRanges().get(rectY).getMax()+1.0;
			double absY = maxY-minY;
			double valY = d.getValue(rectY);
			double prozY = (100.0/absY)*(valY-minY);
			double posY = width_rect/100.0*prozY;
			
			boolean inBox = 
					x   <= view.getPadding()+width_rect*rectX+posX   &&
					x+w >= view.getPadding()+width_rect*rectX+posX+2.0 &&
					y   <= view.getPadding()+width_rect*rectY+posY   &&
					y+h >= view.getPadding()+width_rect*rectY+posY+2.0;
			if(inBox) {
				d.setColor(Color.red);
			}
			else {
				d.setColor(Color.black);
			}
			view.repaint();
		}
		
	}

	public void mouseDragged(MouseEvent arg0) {
		w = arg0.getX();
		h = arg0.getY();
		if(w<x && h<y) {
			view.getMarkerRectangle().setRect(w,h,x-w,y-h);
		}
		else if (w<x){
			view.getMarkerRectangle().setRect(w,y,x-w,h-y);
		}
		else if (h<y){
			view.getMarkerRectangle().setRect(x,h,w-x,y-h);
		}
		else {
			view.getMarkerRectangle().setRect(x,y,w-x,h-y);
		}
		view.repaint();
	}

	public void mouseMoved(MouseEvent arg0) {
	}

	public void setModel(Model model) {
		this.model  = model;	
	}

	public void setView(View view) {
		this.view  = view;
	}

}
