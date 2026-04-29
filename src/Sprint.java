import java.util.List;
import java.util.ArrayList;

public class Sprint {
    private String name;
    private String startDate;
    private String endDate;
    private final List<Story> stories;

    public Sprint(String name, String startDate, String endDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.stories = new ArrayList<>();
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public List<Story> getStories() { return stories; }
    public void addStory(Story story) { stories.add(story); }
    public void removeStory(Story story) { stories.remove(story); }

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
}