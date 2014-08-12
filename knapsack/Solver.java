/**
 * $Id: Solver.java, v1.0 9/08/14 05:36 PM oscarfabra Exp $
 * {@code Solver} Is a class that reads and solves an instance of the popular
 * knapsack problem using a dynamic programming approach.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 9/08/14
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Solver is a class that reads and solves an instance of the popular knapsack
 * problem using a dynamic programming approach.
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
        // Gets knapsack size and number of items from first line
        String firstLine = lines.remove(0);
        String [] wAndN = firstLine.split(" ");
        int W = Integer.parseInt(wAndN[0]);
        int n = Integer.parseInt(wAndN[1]);

        // Builds a list of items as an input to solve the problem
        List<Item> items = Knapsack.buildItems(lines, n);

        // Finds the value of the optimal solution given the list of items and
        // knapsack size; leaves the items to select in the items list
        int value = Knapsack.solve(items, W, n);

        // Prints the value of the optimal solution
        System.out.println("The value of the optimal solution is: " + value);

        // Prints the solution only for small n (for BIG n it probably can't be
        // stored in memory, so it is not)
        long compare = Long.valueOf(W + 1) * Long.valueOf(n + 1);
        if(compare <= Knapsack.THRESHOLD)
        {
            System.out.println("The items to select are the following: ");
            items = Knapsack.getSelectedItems();
            for(int i = 0; i < items.size(); i++)
            {
                Item item = items.get(i);
                System.out.println(item.getValue() + " " + item.getWeight());
            }

        }
        System.out.println();
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
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Solver.solve(lines);
    }
}
