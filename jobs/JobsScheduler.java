/**
 * $Id: JobsScheduler.java, v 1.1 02/07/14 20:38 oscarfabra Exp $
 * {@code JobsScheduler} Is a class that implements a greedy algorithm for
 * minimizing the weighted sum of completion times.
 *
 * @author <a href="mailto:oscarfabra@gmail.com">Oscar Fabra</a>
 * @version 1.0
 * @since 02/07/14
 */

import java.util.ArrayList;
import java.util.List;

/**
 * Class that implements a greedy algorithm that schedules jobs and minimizes
 * the weighted sum of completion times.
 */
public class JobsScheduler
{
    //-------------------------------------------------------------------------
    // CONSTRUCTOR
    //-------------------------------------------------------------------------

    private JobsScheduler() {}  // This class shouldn't be instantiated

    //-------------------------------------------------------------------------
    // PUBLIC CLASS METHODS
    //-------------------------------------------------------------------------

    /**
     * Gets a list of jobs from the given lines os String.
     * @param lines Lines of Strings, each line has the form
     *              [job_weight] [job_length]
     * @return List of Job objects from the given list of lines.
     */
    public static List<Job> readJobsList(List<String> lines)
    {
        List<Job> jobs = new ArrayList<Job>(lines.size());

        for(String line : lines)
        {
            String [] values = line.split(" ");
            int weight = Integer.parseInt(values[0]);
            int length = Integer.parseInt(values[1]);
            Job job = new Job(weight, length);
            jobs.add(job);
        }

        return jobs;
    }

    /**
     * Gets the sum of completion times of the given list of jobs running the
     * greedy algorithm whose type is specified in the first parameter;
     * type = 1 for decreasing order of the difference (weight - length),
     * type = 2 for decreasing order of the ratio (weight/length).
     * @param type Greedy algorithm to run on the given list of jobs.
     * @param jobs List of Job objects to schedule and for which to find the
     *             sum of its completion times.
     * @return Sum of the weighted completion times according to the specified
     * greedy algorithm to run.
     */
    public static int getSumOfCompletionTimes(int type, List<Job> jobs)
    {
        // Orders the given jobs according to the specified greedy algorithm
        switch(type)
        {
            case 1: JobsScheduler.scheduleByDifference(jobs);
                break;
            case 2: JobsScheduler.scheduleByRatio(jobs);
                break;
        }

        // Gets and returns the sum of completion times of the ordered jobs
        return JobsScheduler.getSumOfCompletionTimes(jobs);
    }

    //-------------------------------------------------------------------------
    // PRIVATE HELPER METHODS
    //-------------------------------------------------------------------------

    /**
     * Schedules the given list of jobs by the difference (weight - length) of
     * each job.
     * @param jobs List of Job objects to schedule.
     */
    private static void scheduleByDifference(List<Job> jobs)
    {
        // TODO: Schedule jobs by the difference (weight - length) of each job
    }

    /**
     * Schedules the given list of jobs by the ratio (weight/length of each
     * job.
     * @param jobs List of Job objects to schedule.
     */
    private static void scheduleByRatio(List<Job> jobs)
    {
        // TODO: Schedule jobs by the ratio (weight/length) of each job
    }

    /**
     * Computes and returns the sum of the completion times of the given
     * schedule (list of jobs already ordered by some method).
     * @param schedule List of jobs already scheduled by some method.
     * @return Sum of the completion times of the given list of jobs.
     */
    private static int getSumOfCompletionTimes(List<Job> schedule)
    {
        int sum = 0;
        // TODO: Compute the sum of the completion times of the given schedule
        return sum;
    }



}
