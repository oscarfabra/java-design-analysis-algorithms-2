/**
 * $Id: Solver.java, v 1.0 17/07/14 21:14 oscarfabra Exp $
 * {@code Solver} Is a class that reads and solves a greedy algorithm for
 * computing the minimum spanning tree of a given undirected graph using
 * Kruskal's algorithm.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 17/07/14
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class that reads and solves a greedy algorithm for computing the minimum
 * spanning tree (MST) of a given undirected graph using Kruskal's algorithm.
 */
public class Solver
{
    //-------------------------------------------------------------------------
    // PUBLIC METHODS
    //-------------------------------------------------------------------------

    /**
     * Solves the given instance and prints the solution in standard output.
     * @param lines Input list with the variables for the problem.
     */
    private static void solve(List<String> lines)
    {
        // Gets the first line from the list
        String firstLine = lines.remove(0);
        String [] nAndM = firstLine.split(" ");
        Integer n = Integer.parseInt(nAndM[0]);
        Integer m = Integer.parseInt(nAndM[1]);

        // Builds the adj list of edges of each vertex from the given lines
        Map<Integer,List<Edge>> vertexEndpoints =
                Graph.buildVertexEdges(m, lines);

        // Creates a new graph
        Graph graph = new Graph(n, vertexEndpoints);

        // Finds the MST of the given graph using Kruskal's straightforward
        // implementation
        long cost = Kruskal.solveStraightforward(graph);

        // Prints the answer in standard output
        System.out.println("The cost of the minimum spanning tree (MST) of " +
                "the given graph is: " + cost);
    }

    /**
     * Reads the lines received from standard input and arranges them in a
     * list.
     * @param args Array of String with the filepath of the file to read.
     * @return A list of lines with the data to solve the problem.
     * @throws java.io.FileNotFoundException If file couldn't be found.
     */
    private static List<String> readLines(String[] args)
            throws FileNotFoundException
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
     */
    public static void main(String [] args)
    {
        List<String> lines = null;
        try
        {
            lines = Solver.readLines(args);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        Solver.solve(lines);
    }
}
