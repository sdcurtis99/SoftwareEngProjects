public class Task extends WorkItem {

    public Task(String title, String description, Status status, Priority priority) {
        super(title, description, status, priority);
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
    }
}