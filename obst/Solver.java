/**
 * $Id: Solver.java, v 1.0 06/09/14 22:01 oscarfabra Exp $
 * {@code Solver} Reads and computes an optimal binary tree given a file with
 * the probabilities of search for each item.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 06/09/14
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Solver is a class that reads and computes an optimal binary search tree
 * given a file with a list of probabilities of search for each item.
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
        // Reads contents of lines list
        int n = Integer.parseInt(lines.remove(0));
        double [] p = new double[n];
        int i = 0;
        Iterator<String> iterator = lines.iterator();
        while(iterator.hasNext())
        {
            p[i++] = Double.parseDouble(iterator.next());
        }

        // Computes the optimal binary search tree
        Map<Integer, Integer> obst = OptimalBST.solve(p, n);

        // Prints the resulting BST in standard output
        System.out.println("The Optimal Binary Search Tree (OBST) is:");
        Set<Integer> keys = obst.keySet();
        for(Integer key : keys)
        {
            Integer item = obst.get(key);
            System.out.println("-- Key: " + key + ", Item: " + item);
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
