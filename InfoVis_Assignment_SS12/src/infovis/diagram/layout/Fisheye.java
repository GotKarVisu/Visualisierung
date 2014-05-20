package infovis.diagram.layout;

import infovis.debug.Debug;
import infovis.diagram.Model;
import infovis.diagram.View;
import infovis.diagram.elements.Edge;
import infovis.diagram.elements.Vertex;

import java.util.ArrayList;
import java.util.Iterator;

public class Fisheye implements Layout{
	private double d = 5.0;
	private double PnormX = 0.0, PnormY = 0.0;
	private double DnormX = 0.0, DnormY = 0.0;
	private double DmaxX = 0.0, DmaxY = 0.0;
	private double PboundaryX = 0.0, PboundaryY = 0.0;
	
	private double GetD() { return d; }
	private double GetPnormX() { return PnormX; }
	private double GetPnormY() { return PnormY; }
	private double GetDnormX() { return DnormX; }
	private double GetDnormY() { return DnormY; }
	private double GetDmaxX() { return DmaxX; }
	private double GetDmaxY() { return DmaxY; }
	private double GetPboundaryX() { return PboundaryX; }
	private double GetPboundaryY() { return PboundaryY; }
	
	
	public int min(int a, int b) {
		if(a>=b) return b;
		return a;
	}
	
	
	public void setMouseCoords(int x, int y, View view) {
		
		
	}

	public Model transform(Model model, View view) {
		// TODO Aufgabe 4
		int absX = min(view.getMouseX(), (view.getWidth()-view.getMouseX()));
		int absY = min(view.getMouseY(), (view.getHeight()-view.getMouseY()));
		int abs = min(absY, absX);
		
		double DmaxX = 0;
		
		if(PnormX > view.getMouseX()) {
			DmaxX = PboundaryX - view.getMouseX();
		} else {
			DmaxX = 0 - view.getMouseX(); 
		}
		
		double PfishX = view.getMouseX() + G((GetDnormX()/GetDmaxX())) * DmaxX;
		double PfishY = view.getMouseY() + G((GetDnormY()/GetDmaxY())) * DmaxY;
		
		Model fisheyeModel = new Model();
		fisheyeModel.addEdges(new ArrayList(model.getEdges()));
		fisheyeModel.addVertices(new ArrayList(model.getVertices()));
		for(Vertex vertex: fisheyeModel.getVertices()){
			vertex.setWidth(100);
		}
		return fisheyeModel;
	}
	
	public double G(double x) {
		return (((GetD()+1)*x)/(GetD()*x+1));
	}
	
}


