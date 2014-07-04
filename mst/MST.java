/**
 * $Id: MST.java, v 1.0 03/07/14 20:38 oscarfabra Exp $
 * {@code MST} Class that implements a greedy algorithm for computing the
 * minimum spanning tree of a given undirected graph.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 03/07/14
 */

import java.util.ArrayList;
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
     * Finds the Minimum Spanning Tree (MST) of the given graph using Prim's
     * MST algorithm and returns its cost. O(n * m) algorithm.
     * <b>Pre: </b>The given graph is connected and has no cycles.
     * @param graph Graph to examine.
     * @param mst List of Integers in which to store the MST.
     * @return The overall cost of the MST found.
     */
    public static long solveByPrims(Graph graph, List<Integer> mst)
    {
        // Initializes the first vertex s, the number of vertices, and the
        // overall cost for finding the MST
        int s = 1;
        int n = graph.getN();
        long cost = 0;
        List<Integer> x = new ArrayList<Integer>(n);
        x.add(s);

        // Main loop for finding and storing the MST, O(n*m) algorithm
        System.out.println("Building Minimum Spanning Tree using Prim's " +
                "Algorithm...");
        while(x.size() < n)
        {
            // Finds the cheapest edge and adds its cost to the overall cost
            Edge minEdge = MST.findCheapestEdge(x, graph);
            cost += minEdge.getCost();

            // Adds the appropriate vertex id
            if(!x.contains(Integer.valueOf(minEdge.getHead())))
            {
                x.add(minEdge.getHead());
            }
            else
            {
                x.add(minEdge.getTail());
            }
            // Message in standard output for debugging purposes
            if(x.size()%10 == 0)
            {
                System.out.println("-- [" + x.size() + " vertices covered " +
                        "so far.]");
            }
        }
        System.out.println("...MST built.");

        // Copies the list of vertices V into the given list mst
        for(Integer vertexId : x)
        {
            mst.add(vertexId);
        }

        return cost;
    }

    //-------------------------------------------------------------------------
    // PRIVATE HELPER METHODS
    //-------------------------------------------------------------------------

    /**
     * Finds the cheapest edge of the given graph with a vertex in x, and the
     * other vertex outside of x.
     * @param x List of of explored vertex ids.
     * @param graph Graph to examine.
     * @return Edge with the smallest cost that posses the required conditions.
     */
    private static Edge findCheapestEdge(List<Integer> x, Graph graph)
    {
        // Assumes smallest cost to be a very large number
        int minCost = 1000000;
        Edge minEdge = null;

        for(Edge crossingEdge : findCrossingEdges(x, graph))
        {
            if(crossingEdge.getCost() < minCost)
            {
                minEdge = crossingEdge;
                minCost = crossingEdge.getCost();
            }
        }
        // Returns the edge with the minimum cost
        return minEdge;
    }

    /**
     * Finds a returns the edges that have one vertex in x and the other
     * outside x.
     * @param x List of vertices added to the MST.
     * @param graph Graph to examine.
     * @return List of edges that have one vertex in x and the other outside x.
     */
    private static List<Edge> findCrossingEdges(List<Integer> x, Graph graph)
    {
        // Determines which are the crossing edges
        List<Edge> crossingEdges = new ArrayList<Edge>();
        for(Integer uId : x)
        {
            // Explores the adjacent edges of the vertices that are in x
            for(Edge edge : graph.getAdjacentEdges(uId))
            {
                Integer vertexA = edge.getTail();
                Integer vertexB = edge.getHead();
                Integer vertexToAdd = 0;
                if(x.contains(vertexA) && !x.contains(vertexB))
                {
                    vertexToAdd = vertexB;
                }
                else if(!x.contains(vertexA) && x.contains(vertexB))
                {
                    vertexToAdd = vertexA;
                }
                // If only one of the vertices is in x, then we know it's a
                // crossing edge
                if(vertexToAdd != 0)
                {
                    crossingEdges.add(edge);
                }
            }
        }
        return crossingEdges;
    }
}
