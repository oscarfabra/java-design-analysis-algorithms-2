/**
 * $Id: QuickScores.java, v 1.0 02/07/2014 22:04 oscarfabra Exp $
 * {@code QuickScores} Is a class that sorts a list of Job objects according to
 * their scores, given in the form of an array of double numbers.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 02/07/2014
 */

import java.util.ArrayList;
import java.util.List;

/**
 * Class that sorts a list of Job objects according to the given array of
 * doubles.
 */
public class QuickScores
{
    //-------------------------------------------------------------------------
    // CLASS ATTRIBUTES
    //-------------------------------------------------------------------------

    // Array of jobs to sort the given list in the sortScores() method
    private static List<Job> jobs = null;

    //-------------------------------------------------------------------------
    // CONSTRUCTOR
    //-------------------------------------------------------------------------

    // This class should not be instantiated
    private QuickScores(){ }

    //-------------------------------------------------------------------------
    // PUBLIC CLASS METHODS
    //-------------------------------------------------------------------------

    /**
     * Sorts the given list according to the given array of doubles.
     * @param scores Array of double numbers.
     * @param jobs List of Job objects to sort.
     */
    public static void sortScores(double [] scores, List<Job> jobs)
    {
        // Copies the given list of jobs into the homonym class variable
        QuickScores.jobs = new ArrayList<Job>(jobs.size());
        for(Job job : jobs)
        {
            QuickScores.jobs.add(job);
        }

        // Sorts scores array AND the jobs list
        sort(scores);

        // Updates the given jobs list copying the sorted class variable
        for(int i = 0; i < jobs.size(); i++)
        {
            jobs.set(i, QuickScores.jobs.get(i));
        }
    }

    //-------------------------------------------------------------------------
    // PRIVATE HELPER METHODS
    //-------------------------------------------------------------------------

    /**
     * Sorts the given array using quicksort pivoting over the first element.
     * @param a Array of int numbers.
     */
    private static void sort(double[] a)
    {
        sort(a, 0, a.length - 1);
    }

    /**
     * Prints the elements of the given array in standard output.
     * @param a Array of double numbers.
     */
    private static void show(double[] a)
    {
        for (int i = 0; i < a.length; i++)
        {
            System.out.print(a[i] + " ");
        }
        System.out.println();
    }

    /**
     * Sorts array a in [lb...ub] using the first element as pivot.
     * @param a  Array of int numbers.
     * @param lb Lower-bound index.
     * @param ub Upper-bound index.
     */
    private static void sort(double[] a, int lb, int ub)
    {
        if (lb >= ub)
        {
            return;
        }
        int p = partitionFirst(a, lb, ub);
        sort(a, lb, p - 1);
        sort(a, p + 1, ub);
    }

    /**
     * Partitions the array a in [index...ub] using the first element as pivot.
     * @param a  Array of double numbers.
     * @param lb Lower-bound index.
     * @param ub Upper-bound index.
     * @return Index of the pivot element.
     */
    private static int partitionFirst(double[] a, int lb, int ub)
    {
        double p = a[lb];
        int i = lb + 1;
        for (int j = lb + 1; j <= ub; j++)
        {
            // Swap if element at j is greater than the pivot, or if they're
            // equal but weight of but job in position j has higher weight
            if(a[j] > p || (a[j] == p && higherWeight(j, lb)))
            {
                swap(a, j, i);
                i++;
            }
        }
        swap(a, lb, i - 1);
        return i - 1;
    }

    /**
     * Determines whether job at position i has higher weight than job at
     * position j.
     * @param i Index of first job in the jobs class list.
     * @param j Index of second job in the jobs class list.
     * @return Whether first job has higher weight than second job.
     */
    private static boolean higherWeight(int i, int j)
    {
        Job job1 = QuickScores.jobs.get(i);
        Job job2 = QuickScores.jobs.get(j);
        return job1.getWeight() > job2.getWeight();
    }

    /**
     * Swaps the elements in array a at indices j and i. <br/>
     * @param a Array of double numbers.
     * @param j Index of element to swap.
     * @param i Index of element to swap.
     */
    private static void swap(double[] a, int j, int i)
    {
        // Swap elements in a
        double aux = a[j];
        a[j] = a[i];
        a[i] = aux;

        // Swap elements in the jobs list accordingly
        Job auxJob = new Job(QuickScores.jobs.get(j));
        QuickScores.jobs.set(j, QuickScores.jobs.get(i));
        QuickScores.jobs.set(i, auxJob);
    }

    /**
     * Converts the given array of String into an array of double values.
     * @param a    Array of double numbers.
     * @param args List of numbers separated by spaces.
     */
    private static void convertStrings(double[] a, String[] args)
    {
        // Stores again the arguments received by the main
        for (int i = 0; i < args.length; i++)
        {
            a[i] = Double.parseDouble(args[i]);
        }
    }

    //-------------------------------------------------------------------------
    // MAIN
    //-------------------------------------------------------------------------

    /**
     * Main test method for the QuickScores class. <br/>
     * @param args List of numbers separated by spaces.
     */
    public static void main(String[] args)
    {
        if (args.length == 0)
        {
            System.exit(-1);
        }

        // converts the given array into an array of double values
        double[] a = new double[args.length];
        QuickScores.convertStrings(a, args);
        System.out.println("Array received: ");
        QuickScores.show(a);

        // Sorts array a using quicksort and then shows it
        sort(a);
        System.out.println("Sorted array: ");
        QuickScores.show(a);
    }
}