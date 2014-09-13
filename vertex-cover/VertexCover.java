/**
 * $Id: VertexCover.java, v1.0 6/09/14 10:06 PM oscarfabra Exp $
 * {@code VertexCover} solves the vertex cover problem for a given undirected
 * graph.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 6/09/14
 */

import java.util.ArrayList;
import java.util.List;

/**
 * VertexCover solves the vertex cover for a given undirected graph. The
 * problem is defined as follows:
 * Given: An undirected graph G = (V,E).
 * Goal: Compute a minimum-cardinality vertex cover (a set S subset of V that
 * contains at least one endpoint of each edge of G).
 */
public class VertexCover
{
    //-------------------------------------------------------------------------
    // ATTRIBUTES
    //-------------------------------------------------------------------------

    //-------------------------------------------------------------------------
    // CONSTRUCTORS
    //-------------------------------------------------------------------------

    private VertexCover() { }   // This class shouldn't be instantiated

    //-------------------------------------------------------------------------
    // PUBLIC METHODS
    //-------------------------------------------------------------------------

    /**
     * Compute a minimum-cardinality vertex cover (a set S subset of V that
     * contains at least one endpoint of each edge of G) and returns it.
     * @param graph Graph to examine.
     * @return List of vertices with the corresponding vertex cover.
     */
    public static List<Vertex> solve(Graph graph)
    {
        int n = graph.getN();
        List<Vertex> cover = null;
        Graph gPrime = new Graph(graph);

        // Finds the minimum-cardinality vertex cover of graph
        int k = 1;
        while(cover == null && k <= n)
        {
            cover = VertexCover.solveForK(graph, k);
            k++;
        }

        // Updates graph and returns the vertex cover
        graph.copy(gPrime);
        return cover;
    }

    //-------------------------------------------------------------------------
    // PRIVATE HELPER METHODS
    //-------------------------------------------------------------------------

    /**
     * Recursive algorithm that finds a vertex cover of size k for the given
     * graph. Returns an empty list (null) if such set doesn't exists.
     * @param graph Graph to examine.
     * @param k Size of subset of V to find.
     * @return Vertex cover of graph of size k. null if it doesn't exists.
     */
    private static List<Vertex> solveForK(Graph graph, int k)
    {
        if(k == 0)
        {
            return (graph.getM() == 0) ? new ArrayList<Vertex>() : null;
        }

        // Randomly selects and edge to prune
        int edgeId = (int) (Math.random() * graph.getM() + 1);
        Edge edge = graph.getEdge(edgeId);

        // Searches for a vertex cover removing the tail of edge
        graph.removeVertex(edge.getTail());
        List<Vertex> set_u = VertexCover.solveForK(graph, k - 1);
        if(set_u != null)
        {
            set_u.add(graph.getVertex(edge.getTail()));
            return set_u;
        }

        // Searches for a vertex cover removing the head of edge
        graph.removeVertex(edge.getTail());
        List<Vertex> set_v = VertexCover.solveForK(graph, k - 1);
        if(set_v != null)
        {
            set_v.add(graph.getVertex(edge.getHead()));
            return set_v;
        }
        else
        {
            return null;
        }
    }
}
