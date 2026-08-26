package peter.task;

/**
 * Represents a Deadline type of Task.
 * A deadline has a time besides the basic information of Task.
 */
public class Deadline extends Task {
    protected String by;

    /**
     * Constructs a Deadline object with the specified description.
     * The task is initially not completed.
     *
     * @param description The textual description of the task.
     * @param by The time of the deadline.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }

    @Override
    public String toFileFormat() {
        return "D | " + super.toFileFormat() + " | " + this.by;
    }
}