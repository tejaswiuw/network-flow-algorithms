import graphCode.Edge;
import graphCode.Vertex;

public class VertexEdge {
	private Vertex vertex;
	private Edge edge;
	
	public VertexEdge(Vertex v, Edge e) {
		vertex = v;
		edge = e;
	}
	
	public Vertex getVertex() {
		return vertex;
	}
	
	public Edge getEdge() {
		return edge;
	}
}
