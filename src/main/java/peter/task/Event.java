package peter.task;

/**
 * Represents an Event type of Task.
 * An event has start and end times in addition to the basic task information.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Constructs an Event object with the specified description and times.
     * The task is initially not completed.
     *
     * @param description The textual description of the task.
     * @param from The start time of the event.
     * @param to The end time of the event.
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
