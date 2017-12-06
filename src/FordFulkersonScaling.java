import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import graphCode.Edge;
import graphCode.SimpleGraph;
import graphCode.Vertex;

/**
 * <b>FordFulkersonScaling algorithm</b> is used to compute the max flow of a
 * network considering scaling parameter while selecting the path. This
 * implementation of the algorithm exposes a method to
 * {@link #calculateMaxFlow(SimpleGraph)} which returns the max flow of the
 * input graph.
 * 
 */
public class FordFulkersonScaling {

	/**
	 * 
	 * Breadth First Search to find the path (s->t) with edge weights more than
	 * the min_weight provided.
	 * 
	 * @param parent
	 *            Map where the vertices & edges are stored
	 * @param G
	 *            graph to be traversed
	 * @param edge_min
	 *            Weight
	 * @return
	 */
	private boolean bfsWithScaling(HashMap<Vertex, VertexEdge> parent, SimpleGraph G, double edge_min) {
		Set<String> vSet = new LinkedHashSet<String>();
		LinkedList<Vertex> queue = new LinkedList<Vertex>();
		queue.add(G.aVertex());
		vSet.add((String) G.aVertex().getName());

		while (queue.size() != 0) {
			Vertex u = queue.poll();
			Edge e;
			Vertex v;
			Iterator<Edge> j = G.incidentEdges(u);
			while (j.hasNext()) {
				e = (Edge) j.next();
				v = G.opposite(u, e);

				if ((Double) e.getData() < edge_min) {
					// Look for the next available edge with weight more than or
					// equal to the edge_min
					continue;
				}
				if (!(vSet.contains(v.getName()))) {
					queue.add(v);
					vSet.add((String) v.getName());
					VertexEdge ve = new VertexEdge(u, e);
					parent.put(v, ve);
				}
			}
		}
		return (vSet.contains("t") == true);
	}

	/**
	 * Gets edge connecting v to w.
	 * 
	 * @param G
	 *            input graph
	 * @param v
	 *            start vertex
	 * @param w
	 *            end vertex
	 * @return Edge v->w if exists else null
	 */
	private Edge edgeBetweenVertices(SimpleGraph G, Vertex v, Vertex w) {
		Iterator<Edge> j = G.incidentEdges(w);
		while (j.hasNext()) {
			Edge e = (Edge) j.next();
			if (e.getFirstEndpoint().getName() == v.getName()) {
				return e;
			}
		}
		return null;
	}

	/**
	 * Calculates scaling parameter based on the edge weights.
	 * 
	 * @param graph
	 *            Graph for which the scaling parameter should be calculated.
	 * @return scaling parameter value for the graph
	 */
	private int calculateScalingParam(SimpleGraph graph) {
		// System.out.println("Calculating scaling parameter..");
		Iterator<Edge> it = graph.edges();
		ArrayList<Double> weights = new ArrayList<Double>();
		while (it.hasNext()) {
			weights.add((Double) it.next().getData());
		}
		return Integer.highestOneBit(Collections.max(weights).intValue());
	}

	/**
	 * This method takes in a graph as input and returns the max flow of the
	 * input graph using F-F-S algorithm. This method also measures the time
	 * taken in milliseconds to compute the max flow
	 * 
	 * @param G
	 *            a {@link SimpleGraph}
	 * @param vertexEdgeMap
	 *            Map of Vertices and corresponding Edges in the graph G
	 * @return max flow for G
	 */
	public int calculateMaxFlow(SimpleGraph G, Hashtable vertexEdgeMap) {
		long startTime = System.currentTimeMillis();
		HashMap<Vertex, VertexEdge> parent = new HashMap<Vertex, VertexEdge>();
		int max_flow = 0;
		// calculate the scaling parameter
		int scalingParam = calculateScalingParam(G);

		while (scalingParam >= 1) {
			// repeat the loop until a path exists from source to sink in G
			while (bfsWithScaling(parent, G, scalingParam)) {
				double path_flow = Integer.MAX_VALUE;
				Vertex end = (Vertex) vertexEdgeMap.get("t");
				Vertex start = (Vertex) vertexEdgeMap.get("s");
				// iterate from end to start and find the minimum
				// capacity of edges along the BFS path
				while (end.getName() != start.getName()) {
					VertexEdge parentVE = parent.get(end);

					Vertex parentVertex = parentVE.getVertex();
					Edge edge = parentVE.getEdge();
					path_flow = Math.min(path_flow, (double) edge.getData());
					end = parentVertex;
				}

				end = (Vertex) vertexEdgeMap.get("t");
				start = (Vertex) vertexEdgeMap.get("s");

				while (end.getName() != start.getName()) {
					VertexEdge parentVE = parent.get(end);
					Vertex parentVertex = parentVE.getVertex();
					Edge edge = parentVE.getEdge();
					Edge rgReverseEdge = edgeBetweenVertices(G, end, parentVertex);
					edge.setData((Double) edge.getData() - path_flow);
					if (rgReverseEdge == null) {
						rgReverseEdge = new Edge(end, parentVertex, path_flow, edge.getName());
					} else {
						rgReverseEdge.setData((Double) rgReverseEdge.getData() + path_flow);
					}
					end = parentVE.getVertex();
				}
				parent = new HashMap<Vertex, VertexEdge>();
				max_flow += path_flow;

			}
			scalingParam /= 2;
		}

		System.out.println("Total time taken to calculate max flow using Ford Fulkerson (with Scaling) Algorithm: " + (System.currentTimeMillis() - startTime) + " milliseconds");
		return max_flow;
	}
}
