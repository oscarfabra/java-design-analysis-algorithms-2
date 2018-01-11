/**
 * $Id: Solver.java, v 1.0 06/09/14 22:03 oscarfabra Exp $
 * {@code Solver} Is a class that reads and solves the vertex cover problem for
 * a given file with a representation of an undirected graph.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 06/09/14
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Solver reads and solves the vertex cover problem for a given representation
 * of an undirected graph.
 */
public class Solver
{
    //-------------------------------------------------------------------------
    // PRIVATE METHODS
    //-------------------------------------------------------------------------

    /**
     * Solves the given instance and prints the solution in standard output.
     * @param lines Input list with the variables for the problem.
     */
    private static void solve(List<String> lines)
    {
        String nAndM = lines.remove(0);
        String [] values = nAndM.split(" ");
        int n = Integer.valueOf(values[0]);
        int m = Integer.valueOf(values[1]);

        // Builds the adjacency lists to construct the graph
        Map<Integer, List<Edge>> vertexEndpoints =
                Graph.buildVertexEdges(m, lines);

        // Creates a new graph with the given values
        Graph graph = new Graph(n, vertexEndpoints);

        // Finds the vertex cover
        List<Vertex> vertices = VertexCover.solve(graph);

        // Shows the ids of the corresponding vertices in standard output
        System.out.println("The minimum-cardinality vertex cover of the " +
                "given graph comprises the following vertices: ");
        for(Vertex vertex : vertices)
        {
            System.out.println("-- " + vertex.getId());
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
