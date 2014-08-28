/**
 * $Id: TSP.java, v1.0 26/08/14 11:48 PM oscarfabra Exp $
 * {@code TSP} Is a class that computes the minimum-cost tour (i.e., a cycle
 * that visits every vertex exactly once) given a complete undirected graph
 * with non-negative edge costs.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 26/08/14
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * TSP is a class that solves the popular NP-Complete Algorithm "Traveling
 * Salesman Problem", defined as follows:
 * Input: A complete undirected graph with non-negative edge costs.
 * Output: A minimum-cost tour (i.e., a cycle that visits every vertex exactly
 * once).
 */
public class TSP
{
    //-------------------------------------------------------------------------
    // CONSTANTS
    //-------------------------------------------------------------------------

    // Theoretical infinite value
    public static final int INFINITY = 1000000;

    //-------------------------------------------------------------------------
    // ATTRIBUTES
    //-------------------------------------------------------------------------

    // 2-D array to index by subsets' sizes, size in {1,2,..,n} and destination
    // j in {1,2,...,n}. a[size][j] stores the minimum length of a path from 1
    // to j that visits precisely the vertices of S; S being the minimum-cost
    // subset that contains 1 and j
    private static int [][] a;

    // Maps subsets with their corresponding ids, each id in {1,2,...,n}
    private static Map<Integer, Set<Integer>> subsets;

    // Maps sizes of subsets with their corresponding ids. Uses list-chaining
    // given that a particular size could have several possible subsets
    private static Map<Integer, List<Integer>> sizeSubsets;

    // Maps subsets with their corresponding subset size. In this case there's
    // no need for chaining since each subset has only one possible size
    private static Map<Integer, Integer> subsetSize;

    //-------------------------------------------------------------------------
    // CONSTRUCTORS
    //-------------------------------------------------------------------------

    private TSP() { }   // This class shouldn't be instantiated

    //-------------------------------------------------------------------------
    // PUBLIC METHODS
    //-------------------------------------------------------------------------

    /**
     * Computes the Traveling Salesman Problem using dynamic programming. That
     * is, given a complete undirected graph with non-negative edge costs,
     * computes the minimum-cost cycle that visits every vertex exactly once.
     * @param graph Complete graph with non-negative edge costs.
     * @return Minimum-cost cycle that visits every vertex exactly once.
     */
    public static int solve(Graph graph)
    {
        // Initializes corresponding data structures
        int n = graph.getN();
        TSP.a = new int[n][n];
        TSP.sizeSubsets = new HashMap<Integer, List<Integer>>(n);
        TSP.subsetSize = new HashMap<Integer, Integer>(n);

        // Initializes data structures according to possible subset sizes
        for(int size = 1; size <= n; size++)
        {
            TSP.addDestinationSubsets(graph, size);
        }

        // Walks through each possible subset S of {1,2,...,n} looking for all
        // possible destinations j in {1,2,...,n}
        for(int m = 2; m < n; m++)
        {
            // For each subset of size m that contains 1
            List<Integer> setIds = TSP.sizeSubsets.get(m);
            for(Integer setId : setIds)
            {
                // Walks through each vertex j of the set looking for the
                // minimum length of a path from 1 to j that visits
                // precisely the vertices of the set with id setId
                Set<Integer> set = TSP.subsets.get(setId);
                Iterator<Integer> iterator = set.iterator();
                while(iterator.hasNext())
                {
                    int j = iterator.next();
                    if(j != 1)
                    {
                        TSP.a[setId - 1][j - 1] =
                                TSP.getMinimumLengthPath(set, j);
                    }
                }
            }
        }

        // Finds and returns the minimum-length path from 1 to itself visiting
        // every vertex once
        return TSP.getLengthAfterFinalHop(n);
    }

    /**
     * Finds and returns the minimum-length path from 1 to itself after
     * visiting every vertex once.
     * @param n Number of vertices of the graph.
     * @return Minimum-length of a path from 1 to itself that visits every
     * vertex once.
     */
    private static int getLengthAfterFinalHop(int n)
    {
        // TODO: Get length after final hop...
        return 0;
    }

    //-------------------------------------------------------------------------
    // PRIVATE HELPER METHODS
    //-------------------------------------------------------------------------

    /**
     * Finds and stores all possible subsets S of {1,2,...,n} that contain 1
     * for every destination j in {1,2,...,n}.
     * @param graph Complete graph with non-negative edge costs.
     * @param size Size of the subsets to create.
     */
    private static void addDestinationSubsets(Graph graph, int size)
    {
        // TODO: Add destination subsets...
    }

    /**
     * Gets the minimum length path from 1 to j that contains precisely the
     * vertices of the given set.
     * @param set Set of ids of vertices that contains 1 and j.
     * @param j Destination vertex id.
     * @return Minimum length of a path from 1 to j that visits precisely the
     * vertices of the given set.
     */
    private static int getMinimumLengthPath(Set<Integer> set, int j)
    {
        // TODO: Get minimum length...
        return 0;
    }
}
