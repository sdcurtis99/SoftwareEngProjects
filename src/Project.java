import java.util.List;
import java.util.ArrayList;

public class Project {
    private String name;
    private final List<Sprint> sprints;
    private final List<Story> stories;

    public Project(String name) {
        this.name = name;
        this.sprints = new ArrayList<>();
        this.stories = new ArrayList<>();
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Sprint> getSprints() { return sprints; }
    public void addSprint(Sprint sprint) { sprints.add(sprint); }
    public void removeSprint(Sprint sprint) { sprints.remove(sprint); }

    public List<Story> getStories() { return stories; }
    public void addStory(Story story) { stories.add(story); }
    public void removeStory(Story story) { stories.remove(story); }

    // Backlog logic belongs here, not in ConsoleUI
    public List<Story> getBacklogStories() {
        List<Story> backlog = new ArrayList<>();
        for (Story story : stories) {
            if (story.isInBacklog()) {
                backlog.add(story);
            }
        }
        return backlog;
    }

    public void displayDetails() {
        System.out.println("Project: " + name);
        System.out.println("Sprints:");
        if (sprints.isEmpty()) {
            System.out.println("  No sprints yet");
        } else {
            for (Sprint sprint : sprints) {
                sprint.displayDetails();
            }
        }
        System.out.println("Backlog:");
        List<Story> backlog = getBacklogStories();
        if (backlog.isEmpty()) {
            System.out.println("  No stories in backlog");
        } else {
            for (Story story : backlog) {
                story.displayDetails();
            }
        }
    }
}