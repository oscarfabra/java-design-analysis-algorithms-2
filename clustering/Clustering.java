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

import java.util.*;

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
    private static Map<Integer,List<Integer>> clusterVertices;

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

    // Stores the pairs of points already merged into a cluster
    private static List<Edge> merged;

    //-------------------------------------------------------------------------
    // CONSTRUCTOR
    //-------------------------------------------------------------------------

    private Clustering(){ }         // This class shouldn't be instantiated

    //-------------------------------------------------------------------------
    // PUBLIC CLASS METHODS
    //-------------------------------------------------------------------------

    /**
     * Finds the largest value of k such that there is a k-clustering with
     * spacing at least s taking into account the hamming distance between each
     * of the nodes.
     * @param nodes Array of lists with the associated bits for each node.
     * @param spacing Minimum spacing to look for.
     * @return Largest value of k for a k-clustering with spacing at least s.
     */
    public static int findMaxClustering(List<Integer>[] nodes, int spacing)
    {
        // Initializes data structures
        int n = nodes.length;
        Clustering.clusterVertices = new HashMap<Integer, List<Integer>>(n);
        Clustering.vertexCluster = new HashMap<Integer, Integer>(n);
        Clustering.merged = new ArrayList<Edge>(n / 2);

        // Initializes points putting each of them on a separate cluster
        System.out.println("Initializing clusters...");
        Clustering.clustersNumber = 1;
        for(int i = 0; i < n; i++)
        {
            Clustering.addToCluster(i+1, Clustering.clustersNumber++);

            // Message in standard output for logging purposes
            if((i + 1) % 5000 == 0)
            {
                System.out.println("-- " + (i + 1) + " clusters so far.");
            }
        }
        Clustering.clustersNumber--;
        System.out.println("...clusters initialized.");

        // Finds the largest value of k such that there is a k-clustering with
        // spacing at least s
        Clustering.closestPairSpacing = 0;
        int [] pAndQ;
        System.out.println("Finding largest value of k with spacing at least " +
                spacing + "...");
        while((Clustering.closestPairSpacing < spacing) &&
                (Clustering.clustersNumber > 1))
        {
            // Gets the closest pair of points, guaranteeing that they belong
            // to different clusters
            pAndQ = Clustering.updateClosestHammingDistance(nodes, n, spacing);

            // If it has found a pair of nodes with closestPairSpacing distance
            // between them
            if(pAndQ[0] != -1)
            {
                // Merges the clusters that contain p and q into a single one
                Integer p = Integer.valueOf(pAndQ[0]);
                Integer q = Integer.valueOf(pAndQ[1]);
                int clusterOfP = Clustering.vertexCluster.get(p);
                int clusterOfQ = Clustering.vertexCluster.get(q);
                Clustering.mergeClusters(clusterOfP, clusterOfQ);
            }

            // Prints message in standard output for logging purposes
            if((Clustering.clustersNumber % 10) == 0)
            {
                System.out.println("-- " + Clustering.clustersNumber +
                        " clusters remaining so far.");
            }
        }
        System.out.println("...k found.");

        // Returns the largest value of k
        return Clustering.clustersNumber;
    }

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
        Clustering.clusterVertices = new HashMap<Integer, List<Integer>>(m);
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
     * Finds the pair of nodes with the smallest hamming distance between them
     * guaranteeing that they belong to different clusters and updates this
     * distance.
     * @param nodes Array of lists with the associated bits for each node.
     * @param n Number of nodes.
     * @param spacing Minimum spacing to look for.
     * @return Array of size 2 with p and q in positions 0 and 1 respectively.
     */
    private static int[] updateClosestHammingDistance(List<Integer>[] nodes,
                                                      int n, int spacing)
    {
        // Gets the closest pair of points guaranteeing that they belong to
        // different clusters
        Edge edge = Clustering.getClosestPairHammingDistance(nodes, n, spacing);
        Integer p = -1;
        Integer q = -1;
        // If it has found a pair of nodes with closestPairSpacing distance
        if(edge.getId() != -1)
        {
            p = Integer.valueOf(edge.getTail());
            q = Integer.valueOf(edge.getHead());
            while((Clustering.clustersNumber>1) && (Clustering.vertexCluster.get(p)
                    == Clustering.vertexCluster.get(q)))
            {
                edge = Clustering.getClosestPairHammingDistance(nodes, n, spacing);
                p = edge.getTail();
                q = edge.getHead();
            }
        }
        // Stores p and q in an array of integers and returns it
        int [] pAndQ = new int[2];
        pAndQ[0] = p;
        pAndQ[1] = q;
        return pAndQ;
    }

    /**
     * Finds the pair of nodes with the smallest hamming distance between them.
     * @param nodes Array of lists with the associated bits for each node.
     * @param n Number of nodes.
     * @param spacing Minimum spacing to look for.
     * @return Edge with the pair of points and the hamming distance in between.
     */
    private static Edge getClosestPairHammingDistance(List<Integer>[] nodes,
                                                      int n, int spacing)
    {
        // Walks through the nodes array finding a pair of nodes with spacing
        // closestPairSpacing
        Edge edge = new Edge(-1, -1, -1, -1);
        boolean pairFound = false;
        while(Clustering.closestPairSpacing < spacing)
        {
            for(int i = 0; i < n - 1; i++)
            {
                for(int j = i + 1; j < n; j++)
                {
                    // Break if points haven't been merged before and posses the
                    // minimum distance closestPairSpacing
                    if(!Clustering.pairAlreadyMerged(i+1, j+1) &&
                            Clustering.closestPairHammingDistance(nodes, i, j))
                    {
                        // Updates the flag
                        pairFound = true;

                        // Saves the corresponding pairs and spacing to return
                        edge = new Edge(0, i + 1, j + 1, Clustering.closestPairSpacing);
                        Clustering.merged.add(edge);
                        break;
                    }
                }
                if(pairFound){ break; }
            }
            // If it doesn't finds any pair with spacing closestPairSpacing,
            // increments spacing
            if(pairFound){ break; }
            Clustering.closestPairSpacing++;
        }
        return edge;
    }

    /**
     * Determines whether points with id vId and wId have already been merged.
     * @param vId Id of the first point to look for in the merged class list.
     * @param wId Id of the second point to look for in the merged class list.
     * @return Whether the given points have already been merged or not.
     */
    private static boolean pairAlreadyMerged(int vId, int wId)
    {
        for(Edge edge : Clustering.merged)
        {
            int tailId = edge.getTail();
            int headId = edge.getHead();
            if((vId == tailId && wId == headId) ||
                    (wId == tailId && vId == headId))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines whether nodes at positions i and j of the given array differ
     * by closestPairSpacing bits or not.
     * @param nodes Array of lists with the associated bits for each node.
     * @param i Position of the first node in the nodes array to compare.
     * @param j Position of the second node in the nodes array to compare.
     * @return Whether the given nodes differ by closestPairSpacing bits.
     */
    private static boolean closestPairHammingDistance(List<Integer>[] nodes,
                                                      int i, int j)
    {
        List<Integer> nodeI = nodes[i];
        List<Integer> nodeJ = nodes[j];
        int distance = 0;

        for(int bit = 0; bit < nodeI.size(); bit++)
        {
            if(nodeI.get(bit) != nodeJ.get(bit))
            {
                if (++distance > Clustering.closestPairSpacing)
                {
                   return false;
                }
            }
        }
        // Returns true if distance wasn't surpassed
        return true;
    }

    /**
     * Finds and updates the distance between the two vertices that belong to
     * different clusters and are the closest to each other.
     * @param graph Graph with the distance function to examine.
     * @param distances Array of int with the ids of the edges or pair of points.
     * @return Array of size 2 with p and q in positions 0 and 1 respectively.
     */
    private static int [] updateClosestPairSpacing(Graph graph, int[] distances)
    {
        // Gets the closest pair of points guaranteeing that they belong to
        // different clusters
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
                = Clustering.clusterVertices.get(Integer.valueOf(clusterOfP))
                .size();
        int sizeOfClusterOfQ
                = Clustering.clusterVertices.get(Integer.valueOf(clusterOfQ))
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
                Clustering.clusterVertices.get(Integer.valueOf(originCluster));
        List<Integer> verticesAux = new Vector<Integer>(verticesIds);
        for(Integer vertexId : verticesAux)
        {
            // Removes vertex from one cluster and adds it to the other
            Clustering.removeFromCluster(originCluster, vertexId);
            Clustering.addToCluster(destinationCluster, vertexId);
        }

        // Updates clustersNumber
        if(Clustering.clustersNumber > 1)
        {
            Clustering.clustersNumber--;
        }
    }

    /**
     * Adds the given vertex to the given cluster. Updates maps as appropriate.
     * @param clusterId Id of the cluster to which the vertex will be assigned.
     * @param vertexId Id of the vertex to assign to the given cluster.
     */
    private static void addToCluster(int clusterId, Integer vertexId)
    {
        List<Integer> verticesIds =
                Clustering.clusterVertices.remove(Integer.valueOf(clusterId));
        if(verticesIds == null)
        {
            verticesIds = new Vector<Integer>();
        }
        verticesIds.add(vertexId);
        Clustering.clusterVertices.put(clusterId, verticesIds);
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
                Clustering.clusterVertices.remove(Integer.valueOf(clusterId));
        verticesIds.remove(vertexId);
        if(verticesIds.size() > 0)
        {
            Clustering.clusterVertices.put(clusterId, verticesIds);
        }
        Clustering.vertexCluster.remove(vertexId);
    }
}
