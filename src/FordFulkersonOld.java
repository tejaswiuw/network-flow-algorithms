//package fordfulkersonscaling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

//import graph.Edge;
//import graph.SimpleGraph;
//import graph.Vertex;
import graphCode.*;

/**
 * <b>FordFulkerson algorithm</b> is used to compute the max flow of a network.
 * This implementation of F-F algorithm exposes a method to
 * {@link #calculateMaxFlow(SimpleGraph)} which returns the max flow of the
 * input graph.
 * 
 * The algorithm is primarily divided into three parts:
 * 
 * <ul>
 * <li>Creating the residual graph {@link #createResidualGraph(SimpleGraph)}
 * <li>Finding an augmenting path from source to sink in residual graph
 * {@link #hasPathFromSourceToSink(SimpleGraph, Map)}
 * <li>Adding additional flow to the augmenting path
 * {@link #addFlowToAugmentedPath(SimpleGraph, Vertex, Vertex, Map, double)}
 * </ul>
 * 
 * 
 */
public class FordFulkersonT {

	/**
	 * This method takes in a graph as input and returns the max flow of the
	 * input graph using F-F algorithm. This method also measures the time taken
	 * in milliseconds to compute the max flow
	 * 
	 * @param G
	 *            a {@link SimpleGraph}
	 * @return max flow of G
	 * @throws Exception
	 */
	public double calculateMaxFlow(SimpleGraph G) throws Exception {

		long start = System.currentTimeMillis();
		try {
			// initialize the edge data of the input graph
			computeEdgeData(G);

			// compute the initial residual graph
			SimpleGraph residualGraph = createResidualGraph(G);

			Map<Vertex, Edge> nodeToParent = new HashMap<Vertex, Edge>();
			int maxFlow = 0;

			// loop until a path exists from source to sink in the residual
			// graph
			while (hasPathFromSourceToSink(residualGraph, nodeToParent)) {
				// source Vertex
				Vertex source = findVertexByName(residualGraph, "s");

				// sink Vertex
				Vertex sink = findVertexByName(residualGraph, "t");

				// computing the bottleneck
				double bottleneck = computeBottleneck(residualGraph, source, sink, nodeToParent);

				// augmenting flow to the network
				addFlowToAugmentedPath(residualGraph, source, sink, nodeToParent, bottleneck);

				nodeToParent.clear();
				maxFlow += bottleneck;

				// computing the new residual graph
				residualGraph = createResidualGraph(residualGraph);
			}

			// returns the max flow of the network
			return maxFlow;
		} finally {
			System.out.println("Total time taken to calculate max flow using Ford-Fulkerson Algorithm = " + (System.currentTimeMillis() - start) + " milliseconds");
		}
	}

	/**
	 * This method computes the bottleneck capacity available in the augmenting
	 * path. This is additional amount of flow that can be augmented to the
	 * network flow
	 * 
	 * @param residualGraph
	 * @param source
	 * @param sink
	 * @param nodeToParent
	 * @return bottleneck
	 */
	private double computeBottleneck(SimpleGraph residualGraph, Vertex source, Vertex sink, Map<Vertex, Edge> nodeToParent) {
		double bottleneck = Integer.MAX_VALUE;

		Vertex curr = sink;
		List<String> augmentedPath = new ArrayList<String>();

		// scan each edge on the path from source to sink and find the bottle
		// neck (edge with least available flow capacity)
		while (curr != source) {
			augmentedPath.add(curr.getName().toString());
			Vertex parent = residualGraph.opposite(curr, nodeToParent.get(curr));

			Edge e = nodeToParent.get(curr);
			EdgeData edgeData = (EdgeData) e.getData();

			// if this edges available capacity is less than the current
			// bottleneck, update the bottleneck to current
			// edges available capacity
			if (edgeData.getEdgeCapacity() - edgeData.getEdgeFlow() < bottleneck) {
				bottleneck = edgeData.getEdgeCapacity() - edgeData.getEdgeFlow();
			}
			curr = parent;
		}
		augmentedPath.add(source.getName().toString());

		// Uncomment the line below to print the computed augmented path
		// printAugmentedPath(augmentedPath);

		return bottleneck;
	}

