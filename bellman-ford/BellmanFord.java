/**
 * $Id: BellmanFord.java, v1.0 14/08/14 06:01 PM oscarfabra Exp $
 * {@code BellmanFord} Is a class that computes Bellman-Ford's single-source
 * shortest-path algorithm defined as follows.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 14/08/14
 */

/**
 * Solves Bellman-Ford's single-source, shortest path algorithm defined as
 * follows:
 * Input: Directed graph G=(V,E), edge lengths c_e for each e in E, and source
 * vertex s in V [assumes no parallel edges].
 * Goal: For every destination v in V, compute the length of a shortest s - v
 * path.
 */
public class BellmanFord
{
    //-------------------------------------------------------------------------
    // ATTRIBUTES
    //-------------------------------------------------------------------------

    //-------------------------------------------------------------------------
    // CONSTRUCTORS
    //-------------------------------------------------------------------------

    private BellmanFord( ){ }         // This class shouldn't be instantiated

    //-------------------------------------------------------------------------
    // PUBLIC METHODS
    //-------------------------------------------------------------------------

    /**
     * Computes the shortest-path distance from vertex s to all other vertices
     * of the given graph.
     * @param s Source vertex.
     * @param graph Graph to examine.
     * @return Array of integers with the length of each path s -> v, v in V.
     */
    public static int[] solve(int s, Graph graph)
    {
        // TODO: Write Bellman-Ford O(m*n) algorithm...
        return new int[0];
    }

    //-------------------------------------------------------------------------
    // PRIVATE HELPER METHODS
    //-------------------------------------------------------------------------
}
