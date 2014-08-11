/**
 * $Id: Knapsack.java, v1.0 9/08/14 05:38 PM oscarfabra Exp $
 * {@code Knapsack} Solves an instance of the popular knapsack problem.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 9/08/14
 */

import java.util.ArrayList;
import java.util.List;

/**
 * Knapsack is a class that solves the knapsack problem defined as follows:
 * Input: n items. Each has attributes: value v_i (non-negative), size w_i
 * (non-negative and integral), capacity W (a non-negative integer).
 * Output: A subset S of {1, 2, ..., n} that:
 * Maximizes: Sum of v_i's, for all i in S
 * Subject to: Sum of w_i's are upper bounded by W, for all i in S.
 */
public class Knapsack
{
    //-------------------------------------------------------------------------
    // ATTRIBUTES
    //-------------------------------------------------------------------------

    // Constant to determine whether to solve the problem by a straightforward
    // manner or optimized for big numbers.
    private static final int threshold = 1000000;

    // TODO: Add data structures to improve performance for BIG n...

    //-------------------------------------------------------------------------
    // CONSTRUCTORS
    //-------------------------------------------------------------------------

    private Knapsack() {}       // This class shouldn't be instantiated

    //-------------------------------------------------------------------------
    // PUBLIC METHODS
    //-------------------------------------------------------------------------

    /**
     * Builds and returns a list of strings with the value and weight of each
     * item and number of items n.
     * <b>Pre:</b> Each line of the given list of string has the form:
     * [value_i] [weight_i] for each i in {1...n}
     * @param lines List of string with the attributes for each item.
     * @param n Number of items.
     * @return List of items for a particular knapsack problem.
     */
    public static List<Item> buildItems(List<String> lines, int n)
    {
        List<Item> items = new ArrayList<Item>(n);
        int newItemId = 1;
        for(String line : lines)
        {
            String [] valueAndWeight = line.split(" ");
            int value = Integer.parseInt(valueAndWeight[0]);
            int weight = Integer.parseInt(valueAndWeight[1]);
            Item item = new Item(newItemId++, value, weight);
            items.add(item);
        }
        return items;
    }

    /**
     * Solves the given instance of the knapsack problem.
     * @param items List of items.
     * @param W Knapsack size.
     * @param n Number of items.
     * @return Value of the optimal solution, leaving the selected items in the
     * given items list.
     */
    public static int solve(List<Item> items, int W, int n)
    {
        // Determines whether to solve the problem using a straightforward
        // implementation or one that's optimized for big numbers
        int value = 0;
        if(Knapsack.threshold >= W * n)
        {
            value = Knapsack.solveStraightforward(items, W, n);
        }
        else
        {
            value = Knapsack.solveForBigData(items, W, n);
        }
        // Returns the value of the optimal solution
        return value;
    }

    //-------------------------------------------------------------------------
    // PRIVATE HELPER METHODS
    //-------------------------------------------------------------------------

    /**
     * Solves the knapsack problem using a straightforward implementation.
     * @param items List of items.
     * @param W Knapsack size.
     * @param n Number of items.
     * @return Value of the optimal solution, leaving the selected items in the
     * given items list.
     */
    private static int solveStraightforward(List<Item> items, int W, int n)
    {
        // TODO: Straightforward solution...

        return 0;
    }

    /**
     * Solves the knapsack problem using an implementation optimized for big n.
     * @param items List of items.
     * @param W Knapsack size.
     * @param n Number of items.
     * @return Value of the optimal solution, leaving the selected items in the
     * given items list.
     */
    private static int solveForBigData(List<Item> items, int W, int n)
    {
        // TODO: Straightforward solution...

        return 0;
    }
}
