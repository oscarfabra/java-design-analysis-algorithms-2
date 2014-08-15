/**
 * $Id: BellmanFord.java, v1.0 14/08/14 06:01 PM oscarfabra Exp $
 * {@code BellmanFord} Is a class that computes Bellman-Ford's single-source
 * shortest-path algorithm.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 14/08/14
 */

import java.util.List;

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

    // Large value used to represent infinity (no path between two vertices)
    public static final int INFINITY = 1000000;

    //-------------------------------------------------------------------------
    // CONSTRUCTORS
    //-------------------------------------------------------------------------

    private BellmanFord( ){ }         // This class shouldn't be instantiated

    //-------------------------------------------------------------------------
    // PUBLIC METHODS
    //-------------------------------------------------------------------------

    /**
     * Computes the shortest-path distance from vertex s to all other vertices
     * of the given graph and returns an array of integers with the shortest
     * path distances from s to each vertex v of V.
     * @param s Source vertex.
     * @param graph Graph to examine.
     * @return Array of integers with the length of each path s -> v, v in V.
     */
    public static int[] solve(int s, Graph graph)
    {
        // Initializes appropriate data structure and its first column,
        // assumes vertexId's v are in [1,...,n]
        int n = graph.getN();
        int [][] a = new int[n][2];
        for(int v = 1; v <= n; v++)
        {
            a[v - 1][0] = BellmanFord.INFINITY;
        }
        a[s - 1][0] = 0;

        // Walks through the array filling out the corresponding values, uses
        // memoization; it only keeps the past column (at index 0) and the one
        // that is computing (at index 1), for space optimization
        for(int i = 1; i <= n; i++)
        {
            // Assumes vertexId's v are in [1,...,n]
            for(int v = 1; v <= n; v++)
            {
                int firstCase = a[v][i - 1];
                List<Edge> edgesArriving = graph.getEdgesArriving(v);
                int secondCase = BellmanFord.INFINITY * 2;
                for(Edge edge : edgesArriving)
                {
                    int w = edge.getTail();
                    int candidate = a[w][i - 1] + edge.getCost();
                    if(candidate <= secondCase)
                    {
                        secondCase = candidate;
                    }
                }
                a[v][i] = Math.min(firstCase, secondCase);
            }
            // Updates 2-D array a for better memory usage
            for(int v = 1; v <= n; v++)
            {
                a[v - 1][0] = a[v - 1][1];
                a[v - 1][1] = BellmanFord.INFINITY;
            }
            a[s - 1][1] = 0;
        }
        // Stores the shortest s->v paths for all v's in graph and returns them
        int [] lengths = new int[n];
        for(int i = 0; i < n; i++)
        {
            lengths[i] = a[i][0];
        }
        return lengths;
    }

    //-------------------------------------------------------------------------
    // PRIVATE HELPER METHODS
    //-------------------------------------------------------------------------

    // TODO: Check for negative cycles...
}
