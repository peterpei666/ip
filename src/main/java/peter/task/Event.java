package peter.task;

/**
 * Represents an Event type of Task.
 * A deadline has two time fields for the begining and end besides the basic information of Task.
 */
public class Event extends Task {
    protected String from;
    protected String to;

    /**
     * Constructs a Deadline object with the specified description.
     * The task is initially not completed.
     *
     * @param description The textual description of the task.
     * @param from The time of the begining of an event.
     * @param by The time of the end of an event.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }

    @Override
    public String toFileFormat() {
        return "E | " + super.toFileFormat() + " | " + this.from + " | " + this.to;
    }
}