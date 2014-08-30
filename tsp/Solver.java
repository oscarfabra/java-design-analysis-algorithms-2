/**
 * $Id: Solver.java, v 1.0 26/08/14 23:49 oscarfabra Exp $
 * {@code Solver} Is a class that reads and solves an instance of the popular
 * NP-Complete problem "Traveling Salesman Problem".
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 26/08/14
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Solver is a class that reads and solves an instance of the popular
 * NP-Complete problem "Traveling Salesman Problem" given a file with a
 * representation of an undirected graph with non-negative edge costs.
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
        // Gets the number of cities n
        String firstLine = lines.remove(0);
        int n = Integer.parseInt(firstLine);

        // Reads the coordinates (x,y) of each city from the lines list
        double [][] coord = new double[n][2];
        for(int i = 0; i < n; i++)
        {
            String [] xAndY = lines.get(i).split(" ");
            coord[i][0] = Double.parseDouble(xAndY[0]);
            coord[i][1] = Double.parseDouble(xAndY[1]);
        }

        // Finds the euclidean distance between cities, forming the edges list
        int m = 0;
        List<String> edges = new ArrayList<String>(n);
        for(int i = 0; i < n; i++)
        {
            for(int j = i + 1; j < n; j++)
            {
                // Finds euclidean distance, creates edges Strings, and adds
                // them to edges list
                double d = Math.sqrt(Math.pow(coord[i][0] - coord[j][0], 2) +
                        Math.pow(coord[i][1] - coord[j][1], 2));
                StringBuilder sb = new StringBuilder();
                sb.append(i + 1).append(" ");
                sb.append(j + 1).append(" ").append(d);
                String edge1 = sb.toString();
                sb = new StringBuilder();
                sb.append(j + 1).append(" ");
                sb.append(i + 1).append(" ").append(d);
                String edge2 = sb.toString();
                edges.add(edge1);
                edges.add(edge2);
                m+=2;
            }
        }

        // Builds the list of adjacent edges of each vertex
        Map<Integer,List<Edge>> vertexEdges = Graph.buildVertexEdges(m, lines);

        // Creates a new Graph object with the given parameters
        Graph graph = new Graph(n, vertexEdges);

        // Computes the minimum-cost cycle that visits every vertex only once
        double length = TSP.solve(graph);

        // Prints results in standard output
        System.out.println("The cost of a minimum-length cycle that visits " +
                "every vertex exactly once is: " + length);
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
