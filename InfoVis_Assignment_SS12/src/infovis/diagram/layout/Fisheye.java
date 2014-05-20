package infovis.diagram.layout;

import infovis.debug.Debug;
import infovis.diagram.Model;
import infovis.diagram.View;
import infovis.diagram.elements.Edge;
import infovis.diagram.elements.Vertex;

import java.util.ArrayList;
import java.util.Iterator;

public class Fisheye implements Layout{
//	private double d = 5.0;
//	private double PnormX = 0.0, PnormY = 0.0;
//	private double PfocusX = 0.0, PfocusY = 0.0;
//	private double PboundaryX = 0.0, PboundaryY = 0.0;
//	
//	private double GetD() { return d; }
//	private double GetPnormX() { return PnormX; }
//	private double GetPnormY() { return PnormY; }
//	private double GetPfocusX() { return PfocusX; }
//	private double GetPfocusY() { return PfocusY; }
//	private double GetPboundaryX() { return PboundaryX; }
//	private double GetPboundaryY() { return PboundaryY; }
//	
	public void setMouseCoords(int x, int y, View view) {
		// TODO Aufgabe 4
	}

	public Model transform(Model model, View view) {
		// TODO Aufgabe 4
		
		Model fisheyModel=new Model();
		fisheyModel.addEdges(new ArrayList(model.getEdges()));
		fisheyModel.addVertices(new ArrayList(model.getVertices()));
		for(Vertex vertex: fisheyModel.getVertices()){
			vertex.setWidth(200);
			
		}
		
		
		
		return fisheyModel;
	}
	
//	public double func_G(double x) {
//		return (((GetD()+1)*x)/(GetD()*x+1));
//	}
//	
//	public double Dmax() {
//		if(GetPnormX() > GetPfocusX())
//			return (GetPboundaryX() - GetPfocusX());
//		return (0 - GetPfocusX());
//	}
//	
//	public double DnormX() {
//		return (GetPnormX() - GetPfocusX());
//	
//	}
	
	
	
	
	
	
}


