import java.util.List;
import java.util.ArrayList;

public class Story extends WorkItem {
    private Sprint sprint;
    private final List<Developer> developers;
    private final List<Task> tasks;

    public Story(String title, String description) {
        super(title, description, Status.BACKLOG, Priority.MEDIUM);
        this.developers = new ArrayList<>();
        this.sprint = null;
        this.tasks = new ArrayList<>();
    }

    // Sprint assignment — sync logic lives here, not in ConsoleUI
    public void assignToSprint(Sprint sprint) {
        if (this.sprint != null) {
            this.sprint.removeStory(this);
        }
        this.sprint = sprint;
        sprint.addStory(this);
        this.setStatus(Status.IN_PROGRESS);
    }

    public void unassignFromSprint() {
        if (this.sprint != null) {
            this.sprint.removeStory(this);
            this.sprint = null;
            this.setStatus(Status.BACKLOG);
        }
    }

    public boolean isInBacklog() { return sprint == null; }

    public Sprint getSprint() { return sprint; }

    public List<Developer> getDevelopers() { return developers; }
    public void addDeveloper(Developer developer) { developers.add(developer); }
    public void removeDeveloper(Developer developer) { developers.remove(developer); }

    public List<Task> getTasks() { return tasks; }
    public void addTask(Task task) { tasks.add(task); }
    public void removeTask(Task task) { tasks.remove(task); }

    @Override
    public void displayDetails() {
        System.out.println("Story: " + getTitle());
        System.out.println("Status: " + getStatus());
        System.out.println("Priority: " + getPriority());
        System.out.println("Description: " + getDescription());
        if (developers.isEmpty()) {
            System.out.println("Developers: Unassigned");
        } else {
            System.out.println("Developers:");
            for (Developer developer : developers) {
                System.out.println("  " + developer.getName());
            }
        }
        System.out.println("Sprint: " + (sprint != null ? sprint.getName() : "Backlog"));
        System.out.println("Tasks:");
        if (tasks.isEmpty()) {
            System.out.println("  No tasks yet");
        } else {
            for (Task task : tasks) {
                task.displayDetails();
            }
        }
    }
}