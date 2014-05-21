package infovis.diagram.layout;

import infovis.debug.Debug;
import infovis.diagram.Model;
import infovis.diagram.View;
import infovis.diagram.elements.Edge;
import infovis.diagram.elements.Vertex;

import java.util.ArrayList;
import java.util.Iterator;

public class Fisheye implements Layout{
	public double d = 5;
	
	
	
	public int min(int a, int b) {
		if(a>=b) return b;
		return a;
	}
	
	
	public void setMouseCoords(int x, int y, View view) {}

	public Model transform(Model model, View view) {
		
		
		int PfocusX = view.getMouseX();
		int PfocusY = view.getMouseY();
		Model fisheyeModel = new Model();
		fisheyeModel.clone(model);
//		for(Edge e : model.getEdges()) {
//			fisheyeModel.addEdge(e);
//		}
//		for(Vertex v : model.getVertices()) {
//			fisheyeModel.addVertex(v);
//		}
		for(Vertex vertex: fisheyeModel.getVertices()){
			double PfishX = F1(vertex.getX(),PfocusX, view,true);
			double PfishY = F1(vertex.getY(),PfocusY, view,false);
			
			double QnormX = 0.0, QnormY = 0.0, QfishX = 0.0, QfishY = 0.0;
			QnormX = vertex.getX() + vertex.getWidth()/2.0;
			QnormY = vertex.getY() * vertex.getHeight()/2.0;
			
			QfishX = F1(QnormX, PfocusX, view,true);
			QfishY = F1(QnormY, PfocusY, view,false);
			
			double size = 2*Math.min(Math.abs(QfishX-PfishX), Math.abs(QfishY-PfishY));
			vertex.setWidth(size);
			vertex.setHeight(size/3);
			
			vertex.setX(PfishX);
			vertex.setY(PfishY);
		}
		view.setModel(fisheyeModel);
		return fisheyeModel;
	}
	public double G(double x) {
		return (((d+1)*x)/(d*x+1));
	}
	public double Pfey(double Pnorm, double Dmax, double Pfocus) {
		return G(Pnorm/Dmax)*Dmax+Pfocus;
	}
	public double F1(double X, double focus, View view, boolean w) {
		double Dmax = 0.0;
		
		if(X > focus) {
			if(w) {
				Dmax = view.getBounds().width - focus;
			}
			else {
				Dmax = view.getBounds().height - focus;
			}
		}
		else {
			Dmax = 0 - focus;
		}
		double Dnorm = X - focus;
		double Pfish = Pfey(Dnorm,Dmax,focus);
		return Pfish;
	}
}