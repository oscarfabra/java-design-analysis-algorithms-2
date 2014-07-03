/**
 * $Id: Graph.java, v 1.1 3/06/14 20:42 oscarfabra Exp $
 * {@code Graph} Represents a directed graph with n vertices and m edges.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.2
 * @since 3/06/14
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a directed graph with n vertices and m edges.
 */
public class Graph
{
    //-------------------------------------------------------------------------
    // ATTRIBUTES
    //-------------------------------------------------------------------------

    // Number of vertices, n = V.size()
    private int n;

    // Number of edges, m = E.size()
    private int m;

    // Map of vertices with (vertexId, Vertex) pairs
    private Map<Integer,Vertex> V;

    // List of edges
    private Map<Integer,Edge> E;

    // Map of lists of vertices that indicates leaving edges of each vertex
    private Map<Integer,List<Integer>> vertexEdges;

    //-------------------------------------------------------------------------
    // CONSTRUCTORS
    //-------------------------------------------------------------------------

    /**
     * Creates a new graph from the given map of head vertices of each vertex.
     * @param n Number of vertices of the graph.
     * @param vertexEdges Map of Lists with the adjacent edges of each vertex.
     */
    public Graph(int n, Map<Integer, List<Edge>> vertexEdges)
    {
        // Initializes the map of vertices, O(n) algorithm
        this.V = new HashMap<Integer, Vertex>(n);
        System.out.print("-- Initializing list of vertices V...");
        for(int i = 0; i < n; i++)
        {
            this.V.put(i + 1, new Vertex(i + 1));
        }
        this.n = n;
        System.out.println("done.");

        // Initializes the list of edges and vertexEdges, O(m) algorithm
        this.E = new HashMap<Integer, Edge>(this.n * 2);
        this.vertexEdges = new HashMap<Integer, List<Integer>>(this.n);
        System.out.println("-- Initializing list of edges E...");
        for(Integer key : vertexEdges.keySet())
        {
            List<Edge> adjacentEdges = vertexEdges.get(key);
            for(Edge edge : adjacentEdges)
            {
                this.E.put(edge.getId(), edge);
                this.addVertexEdge(key, edge.getId());
            }

        }
        this.m = this.E.size();
        System.out.println("-- ...list of edges E initialized.");
    }

    /**
     * Copy constructor.
     * @param that Graph to copy attributes from.
     */
    public Graph(Graph that)
    {
        copy(that);
    }

    //-------------------------------------------------------------------------
    // CLASS METHODS
    //-------------------------------------------------------------------------

    /**
     * Extracts the corresponding adjacency lists to initialize a new Graph
     * from the given list of edges.
     * <b>Pre: </b>The input format must be a list of String, each in the form
     * "a b c" where a is the tail, b the head, and c the cost of each edge,
     * a, b in [1...n], c in Z (could be negative).
     * <br/>
     * @param m Number of edges to build.
     * @param edges List of String, each with the head and tail of an edge.
     * @return Map with the adjacent edges of each vertex.
     */
    public static Map<Integer, List<Edge>> buildVertexEdges(int m,
            List<String> edges)
    {
        // Walks through the given list of strings constructing the hashmap of
        // list of edges
        Map<Integer, List<Edge>> vertexEdges = new HashMap<Integer,
                List<Edge>>();
        int newEdgeId = 1;
        for(int i = 0; i < m; i++)
        {
            // Extracts and adds the corresponding pair (key, value of list) to
            // the hashmap. Each pair (key, list) in the hashmap is the id of a
            // vertex, and the list of adjacent edges that come out from it.
            String line = edges.get(i);
            String[] values = line.split(" ");
            int key = Integer.parseInt(values[0]);
            int value = Integer.parseInt(values[1]);
            int cost = Integer.parseInt(values[2]);
            Edge edge = new Edge(newEdgeId++, key, value, cost);
            Graph.addVertexEdge(vertexEdges, key, edge);

            // Message in standard output for logging purposes
            if((i + 1) % 20000 == 0)
            {
                System.out.println("---- "+(i + 1)+" endPoints stored.");
            }
        }
        return vertexEdges;
    }

