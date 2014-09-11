/**
 * $Id: OptimalBST.java, v1.0 6/09/14 10:00 PM oscarfabra Exp $
 * {@code OptimalBST} Is a class that computes an Optimal Search Tree given
 * a set of frequencies for each item.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 6/09/14
 */

import java.util.TreeMap;

/**
 * OptimalBST is a class that solves the Optimal Binary Search Tree Problem
 * defined as follows:
 * Input: Frequencies p_1,p_2,...,p_n for items 1,2,...,n.
 * Output: A valid search tree that minimizes the weighted (average) search
 * time.
 */
public class OptimalBST
{
    //-------------------------------------------------------------------------
    // CONSTRUCTORS
    //-------------------------------------------------------------------------

    private OptimalBST(){ }     // This class shouldn't be instantiated.

    //-------------------------------------------------------------------------
    // PUBLIC METHODS
    //-------------------------------------------------------------------------

    /**
     * Computes and returns a valid search tree that minimizes the weighted
     * (average) search time. Assumes items in sorted order, 1 < 2 < 3 ... < n.
     * @param p Array of frequencies for each item in {1,2,...,n}
     * @param n Number of items to add to the tree.
     * @return Valid search tree that minimizes the average search time.
     */
    public static TreeMap<Integer, Integer> solve(double [] p, int n)
    {

        // TODO: Write dynamic programming O(n²) alg...
        return new TreeMap<Integer, Integer>();
    }

    //-------------------------------------------------------------------------
    // PRIVATE HELPER METHODS
    //-------------------------------------------------------------------------

}
