/**
 * This class represents the main action panel of the GUI.
 * It allows users to create stories, assign developers, and update status.
 *
 * @Shannon Curtis
 * @version 2.0
 */

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
        Sprint oldSprint = this.sprint;
        this.sprint = sprint;
        sprint.addStory(this);
        this.setStatus(Status.IN_PROGRESS);
        firePropertyChange("sprint", oldSprint, sprint);
    }

    public void unassignFromSprint() {
        if (this.sprint != null) {
            Sprint oldSprint = this.sprint;
            this.sprint.removeStory(this);
            this.sprint = null;
            this.setStatus(Status.BACKLOG);
            firePropertyChange("sprint", oldSprint, null);
        }
    }

    public boolean isInBacklog() { return sprint == null; }

    public Sprint getSprint() { return sprint; }

    public List<Developer> getDevelopers() { return developers; }
    public void addDeveloper(Developer developer) {
        if (developer == null) return;
        if (developers.contains(developer)) return;
        developers.add(developer);
        firePropertyChange("developers", null, developer);
    }
    public void removeDeveloper(Developer developer) {
        if (developer == null) return;
        if (!developers.remove(developer)) return;
        firePropertyChange("developers", developer, null);
    }

    public List<Task> getTasks() { return tasks; }
    public void addTask(Task task) {
        if (task == null) return;
        if (tasks.contains(task)) return;
        tasks.add(task);
        firePropertyChange("tasks", null, task);
    }
    public void removeTask(Task task) {
        if (task == null) return;
        if (!tasks.remove(task)) return;
        firePropertyChange("tasks", task, null);
    }

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

    @Override
    public String toString() {
        String sprintName = (getSprint() == null) ? "Backlog" : getSprint().getName();
        return getTitle() + " [" + getStatus() + "] (" + sprintName + ")";
    }
}