import java.util.ArrayList;
import java.util.List;

public class Blackboard {
    private static Blackboard instance;
    private final List<Project> projects;
    private final List<Developer> developers;

    private Blackboard() {
        this.projects = new ArrayList<>();
        this.developers = new ArrayList<>();
    }

    public static Blackboard getInstance() {
        if (instance == null) {
            instance = new Blackboard();
        }
        return instance;
    }

    public List<Project> getProjects() { return projects; }
    public void addProject(Project project) { projects.add(project); }
    public void removeProject(Project project) { projects.remove(project); }

    public List<Developer> getDevelopers() { return developers; }
    public void addDeveloper(Developer developer) { developers.add(developer); }
    public void removeDeveloper(Developer developer) { developers.remove(developer); }
}