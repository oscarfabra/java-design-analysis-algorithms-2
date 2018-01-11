/**
 * $Id: Solver.java, v 1.0 24/06/14 14:38 oscarfabra Exp $
 * {@code Solver} Is a class that reads and solves Dijkstra's shortest path
 * algorithm from a file with the variables of a directed graph.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 24/06/14
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class that reads and solves Dijkstra's shortest path algorithm from a file
 * with the variables of a directed graph.
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
        Map<Integer,List<Edge>> vertexEdges = Graph.buildVertexEdges(m, lines);

        // Creates a new Graph object with the given parameters
        Graph graph = new Graph(n, vertexEdges);

        // Computes the shortest paths from vertex s to all other vertices
        // using Dijkstra's O(m log n) algorithm using heaps
        int [] lengths = Dijkstra.solve(Solver.s, graph);

        System.out.println("The lengths of the shortest paths from vertex "+
                Solver.s + " to all other vertices are:");
        for(int i = 0; i < n; i++)
        {
            System.out.println("-- To " + (i + 1) + ": " + lengths[i]);
        }
    }

    /**
     * Reads the lines received from standard input and arranges them in a
     * list.
     * @param args Array of String with the filepath of the file to read.
     * @return A list of lines with the data for the problem.
     * @throws FileNotFoundException If the file couldn't be found.
     */
    private static List<String> readLines(String[] args)
            throws FileNotFoundException
    {
        List<String> lines = new ArrayList<String>();
        String filename = null;

        // get the file name
        for(String arg : args)
        {
            if(arg.startsWith("-file="))
            {
                filename = arg.substring(6);
            }
        }
        if(filename == null)
        {
            return null;
        }
        // Reads the lines out of the file
        FileReader fileReader = new FileReader(filename);
        BufferedReader input = new BufferedReader(fileReader);
        String line = null;
        try
        {
            while((line = input.readLine()) != null)
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
     * @param args filepath relative to the file with the representation of a
     *             directed graph in the form -file=filepath
     *             Source vertex s as second parameter, s in [1,2,...,n]
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
