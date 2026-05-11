/**
 * This class represents the main action panel of the GUI.
 * It allows users to create stories, assign developers, and update status.
 *
 * @Shannon Curtis
 * @version 2.0
 */

import java.util.List;
import java.util.ArrayList;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Project {
    private String name;
    private final List<Sprint> sprints;
    private final List<Story> stories;
    private final PropertyChangeSupport pcs;

    public Project(String name) {
        this.name = name;
        this.sprints = new ArrayList<>();
        this.stories = new ArrayList<>();
        this.pcs = new PropertyChangeSupport(this);
    }

    public String getName() { return name; }
    public void setName(String name) {
        String old = this.name;
        this.name = name;
        pcs.firePropertyChange("name", old, name);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public List<Sprint> getSprints() { return sprints; }
    public void addSprint(Sprint sprint) {
        sprints.add(sprint);
        pcs.firePropertyChange("sprints", null, sprint);
    }
    public void removeSprint(Sprint sprint) {
        sprints.remove(sprint);
        pcs.firePropertyChange("sprints", sprint, null);
    }

    public List<Story> getStories() { return stories; }
    public void addStory(Story story) {
        stories.add(story);
        pcs.firePropertyChange("stories", null, story);
    }
    public void removeStory(Story story) {
        stories.remove(story);
        pcs.firePropertyChange("stories", story, null);
    }

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

    @Override
    public String toString() {
        return name;
    }
}