    /**
     * Adds a key and value to the given map. Values are edges. The edge is
     * added to the chained list of the given key.
     * @param vertexEdges HashMAp to add a new Edge value to.
     * @param key Key to assign the new edge to.
     * @param newEdge Edge object to add to the given
     */
    private static void addVertexEdge(Map<Integer, List<Edge>> vertexEdges,
                                      int key, Edge newEdge)
    {
        // Removes the list of the given key, adds the value, and puts it
        // again
        List<Edge> adjacentEdges = vertexEdges.remove(key);
        if(adjacentEdges == null)
        {
            adjacentEdges = new ArrayList<Edge>();
        }
        adjacentEdges.add(newEdge);
        vertexEdges.put(key, adjacentEdges);
    }

    //-------------------------------------------------------------------------
    // PUBLIC METHODS
    //-------------------------------------------------------------------------

    /**
     * Copies the attributes from the given graph to this graph.
     * @param that Graph to copy attributes from.
     */
    public void copy(Graph that)
    {
        // Copies the list of vertices
        this.n = that.n;
        this.V = new HashMap<Integer, Vertex>(this.n);
        for(Integer key : that.V.keySet())
        {
            this.V.put(key, that.V.get(key));
        }

        // Copies the list of edges
        this.m = that.m;
        this.E = new HashMap<Integer, Edge>(this.m);
        for(Integer key : that.E.keySet())
        {
            this.E.put(key, that.E.get(key));
        }

        // Initializes the vertexEndpoints HashMap
        this.vertexEdges = new HashMap<Integer, List<Integer>>(this.n);
        for(Integer key : that.vertexEdges.keySet())
        {
            this.vertexEdges.put(key,that.vertexEdges.get(key));
        }
    }

    /**
     * Gets the number of vertices n of this graph.
     * @return The number of vertices n.
     */
    public int getN()
    {
        return this.n;
    }

    /**
     * Gets the number of edges m of this graph.
     * @return The number of edges m.
     */
    public int getM()
    {
        return this.m;
    }

    /**
     * Gets the vertex with the given id in V.
     * @param vertexId Id of the vertex to look for.
     * @return Vertex with the given id.
     */
    public Vertex getVertex(int vertexId)
    {
        if(!this.V.containsKey(vertexId))
        {
            return null;
        }
        return this.V.get(vertexId);
    }

    /**
     * Sets the vertex with the given id as explored in V.
     * @param vertexId Id of the vertex to look for.
     */
    public void setVertexAsExplored(int vertexId)
    {
        Vertex vertex = this.V.remove(vertexId);
        vertex.setExplored();
        this.V.put(vertexId, vertex);
    }

    /**
     * Returns a list with the vertices to where the vertex with the given id
     * points at.
     * @param vertexId Id of the vertex to look for.
     * @return List of vertices to where the vertex with the given id points
     * at.
     */
    public List<Vertex> getHeadVertices(int vertexId)
    {
        // Walks through the list of vertices head of the vertex with the
        // given id
        List<Vertex> headVertices = new ArrayList<Vertex>();
        for(Integer edgeId : this.vertexEdges.get(vertexId))
        {
            int headId = this.E.get(edgeId).getHead();
            headVertices.add(this.V.get(headId));
        }
        return headVertices;
    }

    //-------------------------------------------------------------------------
    // PRIVATE HELPER METHODS
    //-------------------------------------------------------------------------

    /**
     * Adds a new adjacent edge Id to the vertexEdges hashmap.
     * @param key Key to associate with the edgeId (id of a vertex in V).
     * @param edgeId Id of an adjacent edge to assign to the given key.
     */
    private void addVertexEdge(Integer key, int edgeId)
    {
        // Removes the list of the given key, adds the value, and puts it
        // again
        List<Integer> adjEdgesIds = vertexEdges.remove(key);
        if(adjEdgesIds == null)
        {
            adjEdgesIds = new ArrayList<Integer>();
        }
        adjEdgesIds.add(edgeId);
        vertexEdges.put(key, adjEdgesIds);
    }
}
