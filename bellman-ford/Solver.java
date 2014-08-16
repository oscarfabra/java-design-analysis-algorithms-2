/**
 * $Id: Solver.java, v1.0 14/08/14 05:59 PM oscarfabra Exp $
 * {@code Solver} Is a class that reads and solves Bellman-Ford's singe-source
 * shortest path algorithm of a given directed graph.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 14/08/14
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Solves Bellman-Ford's single-source, shortest path algorithm defined as
 * follows:
 * Input: Directed graph G = (V , E), edge lengths c_e for each e in E, and
 * source vertex s in V [assumes no parallel edges].
 * Goal: For every destination v in V, compute the length of a shortest s - v
 * path.
 */
public class Solver
{
    //-------------------------------------------------------------------------
    // CLASS VARIABLE
    //-------------------------------------------------------------------------

    // Stores the source vertex obtained from second program argument
    private static int s = 0;

    //-------------------------------------------------------------------------
    // PRIVATE METHODS
    //-------------------------------------------------------------------------

    /**
     * Solves the given instance and prints the solution in standard output.
     * @param lines Input list with the variables for the problem.
     */
    private static void solve(List<String> lines)
    {
        // Gets the number of vertices n and number of edges m from first line
        String firstLine = lines.remove(0);
        String [] nAndM = firstLine.split(" ");
        int n = Integer.parseInt(nAndM[0]);
        int m = Integer.parseInt(nAndM[1]);

        // Builds the list of adjacent edges of each vertex
        Map<Integer, List<Edge>> vertexEdges =
                Graph.buildVertexEdges(m, lines);

        // Creates a new graph with the given input
        Graph graph = new Graph(n, vertexEdges);

        // Computes the shortest-path using Bellman-Ford's algorithm, gets
        // the values of the shortest paths from s to all other vertices
        int [] lengths = BellmanFord.solve(Solver.s, graph);

        // Prints results found, if any
        if(lengths == null)
        {
            System.out.println("Negative cycle found; can't find shortest " +
                    "paths.");
        }
        else
        {
            System.out.println("The lengths of the shortest paths from vertex "+
                    Solver.s + " to all other vertices are:");
            for(int i = 0; i < n; i++)
            {
                System.out.println("-- To " + (i + 1) + ": " + lengths[i]);
            }
        }
    }

    /**
     * Reads the lines received from standard input and arranges them in a
     * list.
     * @param args Array of String with the filepath of the file to read.
     * @return A list of lines with the data to solve the problem.
     * @throws Exception If file couldn't be found or s not given.
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

        // Gets the source vertex from the second argument, throws exception
        // if invalid or not found
        try
        {
            Solver.s = Integer.parseInt(args[1]);
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
     *             Source vertex s as second parameter, s in [1,2,...,n]
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
