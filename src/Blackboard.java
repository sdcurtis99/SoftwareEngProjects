import java.util.ArrayList;
import java.util.List;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Blackboard {
    private static Blackboard instance;
    private final List<Project> projects;
    private final List<Developer> developers;
    private final PropertyChangeSupport pcs;

    private Blackboard() {
        this.projects = new ArrayList<>();
        this.developers = new ArrayList<>();
        this.pcs = new PropertyChangeSupport(this);
    }

    public static Blackboard getInstance() {
        if (instance == null) {
            instance = new Blackboard();
        }
        return instance;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public List<Project> getProjects() { return projects; }
    public void addProject(Project project) {
        projects.add(project);
        pcs.firePropertyChange("projects", null, project);
    }
    public void removeProject(Project project) {
        projects.remove(project);
        pcs.firePropertyChange("projects", project, null);
    }

    public List<Developer> getDevelopers() { return developers; }
    public void addDeveloper(Developer developer) {
        developers.add(developer);
        pcs.firePropertyChange("developers", null, developer);
    }
    public void removeDeveloper(Developer developer) {
        if (!developers.remove(developer)) return;
        // Keep the model consistent: remove from stories and unassign from tasks.
        for (Project p : projects) {
            for (Story s : p.getStories()) {
                s.removeDeveloper(developer);
                for (Task t : s.getTasks()) {
                    if (t.getAssignee() == developer) t.setAssignee(null);
                }
            }
        }
        pcs.firePropertyChange("developers", developer, null);
    }
}