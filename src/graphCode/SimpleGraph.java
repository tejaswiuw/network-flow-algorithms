/*
 * Written by Ed Hong UWT Feb. 17, 2003.
 * Modified by Donald Chinn May 14, 2003.
 * Modified by Donald Chinn December 11, 2003.
 */
package graphCode;

import java.util.*;

/**
 * A class that represents a graph.
 * 
 * @author edhong
 * @version 0.0
 */
public class SimpleGraph {

    LinkedList vertexList;
    LinkedList edgeList;

    // Constructor
    public SimpleGraph() {
        this.vertexList = new LinkedList();
        this.edgeList = new LinkedList();
    }
    
    /**
     * Return the vertex list of this graph.
     * @returns  vertex list of this graph
     */
    public Iterator vertices() {
        return vertexList.iterator();
    }

    /**
     * Return the edge list of this graph.
     * @returns  edge list of this graph
     */
    public Iterator edges() {
        return edgeList.iterator();
    }

    /**
     * Given a vertex, return an iterator to the edge list of that vertex
     * @param v  a vertex
     * @returns  an iterator to the edge list of that vertex
     */
    public Iterator incidentEdges(Vertex v) {
        return v.incidentEdgeList.iterator();
    }

    /**
     * Return an arbitrary vertex of this graph
     * @returns  some vertex of this graph
     */
    public Vertex aVertex() {
        if (vertexList.size() > 0)
            return (Vertex) vertexList.getFirst();
        else
            return null;
    }

    /**
     * Add a vertex to this graph.
     * @param data  an object to be associated with the new vertex
     * @param name  a name to be associated with the new vertex
     * @returns  the new vertex
     */
    public Vertex insertVertex(Object data, Object name) {
        Vertex v;
        v = new Vertex(data, name);
        vertexList.addLast(v);
        return v;
    }

    /**
     * Add an edge to this graph.
     * @param v  the first endpoint of the edge
     * @param w  the second endpoint of the edge
     * @param data  data to be associated with the new edge
     * @param name  name to be associated with the new edge
     * @returns  the new edge
     */
    public Edge insertEdge(Vertex v, Vertex w, Object data, Object name) {
        Edge e;
        e = new Edge(v, w, data, name);
        edgeList.addLast(e);
        v.incidentEdgeList.addLast(e);
        w.incidentEdgeList.addLast(e);
        return e;
    }

    /**
     * Given a vertex and an edge, if the vertex is one of the endpoints
     * of the edge, return the other endpoint of the edge.  Otherwise,
     * return null.
     * @param v  a vertex
     * @param e  an edge
     * @returns  the other endpoint of the edge (or null, if v is not an endpoint of e)
     */
    public Vertex opposite(Vertex v, Edge e) {
        Vertex w;
        
        if (e.getFirstEndpoint() == v) {
            w= e.getSecondEndpoint();
        }
        else if (e.getSecondEndpoint() == v) {
            w = e.getFirstEndpoint();
        }
        else
            w = null;
        
        return w;
    }
    
    /**
     * Return the number of vertices in this graph.
     * @returns  the number of vertices
     */
    public int numVertices() {
        return vertexList.size();
    }

    /**
     * Return the number of edges in this graph.
     * @returns  the number of edges
     */
    public int numEdges() {
        return edgeList.size();
    }

    /**
     * Code to test the correctness of the SimpleGraph methods.
     */
    public static void main(String[] args) {
        // create graph a----b-----c,
        //                X     Y
        // X and Y are objects stored at edges. .

        // All Objects stored will be strings.

        SimpleGraph G = new SimpleGraph();
        Vertex r, s, t, u, v, w, f, g, h;
        Edge e1, e2, e3, e4, e5, e6, x1, x2, x3, x4, x5, x6;
        
        r = G.insertVertex(null, "f");
        f = r;
        s = G.insertVertex(null, "g");
        g = s;
        t = G.insertVertex(null, "h");
        h = t;
 /*       u = G.insertVertex(null, "i");
        i = u;
        v = G.insertVertex(null, "j");
        j = v;
        w = G.insertVertex(null, "k");
        k = w;
 */       
        e1 = G.insertEdge(f, g, 16, "X1");
        x1 = e1;
        e2 = G.insertEdge(f, h, 13, "X2");
        x2 = e2;
        e3 = G.insertEdge(g, h, 10, "X3");
        x3 = e3;
        e4 = G.insertEdge(h, g, 4, "X4");
        x4 = e4;
        
  /*      
        v = G.insertVertex(null, "a");
        a = v;
        w = G.insertVertex(null, "b");
        b = w;
        e = G.insertEdge(v, w, null, "X");
        x = e;
        v = G.insertVertex(null, "c");
        c = v;
        e = G.insertEdge(w, v, null, "Y");
        y = e;
*/
        Iterator i;
        Edge e;
        System.out.println("Iterating through vertices...");
        for (i= G.vertices(); i.hasNext(); ) {
            v = (Vertex) i.next();
            System.out.println("found vertex " + v.getName());
        }

        System.out.println("Iterating through adjacency lists...");
        for (i= G.vertices(); i.hasNext(); ) {
            v = (Vertex) i.next();
            System.out.println("Vertex "+v.getName());
            Iterator j;
            
            for (j = G.incidentEdges(v); j.hasNext();) {
                e = (Edge) j.next();
                System.out.println("  found edge " + e.getName());
            }
        }

        System.out.println("Testing opposite...");
        System.out.println("aXbYc is ");
        System.out.println(f);
        System.out.println(x1);
        System.out.println(g);
        System.out.println(h);
        System.out.println(x2);

        System.out.println("opposite(a,x) is " + G.opposite(f,x1).getName()+" Value "+x1.getData());
 /*       System.out.println("opposite(a,y) is " + G.opposite(a,y));
        System.out.println("opposite(b,x) is " + G.opposite(b,x));
        System.out.println("opposite(b,y) is " + G.opposite(b,y));
        System.out.println("opposite(c,x) is " + G.opposite(c,x));
        System.out.println("opposite(c,y) is " + G.opposite(c,y));
*/
    }
}




