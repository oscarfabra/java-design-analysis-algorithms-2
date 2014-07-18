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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Class that implements a greedy algorithm for computing the minimum spanning
 * tree (MST) of a given undirected graph using Kruskal's algorithm.
 */
public class Kruskal
{
    //-------------------------------------------------------------------------
    // CLASS VARIABLES
    //-------------------------------------------------------------------------

    // Stores the ids of the covered edges of the MST
    private static List<Integer> T = null;

    // Stores the ids of the covered vertices of the MST in a given iteration
    private static List<Integer> X = null;

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

        // Initializes the list on which to store the explored edges
        Kruskal.T = new ArrayList<Integer>(m/2);
        Kruskal.X = new ArrayList<Integer>();
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
                Kruskal.T.add(edge.getId());
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
     * produces a cycle using depth-first search (DFS).
     *
     * @param edge Edge to look for in the given graph.
     * @param graph Graph to examine.
     * @return Whether the edge with the given id produces a cycle.
     */
    private static boolean edgeProducesACycle(Edge edge, Graph graph)
    {
        // Finds a cycle using vertices' explored attribute
        graph.setVertexExploredValue(edge.getTail(), true);
        boolean hasCycle = DFSForFindingACycle(edge.getHead(), graph);

        // Cleans the explored attribute of visited vertices
        for(Integer vertexId : Kruskal.X)
        {
            graph.setVertexExploredValue(vertexId, false);
        }
        Kruskal.X = new ArrayList<Integer>();

        // Returns the answer found
        return hasCycle;
    }

    /**
     * Performs a depth-first search to determine whether T + {edgeToAdd}
     * produces a cycle, starting at the given vertex id.
     * @param vertexId Id of the vertex in which to start to look at.
     * @param graph Graph to examine.
     * @return Whether there's a cycle or not.
     */
    private static boolean DFSForFindingACycle(int vertexId, Graph graph)
    {
        // If it encounters an already-explored vertex, then there's a cycle
        if(graph.getVertex(vertexId).isExplored())
        {
            return true;
        }

        // Sets the given vertex as explored and adds it to X
        graph.setVertexExploredValue(vertexId, true);
        Kruskal.X.add(vertexId);

        // Walks through the list of adjacent edges of the given vertex
        // looking for cycles
        for(Edge edge : graph.getAdjacentEdges(vertexId))
        {
            // Determines whether there's a cycle
            if(Kruskal.T.contains(Integer.valueOf(edge.getId()))
                    && Kruskal.vertexNotAlreadyVisited(vertexId, edge))
            {
                int tailId = edge.getTail();
                int headId = edge.getHead();
                if(tailId == vertexId)
                {
                    return DFSForFindingACycle(headId, graph);
                }
                else
                {
                    return DFSForFindingACycle(tailId, graph);
                }
            }
        }
        return false;
    }

    /**
     * Determines if a given vertex has already been visited looking at the
     * list of visited vertices X.
     * @param vertexId Id of the vertex in which to look for.
     * @param edge Edge to examine.
     * @return Whether the vertex has been visited or not.
     */
    private static boolean vertexNotAlreadyVisited(int vertexId, Edge edge)
    {
        if(vertexId == edge.getTail())
        {
            return Kruskal.X.contains(Integer.valueOf(edge.getHead()));
        }
        else if(vertexId == edge.getHead())
        {
            return Kruskal.X.contains(Integer.valueOf(edge.getTail()));
        }
        else
        {
            return false;
        }
    }
}
