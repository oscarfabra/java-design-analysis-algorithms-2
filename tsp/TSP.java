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
    // j in {1,2,...,n}. a[size].get(j) stores the minimum length of a path from 1
    // to j that visits precisely the vertices of S; S being the minimum-cost
    // subset that contains 1 and j
    private static List<Float>[] a;

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
    public static float solve(Graph graph)
    {
        // Initializes corresponding data structures
        System.out.print("-- Initializing data structures for solving TSP...");
        int n = graph.getN();
        int subsetsNumber = (int)Math.pow(2, n - 1);
        TSP.a = (ArrayList<Float>[])new ArrayList[subsetsNumber];
        for(int i = 0; i < subsetsNumber; i++)
        {
            TSP.a[i] = new ArrayList<Float>(n);
            for(int j = 0; j < n; j++)
            {
                TSP.a[i].add((float) 0);
            }
        }
        TSP.subsets = new HashMap<Integer, Set<Integer>>(n);
        TSP.sizeSubsets = new HashMap<Integer, List<Integer>>(n);
        TSP.subsetSize = new HashMap<Integer, Integer>(n);

        // Creates the subset List to find the combinations for, and an
        // auxiliary subset for later retrieval
        List<Integer> subsetList = new ArrayList<Integer>(n);
        for(int i = 1; i <= n; i++)
        {
            subsetList.add(i);
        }
        System.out.println("done.");

        // Message in standard output for logging purposes
        System.out.println("-- Finding and setting up base cases for all " +
                "subsets that contain 1...");
        // Fills first base case according to possible subset sizes
        Set<Integer> tempSubset = new TreeSet<Integer>();
        tempSubset.add(1);
        int nextSubsetId = 1;
        TSP.putSubset(nextSubsetId, tempSubset);
        TSP.setLength(nextSubsetId - 1, 0, 0);
        nextSubsetId++;
        // Walks through the 2-D array a initializing base cases
        for(int k = 2; k <= n; k++)
        {
            // Gets all possible subsets of size k that contain 1
            List<Set<Integer>> subsets =
                    Combinations.solveWithV(subsetList, k, 1);
            // Puts each subset that contains 1 in its corresponding collection
            // and initializes its corresponding path length
            for(Set subset : subsets)
            {
                TSP.putSubset(nextSubsetId, subset);
                TSP.setLength(nextSubsetId - 1, 0, TSP.INFINITY);
                nextSubsetId++;
            }
            // Message in standard output for logging purposes
            System.out.println("-- [Subsets of size " + k + " set up.]");
        }
        System.out.println("-- ...finished setting up base cases.");

        // Walks through each possible subset S of {1,2,...,n} looking for all
        // possible destinations j in {1,2,...,n}
        System.out.println("-- Looking for the minimum-cost cycle that visits " +
                "each vertex exactly once...");
        for(int m = 2; m <= n; m++)
        {
            // For each subset of size m that contains 1...
            List<Integer> setIds = TSP.sizeSubsets.get(m);
            for(Integer setId : setIds)
            {
                // ...walks through each vertex j of the set looking for the
                // minimum length of a path from 1 to j that visits
                // precisely the vertices of the set with id setId
                Set<Integer> set = TSP.subsets.get(setId);
                Iterator<Integer> iterator = set.iterator();
                while(iterator.hasNext())
                {
                    int j = iterator.next();
                    if(j != 1)
                    {
                        TSP.setLength(setId - 1, j - 1,
                                    TSP.getMinimumLengthPath(graph, setId, j));
                    }
                }
            }
            // Message in standard output for logging purposes
            System.out.println("-- [Minimum-path of size " + m + " found.]");
        }
        System.out.println("-- ...minimum-cost cycle that visits each vertex " +
                "exactly once found.");

        // Finds and returns the minimum-length path from 1 to itself visiting
        // every vertex once
        return TSP.getLengthAfterFinalHop(graph, n);
    }

    //-------------------------------------------------------------------------
    // PRIVATE HELPER METHODS
    //-------------------------------------------------------------------------

    /**
     * Gets the minimum length path from 1 to j that contains precisely the
     * vertices of the given set.
     * @param graph Graph to select edge costs from.
     * @param setId Set of id of set to traverse.
     * @param j Destination vertex id.
     * @return Minimum length of a path from 1 to j that visits precisely the
     * vertices of the given set.
     */
    private static float getMinimumLengthPath(Graph graph, Integer setId, int j)
    {
        float min = TSP.INFINITY;
        List<Edge> edges = graph.getEdgesArriving(j);
        for(Edge e : edges)
        {
            int k = e.getTail();
            if(k != j)
            {
                float currentLength = TSP.getLength(setId - 1, k - 1);
                if(currentLength == TSP.INFINITY)
                {
                    min = Math.min(TSP.INFINITY, min);
                }
                else
                {
                    min = Math.min(currentLength + e.getCost(), min);
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
    private static float getLengthAfterFinalHop(Graph graph, int n)
    {
        float min = TSP.INFINITY;
        // Last set is the one that includes exactly {1,2,...,n}
        int lastSetId = TSP.subsets.size();
        for(int j = 2; j <= n; j++)
        {
            List<Edge> edges = graph.getEdgesArriving(1);
            for(Edge e : edges)
            {
                float length = TSP.getLength(lastSetId - 1, j - 1);
                if(length == TSP.INFINITY)
                {
                    min = Math.min(TSP.INFINITY, min);
                }
                else
                {
                    min = Math.min(length + e.getCost(), min);
                }
            }
        }
        return min;
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
     * Sets the given value in the specified position of 2-D array a.
     * @param rowIndex Set to assign the value to, rowIndex in
     *                 [0,1,...,setsNumber-1]
     * @param columnIndex Position in the chained list to assign the value to,
     *                    columnIndex in [0,1,...,n - 1]
     * @param value Number to set in the given position of the 2-D array a.
     */
    private static void setLength(int rowIndex, int columnIndex, float value)
    {
        ArrayList<Float> vertices = (ArrayList<Float>) TSP.a[rowIndex];
        vertices.set(columnIndex, value);
        TSP.a[rowIndex] = vertices;
    }

    /**
     * Gets the value in the specified position of 2-D array a.
     * @param rowIndex Set to get the value from, rowIndex in
     *                 [0,1,...,setsNumber-1]
     * @param columnIndex Position in the chained list to get the value from,
     *                    columnIndex in [0,1,...,n - 1]
     * @return Number at the specified position of the 2-D array a.
     */
    private static float getLength(int rowIndex, int columnIndex)
    {
        ArrayList<Float> vertices = (ArrayList<Float>) TSP.a[rowIndex];
        return vertices.get(columnIndex);
    }
}
