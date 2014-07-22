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

import java.util.Vector;
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
        int n = graph.getN();
        Clustering.clustersVertices = new HashMap<Integer, List<Integer>>(m);
        Clustering.vertexCluster = new HashMap<Integer, Integer>(n);
        int [] distances = new int[m];

        // Sorts the distances in order to identify the closest pair of points
        QuickEdges.sortEdges(graph, distances);

        // Initializes points putting each of them on a separate cluster
        Clustering.clustersNumber = 1;
        for(Integer vertexId: graph.getVertexKeys())
        {
            Clustering.addToCluster(vertexId, Clustering.clustersNumber++);
        }
        Clustering.clustersNumber--;

        // Repeats until there are only k clusters
        Clustering.currentDistanceIndex = 0;
        int [] pAndQ;
        while(Clustering.clustersNumber > k)
        {
            // Gets the closest pair of points, guaranteeing that they belong
            // to different clusters
            pAndQ = Clustering.updateClosestPairSpacing(graph, distances);

            // Merges the clusters that contain p and q into a single one
            Integer p = Integer.valueOf(pAndQ[0]);
            Integer q = Integer.valueOf(pAndQ[1]);
            int clusterOfP = Clustering.vertexCluster.get(p);
            int clusterOfQ = Clustering.vertexCluster.get(q);
            Clustering.mergeClusters(clusterOfP, clusterOfQ);
        }

        // Finds and updates the closest pair spacing
        Clustering.updateClosestPairSpacing(graph, distances);

        // Returns the distance between the closest pair of separated points
        return Clustering.closestPairSpacing;
    }

    //-------------------------------------------------------------------------
    // PRIVATE HELPER METHODS
    //-------------------------------------------------------------------------

    /**
     * Finds and updates the distance between the two vertices that belong to
     * different clusters and are the closest to each other.
     * @param graph Graph with the distance function to examine.
     * @param distances Array of int with the ids of the edges or pair of
     * @return Array of size 2 with p and q in positions 0 and 1 respectively.
     */
    private static int [] updateClosestPairSpacing(Graph graph, int[] distances)
    {
        int closestPair = distances[Clustering.currentDistanceIndex++];
        Edge edge = graph.getEdge(Integer.valueOf(closestPair));
        Integer p = Integer.valueOf(edge.getTail());
        Integer q = Integer.valueOf(edge.getHead());
        while(Clustering.vertexCluster.get(p)
                == Clustering.vertexCluster.get(q))
        {
            closestPair = distances[Clustering.currentDistanceIndex++];
            edge = graph.getEdge(Integer.valueOf(closestPair));
            p = edge.getTail();
            q = edge.getHead();
        }
        Clustering.closestPairSpacing = edge.getCost();
        // Stores p and q in an array of integers and returns it
        int [] pAndQ = new int[2];
        pAndQ[0] = p;
        pAndQ[1] = q;
        return pAndQ;
    }

    /**
     * Merges the clusters with the given ids into a single one. <br/>
     * <b>Pre:</b> clusterOfP != clusterOfQ.
     * @param clusterOfP Id of the first cluster to merge.
     * @param clusterOfQ Id of the second cluster to merge.
     */
    private static void mergeClusters(int clusterOfP, int clusterOfQ)
    {
        // Moves vertices from the smaller cluster to the bigger one
        int sizeOfClusterOfP
                = Clustering.clustersVertices.get(Integer.valueOf(clusterOfP))
                .size();
        int sizeOfClusterOfQ
                = Clustering.clustersVertices.get(Integer.valueOf(clusterOfQ))
                .size();
        List<Integer> verticesIds = null;
        int originCluster = 0;
        int destinationCluster = 0;
        if(sizeOfClusterOfP >= sizeOfClusterOfQ)
        {
            originCluster = clusterOfQ;
            destinationCluster = clusterOfP;
        }
        else
        {
            originCluster = clusterOfP;
            destinationCluster = clusterOfQ;
        }

        // Removes each vertex from originCluster and adds it to
        // destinationCluster
        verticesIds =
                Clustering.clustersVertices.get(Integer.valueOf(originCluster));
        List<Integer> verticesAux = new Vector<Integer>(verticesIds);
        for(Integer vertexId : verticesAux)
        {
            // Removes vertex from one cluster and adds it to the other
            Clustering.removeFromCluster(originCluster, vertexId);
            Clustering.addToCluster(destinationCluster, vertexId);
        }

        // Updates clustersNumber
        Clustering.clustersNumber--;
    }

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
            verticesIds = new Vector<Integer>();
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
        if(verticesIds.size() > 0)
        {
            Clustering.clustersVertices.put(clusterId, verticesIds);
        }
        Clustering.vertexCluster.remove(vertexId);
    }
}
