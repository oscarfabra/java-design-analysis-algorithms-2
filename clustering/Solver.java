/**
 * $Id: Solver.java, v 1.0 17/07/14 21:14 oscarfabra Exp $
 * {@code Solver} Is a class that reads and solves a greedy algorithm for
 * computing a max-spacing k-clustering.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 17/07/14
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Class that reads and solves a greedy algorithm for computing a max-spacing
 * k-clustering.
 */
public class Solver
{
    //-------------------------------------------------------------------------
    // CLASS VARIABLES
    //-------------------------------------------------------------------------

    // Number of clusters or spacing desired read from standard input.
    // For a cost edges input, the number of clusters k must be provided.
    // For a hamming distances input, the size of minimum spacing s must be
    // provided.
    private static int kOrSpacing;

    //-------------------------------------------------------------------------
    // PRIVATE METHODS
    //-------------------------------------------------------------------------

    /**
     * Solves the given instance and prints the solution in standard output.
     * @param lines Input list with the variables for the problem.
     */
    private static void solve(List<String> lines)
    {
        // Determines whether it is a problem to solve using hamming distances
        String firstLine = lines.remove(0);
        String [] lineContents = firstLine.split(" ");
        int n = Integer.parseInt(lineContents[0]);
        if(lineContents.length == 1)
        {
            // Solves using cost edges
            int m = lines.size();
            Solver.solveByCostEdges(lines, n, m, Solver.kOrSpacing);
        }
        else
        {
            // Solves using hamming distances
            int bits = Integer.parseInt(lineContents[1]);
            Solver.solveByHammingDistances(lines, n, bits, Solver.kOrSpacing);
        }
    }

    /**
     * Solves the k-clustering problem using the hamming distances and the
     * min-spacing to look for provided as input.
     * @param lines List of bits associated for each node or point.
     * @param n Number of vertices or points of the problem.
     * @param bits Number of bits to assign to each node.
     * @param s Minimum spacing to look for, read from standard input.
     */
    private static void solveByHammingDistances(List<String> lines, int n,
                                                int bits, int s)
    {
        // Creates a matrix with the associated bits for each node
        int [][] nodes = new int[n][bits];
        int i = 0;
        System.out.println("Initializing nodes...");
        for(String line : lines)
        {
            String [] values = line.split(" ");
            for(int j = 0; j < bits; j++)
            {
                nodes[i][j] = Integer.parseInt(values[j]);
            }
            i++;
            // Prints a message in standard output for logging purposes
            if( i % 5000 == 0)
            {
                System.out.println("-- " + i + " nodes so far." );
            }
        }
        System.out.println("...nodes initialized.");

        // Finds the largest value of k to get an spacing of at least s
        int maxK = Clustering.findMaxClustering(nodes, n, bits, s);

        // Prints solution in standard output
        System.out.println("The largest value of k for a min-spacing of " + s +
                " is: "+ maxK);
    }

    /**
     * Solves the max-spacing k-clustering problem using the cost edges
     * approach and prints the solution in standard output.
     * @param lines List of edges containing tail and head vertices, and cost
     *              for each edge.
     * @param n Number of vertices or points of the problem.
     * @param m Number of edges. In this case, they represent the distances
     * @param k Number of clusters desired, read from standard input.
     */
    private static void solveByCostEdges(List<String> lines, int n, int m,
                                         int k)
    {
        // Gets the map of distances between each node and the other nodes
        Map<Integer, List<Edge>> vertexEndpoints =
                Graph.buildVertexEdges(m, lines);

        // Creates a new graph to solve the k-clustering problem
        Graph graph = new Graph(n, vertexEndpoints);

        // Finds the max-spacing of a k-clustering of the distance function
        // represented in the graph
        int maxSpacing = Clustering.findMaxSpacing(graph, k);

        // Prints solution in standard output
        System.out.println("The max-spacing of a " + k +
                "-clustering is: "+ maxSpacing);
    }

    /**
     * Reads the lines received from standard input and arranges them in a
     * list.
     * @param args Array of String with the filepath of the file to read.
     * @return A list of lines with the data to solve the problem.
     * @throws Exception If file couldn't be found or k not given.
     */
    private static List<String> readLines(String[] args)
            throws Exception
    {
        List<String> lines = new Vector<String>();
        String filename = null;

        // Gets the file name
        String arg = args[0];
        if(arg.startsWith("-file="))
        {
            filename = arg.substring(6);
        }

        if(filename == null)
        {
            return null;
        }

        // Reads the lines out of the file
        FileReader fileReader = new FileReader(filename);
        BufferedReader buffer = new BufferedReader(fileReader);
        String line = null;
        try
        {
            while((line = buffer.readLine()) != null)
            {
                lines.add(line);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        // Gets k or minimum spacing as second argument, throws an exception if
        // not found
        try
        {
            Solver.kOrSpacing = Integer.parseInt(args[1]);
        }
        catch(NumberFormatException nfe)
        {
            throw nfe;
        }

        // Returns the lines read
        return lines;
    }

    //-------------------------------------------------------------------------
    // MAIN
    //-------------------------------------------------------------------------

    /**
     * Main test method.
     * @param args filepath relative to the file with the representation of an
     *             undirected graph in the form -file=filepath
     *             For a cost edges problem, the number of clusters k must be
     *             provided as second input.
     *             For a hamming distances problem, the size of minimum spacing
     *             s must be provided as second input.
     */
    public static void main(String [] args)
    {
        List<String> lines = null;
        try
        {
            lines = Solver.readLines(args);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Solver.solve(lines);
    }
}