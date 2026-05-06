import java.util.List;
import java.util.ArrayList;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Sprint {
    private String name;
    private String startDate;
    private String endDate;
    private final List<Story> stories;
    private final PropertyChangeSupport pcs;

    public Sprint(String name, String startDate, String endDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) {
        String old = this.startDate;
        this.startDate = startDate;
        pcs.firePropertyChange("startDate", old, startDate);
    }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) {
        String old = this.endDate;
        this.endDate = endDate;
        pcs.firePropertyChange("endDate", old, endDate);
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

    public void displayDetails() {
        System.out.println("Sprint: " + name);
        System.out.println("Start: " + startDate);
        System.out.println("End: " + endDate);
        System.out.println("Stories:");
        if (stories.isEmpty()) {
            System.out.println("  No stories yet");
        } else {
            for (Story story : stories) {
                story.displayDetails();
            }
        }
    }

    @Override
    public String toString() {
        return name + " (" + startDate + " → " + endDate + ")";
    }
}