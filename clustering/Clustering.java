/**
 * $Id: Clustering.java, v 1.0 20/07/14 20:38 oscarfabra Exp $
 * {@code Clustering} is a class that implements a greedy algorithm for
 * computing the max-spacing k-clustering of a distance function represented
 * as a complete graph with edge costs.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 20/07/14
 */

/**
 * Class that implements a greedy algorithm for computing the max-spacing
 * k-clustering of a distance function represented as a complete graph with
 * edge costs.
 */
public class Clustering
{
    //-------------------------------------------------------------------------
    // CLASS VARIABLES
    //-------------------------------------------------------------------------

    //-------------------------------------------------------------------------
    // CONSTRUCTOR
    //-------------------------------------------------------------------------

    private Clustering(){ }         // This class shouldn't be instantiated

    //-------------------------------------------------------------------------
    // PUBLIC CLASS METHODS
    //-------------------------------------------------------------------------

    /**
     * Finds the maximum spacing of a k-clustering of the distance function
     * represented in the given graph.
     * @param graph Graph with the distance function to examine.
     * @param k Number of clusters desired.
     * @return Maximum spacing of a k-clustering of the given graph.
     */
    public static int findMaxSpacing(Graph graph, int k)
    {
        // TODO: Find max-spacing of a k-clustering...
        return 0;
    }

    //-------------------------------------------------------------------------
    // PRIVATE HELPER METHODS
    //-------------------------------------------------------------------------

}
