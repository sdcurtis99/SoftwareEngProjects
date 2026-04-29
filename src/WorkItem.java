public abstract class WorkItem {
    private String title;
    private String description;
    private Status status;
    private Priority priority;

    enum Status {
        BACKLOG,
        IN_PROGRESS,
        DONE
    }

    enum Priority {
        LOW,
        MEDIUM,
        HIGH
    }

    public WorkItem(String title, String description, Status status, Priority priority) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
    }

    // Force subclasses to define how they will display their data.
    public abstract void displayDetails();

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }
}