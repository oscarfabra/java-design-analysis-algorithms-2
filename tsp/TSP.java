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

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
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
    private static float [][] a;

    // Stores the id of the minimum-cost length set for each sub-problem size,
    // size in [1,2,...,n]
    private static int [] x;

    // Used to store in memory current subset for faster retrieval
    private static Map<Integer, Set<Integer>> currentSubset;

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
     * @throws IOException If file with a subset couldn't be read or written.
     */
    public static float solve(Graph graph) throws IOException
    {
        // Initializes corresponding data structures
        System.out.print("-- Initializing data structures for solving TSP...");
        int n = graph.getN();
        int subsetsNumber = (int)Math.pow(2, n - 1);
        TSP.a = new float[subsetsNumber][n];
        TSP.x = new int[n];
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
        TSP.a[nextSubsetId - 1][0] = 0;
        TSP.x[0] = nextSubsetId;
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
                TSP.a[nextSubsetId - 1][0] = TSP.INFINITY;
                nextSubsetId++;
            }
            // Message in standard output for logging purposes
            System.out.println("-- [Subsets of size " + k + " set up.]");
            subsets.clear();
        }
        System.out.println("-- ...finished setting up base cases.");

        // Walks through each possible subset S of {1,2,...,n} looking for all
        // possible destinations j in {1,2,...,n}
        System.out.println("-- Looking for minimum-cost cycle that visits " +
                "each vertex exactly once...");
        Files.delete(Paths.get("data/ss_" + 1 + ".txt"));
        for(int m = 2; m <= n; m++)
        {
            // Saves min length of a set without j
            float minLengthWithoutJ = TSP.INFINITY;

            // For each subset of size m that contains 1...
            List<Integer> setIds = TSP.sizeSubsets.get(m);
            for(Integer setId : setIds)
            {
                // ...walks through each vertex j of the set looking for the
                // minimum length of a path from 1 to j that visits precisely
                // the vertices of the set with id setId
                Set<Integer> currentSubsetAux = TSP.getSubset(setId);
                for(Integer j : currentSubsetAux)
                {
                    if(j != 1)
                    {
                        TSP.a[setId - 1][j - 1] =
                            TSP.getMinimumLengthPath(graph, TSP.x[m - 2], setId, j);

                        // If length is shortest, save id of this set without j
                        if(TSP.a[setId - 1][j - 1] < minLengthWithoutJ)
                        {
                            minLengthWithoutJ = TSP.a[setId - 1][j - 1];
                            TSP.x[m - 1] = setId;
                        }
                    }
                }
                // Clears stored subset
                TSP.currentSubset = null;
            }

            // Message in standard output for logging purposes
            System.out.println("-- [Minimum-path of size " + m + " found.]");

            // Deletes the file with the subsets of size m to save memory
            Files.delete(Paths.get("data/ss_" + m + ".txt"));
        }
        System.out.println("-- ...minimum-cost cycle found.");



        // Finds and returns the minimum-length path from 1 to itself visiting
        // every vertex once
        return TSP.getLengthAfterFinalHop(graph, TSP.x[n - 1], n);
    }

    //-------------------------------------------------------------------------
    // PRIVATE HELPER METHODS
    //-------------------------------------------------------------------------

    /**
     * Finds all permutations of a given string.
     * @param prefix Prefix to add to permutations.
     * @param str String to find the permutations for.
     */
    private static void permutation(String prefix, String str)
    {
        // TODO: Customize function for this problem and write result to a file.
        int n = str.length();
        if (n == 0)
        {
            System.out.println(prefix);
        }
        else
        {
            for (int i = 0; i < n; i++)
            {
                permutation(prefix + str.charAt(i),
                        str.substring(0, i) + str.substring(i+1, n));
            }
        }
    }

    /**
     * Gets the minimum length path from 1 to j that contains precisely the
     * vertices of the given set.
     * @param graph Graph to select edge costs from.
     * @param setWithoutJ Id of set without J.
     * @param setId Id of the set that needs to be traversed.
     * @param j Destination vertex id.
     * @return Minimum length of a path from 1 to j that visits precisely the
     * vertices of the given set.
     * @throws FileNotFoundException If file with subset couldn't be found.
     */
    private static float getMinimumLengthPath(Graph graph, Integer setWithoutJ,
                                              int setId, int j) throws FileNotFoundException {
        float min = TSP.INFINITY;
        List<Edge> edges = graph.getEdgesArriving(j);
        Set<Integer> subset = TSP.currentSubset.get(setId);
        for(Edge e : edges)
        {
            int k = e.getTail();
            if(k != j && subset.contains(k))
            {
                float candidate = (TSP.a[setWithoutJ - 1][k - 1] == TSP.INFINITY)?
                        e.getCost() : TSP.a[setWithoutJ - 1][k - 1] + e.getCost();
                min = Math.min(candidate, min);
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
     * @param setId Id of the set that has the min-cost from 1 to j visiting
     *              every vertex exactly once.
     * @param n Number of vertices of the graph.
     * @return Minimum-length of a path from 1 to itself that visits every
     * vertex once.
     */
    private static float getLengthAfterFinalHop(Graph graph, Integer setId,
                                                int n)
    {
        float min = TSP.INFINITY;
        for(int j = 2; j <= n; j++)
        {
            List<Edge> edges = graph.getEdgesArriving(1);
            for(Edge e : edges)
            {
                float candidate = (TSP.a[setId - 1][j - 1] == TSP.INFINITY)?
                        e.getCost() : TSP.a[setId - 1][j - 1] + e.getCost();
                min = Math.min(candidate, min);
            }
        }
        return min;
    }

    /**
     * Puts the given subset and its id in the subsets hashmap, and adds the
     * corresponding values of the sizeSubsets and subsetSize hashmaps for
     * faster retrieval.
     * @param subsetId Id of the subset to add.
     * @param subset Subset of vertex ids to add.
     * @throws IOException If file to write the subset in couldn't be opened.
     */
    private static void putSubset(int subsetId, Set<Integer> subset) throws IOException
    {
        int size = subset.size();
        // Writes subset to file. filename includes subset size.
        // For each line, first value is subsetId,
        // subsequent values are corresponding vertices.
        String filename = "data/ss_" + size + ".txt";
        FileWriter fileWriter = new FileWriter(filename, true);
        BufferedWriter output = new BufferedWriter(fileWriter);
        output.write(String.valueOf(subsetId) + " ");
        for(Integer vId : subset)
        {
            output.write(vId.toString() + " ");
        }
        output.write('\n');
        output.close();

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
     * Reads the file with the subset with the given Id and returns it.
     * @param subsetId Id of the subset to look for.
     * @return Set of vertices of the subset with the given setId.
     * @throws FileNotFoundException If file couldn't be found.
     */
    private static Set<Integer> getSubset(Integer subsetId) throws FileNotFoundException
    {
        if (TSP.currentSubset != null && TSP.currentSubset.containsKey(subsetId))
        {
            return TSP.currentSubset.get(subsetId);
        }

        // Read subset vertices from file
        int size = TSP.subsetSize.get(subsetId);
        String filename = "data/ss_" + size + ".txt";
        FileReader fileReader = new FileReader(filename);
        BufferedReader input = new BufferedReader(fileReader);

        TSP.currentSubset = new HashMap<Integer, Set<Integer>>();
        Set<Integer> subset = new TreeSet<Integer>();

        try
        {
            boolean found = false;
            while(!found)
            {
                String line = input.readLine();
                String[] subsetValues = line.split(" ");
                //Integer setId = Integer.parseInt(subsetValues[0]);
                if(subsetValues[0].equalsIgnoreCase(String.valueOf(subsetId)))
                {
                    for(int i = 1; i < subsetValues.length; i++)
                    {
                        subset.add(Integer.parseInt(subsetValues[i]));
                    }
                    TSP.currentSubset.put(subsetId, subset);
                    found = true;
                }
            }
            input.close();
            fileReader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return TSP.currentSubset.get(subsetId);
    }
}
