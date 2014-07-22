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

import java.util.ArrayList;
import java.util.HashMap;
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

    // Stores the index of the distances array that is being considered
    private static int currentDistanceIndex;

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
        // Initializes data structures
        int m = graph.getM();
        Clustering.clustersVertices = new HashMap<Integer, List<Integer>>(m);
        Clustering.vertexCluster = new HashMap<Integer, Integer>(graph.getN());

        // Stores the distances between points (edge costs)
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
        Clustering.clustersNumber = 1;
        for(Integer vertexId: graph.getVertexKeys())
        {
            Clustering.addToCluster(vertexId, Clustering.clustersNumber++);
        }

        // Repeats until there are only k clusters
        Clustering.currentDistanceIndex = 0;
        while(Clustering.clustersNumber > k)
        {
            // Gets the closest pair of separated points
            int closestPair = distances[Clustering.currentDistanceIndex];
            Edge pAndQ = graph.getEdge(Integer.valueOf(closestPair));
            Clustering.closestPairSpacing = pAndQ.getCost();

            // Merges the clusters that contain p and q into a single one, AND
            // updates distances between clusters accordingly
            int p = pAndQ.getTail();
            int q = pAndQ.getHead();
            Clustering.mergeClustersAndUpdateDistances(p, q, graph, distances);

            // Increases current distance index
            Clustering.currentDistanceIndex++;
        }

        // Returns the distance between the closest pair of separated points
        return Clustering.closestPairSpacing;
    }

    //-------------------------------------------------------------------------
    // PRIVATE HELPER METHODS
    //-------------------------------------------------------------------------

    /**
     * Adds the given vertex to the given cluster. Updates maps as appropriate.
     * @param clusterId Id of the cluster to which the vertex will be assigned.
     * @param vertexId Id of the vertex to assign to the given cluster.
     */
    private static void addToCluster(int clusterId, Integer vertexId)
    {
        List<Integer> verticesIds =
                Clustering.clustersVertices.remove(Integer.valueOf(clusterId));
        if(verticesIds == null)
        {
            verticesIds = new ArrayList<Integer>();
        }
        verticesIds.add(vertexId);
        Clustering.clustersVertices.put(clusterId, verticesIds);
        Clustering.vertexCluster.put(vertexId, clusterId);
    }

    /**
     * Removes the given vertex from the given cluster. Updates maps as
     * appropriate. <br/>
     * <b>Pre:</b> The given vertex is found in the given cluster.
     * @param clusterId Id of the cluster from which the vertex will be removed.
     * @param vertexId Id of the vertex to be removed.
     */
    private static void removeFromCluster(int clusterId, Integer vertexId)
    {
        List<Integer> verticesIds =
                Clustering.clustersVertices.remove(Integer.valueOf(clusterId));
        verticesIds.remove(vertexId);
        Clustering.clustersVertices.put(clusterId, verticesIds);
        Clustering.vertexCluster.remove(vertexId);
    }

    /**
     * Merges vertices with ids p and q into a single cluster AND updates
     * distances as appropriate. <br/>
     * <b>Pre:</b> p and q belong to different clusters. (i.e. <br/>
     * vertexCluster.get(Integer.valueOf(p))
     * != vertexCluster.get(Integer.valueOf(q))).
     * @param p Id of the first vertex to merge.
     * @param q Id of the second vertex to merge.
     * @param graph Graph with the distance function to examine.
     * @param distances Array of int with the ids of the edges or distances
     *                  between clusters, ordered in increasing order.
     */
    private static void mergeClustersAndUpdateDistances(int p, int q,
                                                        Graph graph,
                                                        int[] distances)
    {
        int clusterOfP = Clustering.vertexCluster.get(Integer.valueOf(p));
        int clusterOfQ = Clustering.vertexCluster.get(Integer.valueOf(q));
        int sizeOfClusterOfP = Clustering.clustersVertices.get(clusterOfP).size();
        int sizeOfClusterOfQ = Clustering.clustersVertices.get(clusterOfP).size();

        // Moves vertices from the smaller cluster to the bigger one
        List<Integer> verticesIds = null;
        int originCluster = 0;
        int destinationCluster = 0;
        if(sizeOfClusterOfP >= sizeOfClusterOfQ)
        {
            verticesIds =
                    Clustering.clustersVertices.get(Integer.valueOf(clusterOfQ));
            originCluster = clusterOfQ;
            destinationCluster = clusterOfP;
        }
        else
        {
            verticesIds =
                    Clustering.clustersVertices.get(Integer.valueOf(clusterOfP));
            originCluster = clusterOfP;
            destinationCluster = clusterOfQ;
        }

        // Removes each vertex from originCluster and adds it to
        // destinationCluster, updating distances as appropriate
        for(Integer vertexId : verticesIds)
        {
            // Removes vertex from one cluster and adds it to the other
            Clustering.removeFromCluster(clusterOfQ, vertexId);
            Clustering.addToCluster(clusterOfP, vertexId);

            // Updates distances between clusters
            Clustering.updateDistances(vertexId, originCluster,
                    destinationCluster, distances);
        }

        // Updates clustersNumber
        Clustering.clustersNumber--;
    }

    /**
     * Updates distances between cluster taking into account the vertex that is
     * being moved, its origin and its destination.
     * @param vertexId Id of the vertex that is being moved.
     * @param originCluster Id of the origin cluster.
     * @param destinationCluster Id of the destination cluster.
     * @param distances Array of int with the ids of the edges or distances
     *                  between clusters, ordered in increasing order.
     */
    private static void updateDistances(Integer vertexId, int originCluster,
                                        int destinationCluster, int[] distances)
    {
        // TODO: Update distances...
    }
}
