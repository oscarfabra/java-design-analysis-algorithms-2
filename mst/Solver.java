/**
 * $Id: Solver.java, v 1.0 03/07/14 20:38 oscarfabra Exp $
 * {@code Solver} Is a class that reads and solves a greedy algorithm for
 * computing the minimum spanning tree of a given undirected graph.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 03/07/14
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * class that reads and solves a greedy algorithm for computing the minimum
 * spanning tree (MST) of a given undirected graph.
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
        // Gets the number of vertices and edges from the first line
        String firstLine = lines.remove(0);
        String [] values = firstLine.split(" ");
        int n = Integer.parseInt(values[0]);
        int m = Integer.parseInt(values[1]);

        // Builds the adjacency list of each vertex from the given lines
        Map<Integer, List<Edge>> vertexEndpoints =
                Graph.buildVertexEdges(m, lines);

        // Creates a new graph
        Graph graph = new Graph(n, vertexEndpoints);

        // Finds the Minimum Spanning Tree of the given graph
        List<Integer> mst = new ArrayList<Integer>(graph.getN());
        long cost = MST.solve(graph, mst);

        // Prints the answer in standard output
        System.out.println("The overall cost of the Minimum Spanning Tree " +
                "of the given graph is: " + cost);
        System.out.println("The MST comprise the following vertices: ");
        for(int i = 0; i < mst.size(); i++)
        {
            System.out.print(mst.get(i));
            System.out.println((i == mst.size() - 1)?", ":".");
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
