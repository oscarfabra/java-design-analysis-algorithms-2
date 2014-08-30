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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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
    private static double [][] a;

    // Maps subsets with their corresponding ids, each id in {1,2,...,n}
    private static Map<Integer, Set<Integer>> subsets;

    // Stores the next subset to create
    private static Set<Integer> nextSubset;

    // Defines the id of the next subset to create
    private static int nextSubsetId;

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
    public static double solve(Graph graph)
    {
        // Initializes corresponding data structures
        int n = graph.getN();
        TSP.a = new double[n][n];
        TSP.subsets = new HashMap<Integer, Set<Integer>>(n);
        TSP.nextSubsetId = 1;
        TSP.sizeSubsets = new HashMap<Integer, List<Integer>>(n);
        TSP.subsetSize = new HashMap<Integer, Integer>(n);

        // Fills base case according to possible subset sizes
        TSP.nextSubset = new TreeSet<Integer>();
        TSP.nextSubset.add(1);
        TSP.putSubset(TSP.nextSubsetId, TSP.nextSubset);
        TSP.a[TSP.nextSubsetId - 1][0] = 0;
        TSP.nextSubsetId++;
        for(int size = 2; size <= n; size++)
        {
            TSP.initDestinationSubset(size);
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
                                TSP.getMinimumLengthPath(graph, setId, j);
                    }
                }
            }
        }

        // Finds and returns the minimum-length path from 1 to itself visiting
        // every vertex once
        return TSP.getLengthAfterFinalHop(graph, n);
    }

    //-------------------------------------------------------------------------
    // PRIVATE HELPER METHODS
    //-------------------------------------------------------------------------

    /**
     * Finds and stores all possible subsets S of {1,2,...,n} of the given
     * size that contain 1 for every destination j in {1,2,...,n}.
     * <b>Pre:</b> size >= 2
     * @param size Size of the subsets to create.
     */
    private static void initDestinationSubset(int size)
    {
        // Initializes next subset to add
        TSP.nextSubset = new TreeSet<Integer>();
        for(int vertexId = 1; vertexId <= size; vertexId++)
        {
            TSP.nextSubset.add(vertexId);
        }

        // Puts next subset in its corresponding collections and initializes
        // its corresponding path length
        TSP.putSubset(TSP.nextSubsetId, TSP.nextSubset);
        TSP.a[TSP.nextSubsetId - 1][0] = TSP.INFINITY;
        TSP.nextSubsetId++;
    }

    /**
     * Puts the given subset and its id in the subsets hashmap, and adds the
     * corresponding values of the sizeSubsets and subsetSize hashmaps for
     * faster retrieval.
     * @param subsetId Id of the subset to add.
     * @param subset Subset of vertices ids to add.
     */
    private static void putSubset(int subsetId, Set<Integer> subset)
    {
        TSP.subsets.put(subsetId, subset);
        int size = subset.size();
        List<Integer> subsetIds = TSP.sizeSubsets.remove(size);
        if(subsetIds == null)
        {
            subsetIds = new ArrayList<Integer>();
        }
        subsetIds.add(subsetId);
        TSP.sizeSubsets.put(size, subsetIds);
        TSP.subsetSize.put(subsetId, size);
    }

    /**
     * Gets the minimum length path from 1 to j that contains precisely the
     * vertices of the given set.
     * @param graph Graph to select edge costs from.
     * @param setId Set of id of set to traverse.
     * @param j Destination vertex id.
     * @return Minimum length of a path from 1 to j that visits precisely the
     * vertices of the given set.
     */
    private static double getMinimumLengthPath(Graph graph, Integer setId, int j)
    {
        double min = TSP.INFINITY;
        for(Integer k : TSP.subsets.get(setId))
        {
            if(k != j)
            {
                List<Edge> edges = graph.getEdgesLeaving(j);
                for(Edge e : edges)
                {
                    min = Math.min(TSP.a[setId - 1][k - 1] + e.getCost(), min);
                }
            }
        }
        return min;
    }

    /**
     * Finds and returns the minimum-length path from 1 to itself after
     * visiting every vertex once.
     * <b>Pre:</b> 2-D array a has been filled out for all destinations j
     * in {1,2,...,n}
     * @param graph Graph to select edge costs from.
     * @param n Number of vertices of the graph.
     * @return Minimum-length of a path from 1 to itself that visits every
     * vertex once.
     */
    private static double getLengthAfterFinalHop(Graph graph, int n)
    {
        double min = TSP.INFINITY;
        for(int j = 1; j < n; j++)
        {
            List<Edge> edges = graph.getEdgesLeaving(j);
            for(Edge e : edges)
            {
                min = Math.min(TSP.a[n - 1][j - 1] + e.getCost(), min);
            }
        }
        return min;
    }
}
