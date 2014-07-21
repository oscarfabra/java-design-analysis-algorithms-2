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

import java.util.List;
import java.util.Map;

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

    // Maps the vertices that comprise each cluster. Uses list-chaining to
    // assign the vertices of each cluster.
    private static Map<Integer,List<Integer>> clustersVertices;

    // Maps the cluster of each vertex. In this case there's no need for
    // chaining since each vertex can only belong to one cluster.
    private static Map<Integer,Integer> vertexCluster;

    // Stores the number of clusters in any given moment.
    // clusterNumber = clustersVertices.getKeys().size()
    private static int clustersNumber;

    // Stores the distance of the closest pair of separated points
    private static int closestPairSpacing;

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
        // Stores the distances between points (edge costs)
        int m = graph.getM();
        int [] distances = new int[m];
        int i = 0;
        for(Integer edgeId : graph.getEdgeKeys())
        {
            Edge edge = graph.getEdge(edgeId);
            distances[i++] = edge.getCost();
        }

        // Sorts the distances in order to identify the closest pair of points
        QuickEdges.sortEdges(graph,distances);

        // Initializes points putting each of them on a separate cluster
        int clusterId = 1;
        for(Integer vertexId: graph.getVertexKeys())
        {
            Clustering.addToCluster(vertexId, clusterId++);
        }

        // Repeats until there are only k clusters
        i = 0;
        while(Clustering.clustersNumber > k)
        {
            int closestPair = distances[i++];
            Edge pAndQ = graph.getEdge(Integer.valueOf(closestPair));
            Clustering.closestPairSpacing = pAndQ.getCost();
            int p = pAndQ.getTail();
            int q = pAndQ.getHead();
            Clustering.mergeClustersAndUpdateDistances(p, q, graph, distances);
        }

        // Returns the distance between the closest pair of separated points
        return Clustering.closestPairSpacing;
    }

    //-------------------------------------------------------------------------
    // PRIVATE HELPER METHODS
    //-------------------------------------------------------------------------

    /**
     * Adds the given vertex to the given cluster. Updates maps as appropriate.
     * @param vertexId Id of the vertex to assign to the given cluster.
     * @param clusterId Id of the cluster to which the vertex will be assigned.
     */
    private static void addToCluster(Integer vertexId, int clusterId)
    {
        // TODO: Add vertex to cluster...
    }

    /**
     * Merges vertices with ids p and q into a single cluster AND updates
     * distances as appropriate. <br/>
     * <b>Pre: </b> p and q belong to different clusters. (i.e. <br/>
     * vertexCluster.get(Integer.valueOf(p))
     * != vertexCluster.get(Integer.valueOf(q))).
     * @param p Id of a vertex to merge.
     * @param q Id of the second vertex to merge.
     * @param graph Graph with the distance function to examine.
     * @param distances Array of int with the ids of the edges or distances
     *                  between vertices, ordered in increasing order.
     */
    private static void mergeClustersAndUpdateDistances(int p, int q, Graph graph,
                                                        int[] distances)
    {
        // TODO: Merge clusters and update distances...
    }
}
