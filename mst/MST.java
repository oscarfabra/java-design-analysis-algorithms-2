/**
 * $Id: MST.java, v 1.0 03/07/14 20:38 oscarfabra Exp $
 * {@code MST} Class that implements a greedy algorithm for computing the
 * minimum spanning tree of a given undirected graph.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 03/07/14
 */

import java.util.List;

/**
 * Class that implements a greedy algorithm for computing the minimum spanning
 * tree of a given undirected graph.
 */
public class MST
{
    //-------------------------------------------------------------------------
    // CONSTRUCTOR
    //-------------------------------------------------------------------------

    private MST(){ }        // This class shouldn't be instantiated

    //-------------------------------------------------------------------------
    // PUBLIC CLASS METHODS
    //-------------------------------------------------------------------------

    /**
     * Finds the Minimum Spanning Tree (MST) of the given graph and returns its
     * cost.
     * @param graph Graph to examine.
     * @param mst List of Integers in which to store the MST.
     * @return The overall cost of the MST found.
     */
    public static long solve(Graph graph, List<Integer> mst)
    {
        int cost = 0;
        // TODO: Find MST...
        return cost;
    }

    //-------------------------------------------------------------------------
    // PRIVATE HELPER METHODS
    //-------------------------------------------------------------------------

}
