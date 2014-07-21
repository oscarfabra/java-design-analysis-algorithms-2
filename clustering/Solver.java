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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class that reads and solves a greedy algorithm for computing a max-spacing
 * k-clustering.
 */
public class Solver
{
    //-------------------------------------------------------------------------
    // CLASS VARIABLES
    //-------------------------------------------------------------------------

    // Number of clusters desired read from standard input
    private static int k;

    //-------------------------------------------------------------------------
    // PUBLIC METHODS
    //-------------------------------------------------------------------------

    /**
     * Solves the given instance and prints the solution in standard output.
     * @param lines Input list with the variables for the problem.
     */
    private static void solve(List<String> lines)
    {
        // Gets the number of vertices or nodes n, and the number of edges m
        int n = Integer.parseInt(lines.remove(0));
        int m = lines.size();

        // Gets the map of distances between each node and the other nodes
        Map<Integer, List<Edge>> vertexEndpoints =
                Graph.buildVertexEdges(m, lines);

        // Creates a new graph to solve the k-clustering problem
        Graph graph = new Graph(n, vertexEndpoints);

        // Finds the max-spaacing of the k-clustering of the distance function
        // represented in the graph
        int maxSpacing = Clustering.findMaxSpacing(graph,Solver.k);

        // Prints solution in standard output
        System.out.println("The max-spacing of a " + Solver.k +
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
        List<String> lines = new ArrayList<String>();
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

        // Reads second argument, throws exception if not an integer
        try
        {
            Solver.k = Integer.parseInt(args[1]);
        }
        catch(NumberFormatException nfe)
        {
            throw nfe;
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
     *             Number of clusters desired k.
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