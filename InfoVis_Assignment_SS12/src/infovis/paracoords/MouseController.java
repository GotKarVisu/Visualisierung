package infovis.paracoords;

import infovis.scatterplot.Model;
import infovis.scatterplot.Data;

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
		//Schleife über Alle Objekte
		int coordWidth = (view.getWidth()-view.GetPadding())/model.getDim();
		for(int i=0; i<model.getList().size(); ++i) {
			Data d = model.getList().get(i);
			boolean select = false;
			//Schleife über alle Values eines Objekts			
			for(int j=0; j<model.getDim()-1; ++j) {
				double max = view.GetPadding()+10;
				double min = view.getHeight()-view.GetPadding()-10;
				
				double maxP = model.getRanges().get(j).getMax();
				double minP = model.getRanges().get(j).getMin();
				double proz = (100/(maxP-minP))*(d.getValue(j)-minP);
				int coordY = (int) ((((max-min)/100)*proz)+min);
				int coordX = (int) view.GetPadding()+j*coordWidth;
				
				boolean inBox = 	x <= coordX && (x+w) >= coordX &&
									y <= coordY && (y+h) >= coordY;
				
				if(inBox) select = true;
			}
			if(select) d.setColor(Color.red);
			else	   d.setColor(Color.black);
		}
		view.repaint();
	}

	public void mouseDragged(MouseEvent e) {
		w = e.getX()-x;
		h = e.getY()-y;

		view.getMarkerRectangle().setRect(x,y,w,h);
		view.repaint();
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
