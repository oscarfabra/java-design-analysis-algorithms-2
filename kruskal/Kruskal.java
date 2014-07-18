/**
 * $Id: Kruskal.java, v 1.0 17/07/14 20:38 oscarfabra Exp $
 * {@code Kruskal} Class that implements a greedy algorithm for computing the
 * minimum spanning tree of a given undirected graph using Kruskal's algorithm.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 17/07/14
 */

import java.util.ArrayList;
import java.util.List;

/**
 * Class that implements a greedy algorithm for computing the minimum spanning
 * tree (MST) of a given undirected graph using Kruskal's algorithm.
 */
public class Kruskal
{
    //-------------------------------------------------------------------------
    // CLASS VARIABLES
    //-------------------------------------------------------------------------

    // Stores the ids of the covered vertices of the MST
    private static List<Integer> T = null;

    //-------------------------------------------------------------------------
    // CONSTRUCTOR
    //-------------------------------------------------------------------------

    private Kruskal(){ }    // This class shouldn't be instantiated

    //-------------------------------------------------------------------------
    // PUBLIC CLASS METHODS
    //-------------------------------------------------------------------------

    /**
     * Finds the Minimum Spanning Tree (MST) of the given graph using Kruskal's
     * MST algorithm's straightforward implementation and returns its cost;
     * O(n*m) algorithm. <br/>
     * <b>Pre: </b> The given graph is connected and has no cycles.
     *
     * @param graph Graph to examine.
     * @return The overall cost of the MST found.
     */
    public static long solveStraightforward(Graph graph)
    {
        // Sorts the costs of the edges of the given graph, such that edges[0]
        // contains the id of the cheapest edge, and so on, i in {1...m}
        int m = graph.getM();
        int [] edges = new int[m];
        QuickEdges.sortEdges(graph, edges);

        // Initializes the list on which to store the explored vertices
        Kruskal.T = new ArrayList<Integer>(graph.getN()/2);
        long cost = 0;

        // Walks through the edges of the graph in increasing order of their
        // costs, choosing them as appropriate
        for(int i = 0; i < m; i++)
        {
            // Adds tail and head vertices to T and increase the overall cost
            // only if the next edge doesn't produce any cycles
            Edge edge = graph.getEdge(edges[i]);
            if(!Kruskal.edgeProducesACycle(edge,graph))
            {
                Kruskal.T.add(edge.getTail());
                Kruskal.T.add(edge.getHead());
                cost += edge.getCost();
            }
        }

        // Returns the overall cost found
        return cost;
    }

    //-------------------------------------------------------------------------
    // PRIVATE HELPER METHODS
    //-------------------------------------------------------------------------

    /**
     * Determines whether the edge with the given id of the given graph
     * produces a cycle in the graph.
     *
     * @param edge Edge to look for in the given graph.
     * @param graph Graph to examine.
     * @return Whether the edge with the given id produces a cycle.
     */
    private static boolean edgeProducesACycle(Edge edge, Graph graph)
    {
        return Kruskal.T.contains(Integer.valueOf(edge.getTail())) &&
                Kruskal.T.contains(Integer.valueOf(edge.getHead()));
    }
}