	/**
	 * Adds additional amount of flow through the network and updates the
	 * available capacity of each edge along the augmenting path
	 * 
	 * @param residualGraph
	 *            graph for which capacity & flow are to be adjusted
	 * @param source 
	 * @param sink
	 * @param nodeToParent
	 * @param bottleneck
	 * @throws Exception
	 */
	private void addFlowToAugmentedPath(SimpleGraph residualGraph, Vertex source, Vertex sink, Map<Vertex, Edge> nodeToParent, double bottleneck) throws Exception {
		Vertex curr = sink;
		while (curr != source) {
			Vertex parent = residualGraph.opposite(curr, nodeToParent.get(curr));
			Edge e = nodeToParent.get(curr);
			EdgeData edgeData = (EdgeData) e.getData();
			if (edgeData.isBackedge()) {
				Edge forwardEdge = findForwardEdge(residualGraph, curr, e);
				EdgeData forwardEdgeData = (EdgeData) forwardEdge.getData();
				forwardEdgeData.setEdgeFlow(forwardEdgeData.getEdgeFlow() - bottleneck);
			} else {
				edgeData.setEdgeFlow(edgeData.getEdgeFlow() + bottleneck);
			}
			curr = parent;
		}
	}

	/**
	 * This is a method that prints out the augmenting path after each iteration
	 * of the F-F algorithm. This method is used for analysis and debugging
	 * purpose only.
	 * 
	 * @param augmentedPath
	 */
	private void printAugmentedPath(List<String> augmentedPath) {

		StringBuilder sb = new StringBuilder();
		for (int i = augmentedPath.size() - 1; i >= 0; i--) {
			sb.append(augmentedPath.get(i));
			sb.append(" -> ");
		}
		System.out.println("Augmented path: " + sb.substring(0, sb.length() - 4));
	}

	/**
	 * A method to find the forward edge of the residual graph given a backward
	 * edge
	 * 
	 * @param residualGraph
	 * @param curr
	 * @param backedge
	 * @return forward edge
	 */
	private Edge findForwardEdge(SimpleGraph residualGraph, Vertex curr, Edge backedge) {
		Iterator<Edge> edges = residualGraph.incidentEdges(curr);
		while (edges.hasNext()) {
			Edge e = edges.next();
			if (e.getFirstEndpoint() == backedge.getSecondEndpoint() && e.getSecondEndpoint() == backedge.getFirstEndpoint()) {
				return e;
			}
		}
		return null;
	}

	/**
	 * This method computeEdgeData is called to initialize the edge data of the
	 * graph with edge flow and edge capacity
	 * 
	 * @param G
	 *            input Graph
	 */
	private void computeEdgeData(SimpleGraph G) {

		Iterator<Edge> edgeIter = G.edges();
		while (edgeIter.hasNext()) {
			Edge e = edgeIter.next();
			EdgeData ed = new EdgeData();
			double capacity = Double.parseDouble(e.getData().toString());
			ed.setEdgeCapacity(capacity);
			e.setData(ed);
		}
	}

	/**
	 * Creates a residual graph based on the flow of the network. This method
	 * will add backward edges to the graph under certain conditions.
	 * 
	 * @param G
	 *            input graph
	 * @return Residual graph
	 * @throws Exception
	 */
	private SimpleGraph createResidualGraph(SimpleGraph G) throws Exception {
		SimpleGraph residualGraph = new SimpleGraph();
		Map<String, Vertex> nameVertexMap = new HashMap<String, Vertex>();

		Iterator<Vertex> vertexIter = G.vertices();
		while (vertexIter.hasNext()) {
			Vertex v = (Vertex) vertexIter.next();
			Vertex vResidual = residualGraph.insertVertex(v.getData(), v.getName());
			nameVertexMap.put(v.getName().toString(), vResidual);
		}

		Iterator<Edge> edgeIter = G.edges();
		while (edgeIter.hasNext()) {
			Edge e = (Edge) edgeIter.next();
			EdgeData ed = (EdgeData) e.getData();
			if (ed.isBackedge()) {
				// if the edge is a backedge, then continue
				continue;
			}

			if (ed.getEdgeFlow() > 0) {
				// if the forward edge has flow greater than 0, then create a
				// backwardedge with capacity equal to the forward edge's flow
				EdgeData backEdgeData = new EdgeData();
				backEdgeData.setEdgeCapacity(ed.getEdgeFlow());
				backEdgeData.setEdgeFlow(0);
				backEdgeData.setBackedge(true);
				// adding backwardedge to the residual graph
				residualGraph.insertEdge(nameVertexMap.get(e.getSecondEndpoint().getName().toString()), nameVertexMap.get(e.getFirstEndpoint().getName().toString()), backEdgeData,
						e.getName());
			}

			EdgeData forwardEdgeData = new EdgeData();
			forwardEdgeData.setEdgeCapacity(ed.getEdgeCapacity());
			forwardEdgeData.setEdgeFlow(ed.getEdgeFlow());

			// add forward edge to the residual graph
			residualGraph.insertEdge(nameVertexMap.get(e.getFirstEndpoint().getName().toString()), nameVertexMap.get(e.getSecondEndpoint().getName().toString()), forwardEdgeData,
					e.getName());
		}
		return residualGraph;
	}

