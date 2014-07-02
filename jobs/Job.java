/**
 * Class that represents a job for the greedy algorithm for minimizing the
 * weighted sum of completion times.
 */
public class Job
{
    //-------------------------------------------------------------------------
    // ATTRIBUTES
    //-------------------------------------------------------------------------

    private int weight;         // Weight or priority of the job
    private int length;         // Length of the job (undefined units of time)

    //-------------------------------------------------------------------------
    // CONSTRUCTORS
    //-------------------------------------------------------------------------

    /**
     * Creates a new Job.
     * @param weight Weight or priority of the job.
     * @param length Length of the job, in unspecified units of time.
     */
    public Job(int weight, int length)
    {
        this.weight = weight;
        this.length = length;
    }

    /**
     * Copy constructor.
     * @param that Job to copy.
     */
    public Job(Job that)
    {
        this.weight = that.weight;
        this.length = that.length;
    }

    //-------------------------------------------------------------------------
    // PUBLIC METHODS
    //-------------------------------------------------------------------------

    /**
     * Gets the weight of this job.
     * @return The weight of this job.
     */
    public int getWeight()
    {
        return weight;
    }

    /**
     * Sets the given weight to this job.
     * @param weight The weight to assign to this job.
     */
    public void setWeight(int weight)
    {
        this.weight = weight;
    }

    /**
     * Gets the length of this job.
     * @return The length of this job.
     */
    public int getLength()
    {
        return length;
    }

    /**
     * Sets the given length to this job.
     * @param length The length to assign to this job.
     */
    public void setLength(int length)
    {
        this.length = length;
    }
}
