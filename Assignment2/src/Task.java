public class Task extends WorkItem {
    private Developer assignee;

    public Task(String title, String description, Status status, Priority priority) {
        super(title, description, status, priority);
        this.assignee = null;
    }

    public Task(String title, String description) {
        this(title, description, Status.BACKLOG, Priority.MEDIUM);
    }

    @Override
    public void displayDetails() {
        System.out.println("Task: " + getTitle());
        System.out.println("Status: " + getStatus());
        System.out.println("Priority: " + getPriority());
        System.out.println("Description: " + getDescription());
        System.out.println("Assignee: " + (assignee == null ? "Unassigned" : assignee));
    }

    public Developer getAssignee() {
        return assignee;
    }

    public void setAssignee(Developer assignee) {
        Developer old = this.assignee;
        this.assignee = assignee;
        firePropertyChange("assignee", old, assignee);
    }

    @Override
    public String toString() {
        String who = assignee == null ? "—" : assignee.getUsername();
        return getTitle() + " [" + getStatus() + "] @" + who;
    }
}