	/**
	 * Returns the reference to the vertex in the graph given its name
	 * 
	 * @param G
	 *            Graph
	 * @param name
	 *            name of the vertex
	 * @return reference to the vertex instance
	 */
	private Vertex findVertexByName(SimpleGraph G, String name) {
		Iterator<Vertex> vertexIter = G.vertices();
		while (vertexIter.hasNext()) {
			Vertex v = (Vertex) vertexIter.next();
			if (v.getName().equals(name)) {
				return v;
			}
		}
		return null;
	}

	/**
	 * This method uses <b>Breadth-First-Search algorithm</b> to check if a path
	 * exists from source to sink in the residual graph. If a path exists, this
	 * method returns true and also update the node to parent to trace the path
	 * from source to sink. If this method returns false, the F-F algorithm
	 * terminates and the current calculated flow will be the max flow of the
	 * network
	 * 
	 * @param residualGraph
	 * @param nodeToParent
	 * @return
	 */
	private boolean hasPathFromSourceToSink(SimpleGraph residualGraph, Map<Vertex, Edge> nodeToParent) {
		Set<String> visitedVertices = new HashSet<String>();
		Queue<Vertex> queue = new LinkedList<Vertex>();
		Vertex source = findVertexByName(residualGraph, "s");
		Vertex sink = findVertexByName(residualGraph, "t");

		if (source != null) {
			// add source as the starting point for BFS traversal
			queue.offer(source);
			visitedVertices.add(source.getName().toString());
			while (!queue.isEmpty()) {
				Vertex v = queue.poll();

				if (v.getName().toString().equals(sink.getName().toString())) {
					// Stop the BFS traversal if the sink is already found
					break;
				}
				Iterator<Edge> edges = residualGraph.incidentEdges(v);

				while (edges.hasNext()) {
					Edge e = (Edge) edges.next();
					if (e.getFirstEndpoint() == v && !visitedVertices.contains(e.getSecondEndpoint().getName().toString())) {
						EdgeData ed = (EdgeData) e.getData();
						// proceed only if the edge has capacity for more flow
						// i.e. (capacity - flow) should be greater than 0
						if (ed.getEdgeCapacity() - ed.getEdgeFlow() > 0) {
							nodeToParent.put(e.getSecondEndpoint(), e);
							queue.offer(e.getSecondEndpoint());
							visitedVertices.add(e.getSecondEndpoint().getName().toString());
						}
					}
				}
			}
		}

		if (visitedVertices.contains(sink.getName().toString())) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		FordFulkersonT ffAlgm = new FordFulkersonT();

		SimpleGraph G = constructGraph();
		double maxFlow = -1;
		try {
			maxFlow = ffAlgm.calculateMaxFlow(G);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Max flow of the given graph is " + maxFlow);
	}

	/**
	 * Constructs a simple graph for testing the algorithm
	 * 
	 * @return
	 */
	private static SimpleGraph constructGraph() {
		SimpleGraph G = new SimpleGraph();
		Vertex source = G.insertVertex("s", "s");
		Vertex sink = G.insertVertex("t", "t");

		Vertex a = G.insertVertex("a", "a");
		Vertex b = G.insertVertex("b", "b");
		Vertex c = G.insertVertex("c", "c");
		Vertex d = G.insertVertex("d", "d");

		G.insertEdge(source, a, 10, "sa");
		G.insertEdge(source, b, 10, "sb");
		G.insertEdge(a, d, 8, "ad");
		G.insertEdge(b, d, 9, "bd");
		G.insertEdge(d, c, 6, "dc");
		G.insertEdge(a, b, 2, "ab");
		G.insertEdge(a, c, 4, "ac");
		G.insertEdge(c, sink, 10, "ct");
		G.insertEdge(d, sink, 10, "dt");

		return G;
	}
}
