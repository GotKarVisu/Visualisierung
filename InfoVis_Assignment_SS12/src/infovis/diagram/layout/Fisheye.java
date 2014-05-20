package infovis.diagram.layout;

import infovis.debug.Debug;
import infovis.diagram.Model;
import infovis.diagram.View;
import infovis.diagram.elements.Edge;
import infovis.diagram.elements.Vertex;

import java.util.ArrayList;
import java.util.Iterator;

public class Fisheye implements Layout{
	public double d = 2;
	
	
	
	public int min(int a, int b) {
		if(a>=b) return b;
		return a;
	}
	
	
	public void setMouseCoords(int x, int y, View view) {
		
		
	}

	public Model transform(Model model, View view) {
		
		int PfocusX = view.getMouseX();
		int PfocusY = view.getMouseY();
		
		int DmaxX = view.getBounds().width - PfocusX;
		int DmaxY = view.getBounds().height - PfocusY;
		
		Model fisheyeModel = new Model();
		fisheyeModel.addEdges(new ArrayList(model.getEdges()));
		fisheyeModel.addVertices(new ArrayList(model.getVertices()));
		for(Vertex vertex: fisheyeModel.getVertices()){
			
			int DnormX = (int) vertex.getX();
			int DnormY = (int) vertex.getY();
			
			int PfisheyeX = (int) G(DnormX/DmaxX)*DmaxX + PfocusX;
			int PfisheyeY = (int) G(DnormY/DmaxY)*DmaxY + PfocusY;		
			
			
			vertex.setX(PfisheyeX);
			vertex.setY(PfisheyeY);
		}
		return fisheyeModel;
	}
	
	public double G(double x) {
		return (((d+1)*x)/(d*x+1));
	}
	
}


