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

/**
 * Class that reads and solves Dijkstra's shortest path algorithm from a file
 * with the variables of a directed graph.
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
        // Gets an array of lists of edges from the given lines. edgesArray[i]
        // contains the list of edges whose tail is vertex with id i + 1, i in
        // [0...n-1]
        List<Edge>[] edgesArray = Graph.readEdgesArray(lines);

        // Gets the length of the array
        int n = edgesArray.length;

        // Creates a new Graph object with the given parameters
        Graph graph = new Graph(n, edgesArray);

        // Computes the shortest paths from vertex 1 to all other vertices in
        // the given graph. paths[i] contains the path from vertex 1 to vertex
        // with id i + 1, i in [0...n-1]
        int [] paths = Dijkstra.solve(1, graph);

        System.out.println("The lengths of the shortest paths from vertex 1 " +
                "to vertices 7, 37, 59, 82, 99, 115, 133, 165, 188, 197 are:");
        System.out.print(paths[6] + ", " + paths[36] + ", ");
        System.out.print(paths[58] + ", " + paths[81] + ", ");
        System.out.print(paths[98] + ", " + paths[114] + ", ");
        System.out.print(paths[132] + ", " + paths[164] + ", ");
        System.out.print(paths[187] + ", " + paths[196] + ".");
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

        return lines;
    }

    //-------------------------------------------------------------------------
    // MAIN
    //-------------------------------------------------------------------------

    /**
     * Main test method.
     * @param args filepath relative to the file with the representation of a
     *             directed graph in the form -file=filepath
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
