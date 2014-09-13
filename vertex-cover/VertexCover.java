/**
 * $Id: VertexCover.java, v1.0 6/09/14 10:06 PM oscarfabra Exp $
 * {@code VertexCover} solves the vertex cover problem for a given undirected
 * graph.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 6/09/14
 */

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
        // TODO: Write branch and bound on edges' algorithm...
        return null;
    }

    //-------------------------------------------------------------------------
    // PRIVATE HELPER METHODS
    //-------------------------------------------------------------------------

}
