import java.util.*;

public class ConsoleUI {
    private Scanner scanner = new Scanner(System.in);

    public void start() {
        while (true) {
            printMenu("Main Menu",
                    "1. Create Project",
                    "2. Manage Projects",
                    "0. Exit");

            int choice = getUserChoice(2);

            switch (choice) {
                case 1 -> createProject();
                case 2 -> manageProjects();
                case 0 -> System.exit(0);
            }
        }
    }

    private void createProject() {
        String name = readText("Enter project name: ");
        Blackboard.getInstance().addProject(new Project(name));
        System.out.println("Project created.\n");
    }

    private void manageProjects() {
        List<Project> projects = Blackboard.getInstance().getProjects();

        if (projects.isEmpty()) {
            System.out.println("No projects available.\n");
            return;
        }

        Project project = selectFromList(projects, "Select a project:");

        while (true) {
            printMenu("Project Menu",
                    "1. Create Sprint",
                    "2. Create Story",
                    "3. Assign Developer",
                    "4. Remove Story from Sprint",
                    "5. Update Story Status",
                    "0. Back");

            int choice = getUserChoice(5);

            switch (choice) {
                case 1 -> createSprint(project);
                case 2 -> createStory(project);
                case 3 -> assignDeveloper(project);
                case 4 -> removeStoryFromSprint(project);
                case 5 -> updateStoryStatus(project);
                case 0 -> { return; }
            }
        }
    }


    private void createSprint(Project project) {
        String name = readText("Sprint name: ");
        String start = readText("Start date: ");
        String end = readText("End date: ");

        project.addSprint(new Sprint(name, start, end));
        System.out.println("Sprint created.\n");
    }


    private void createStory(Project project) {
        String title = readText("Story title: ");
        String desc = readText("Description: ");

        project.addStory(new Story(title, desc));
        System.out.println("Story created.\n");
    }

    private void assignDeveloper(Project project) {
        if (project.getStories().isEmpty()) {
            System.out.println("No stories.\n");
            return;
        }

        Story story = selectFromList(project.getStories(), "Select story:");

        List<Developer> devs = Blackboard.getInstance().getDevelopers();
        if (devs.isEmpty()) {
            System.out.println("No developers.\n");
            return;
        }

        Developer dev = selectFromList(devs, "Select developer:");
        story.addDeveloper(dev);

        System.out.println("Developer assigned.\n");
    }

    private void removeStoryFromSprint(Project project) {
        if (project.getStories().isEmpty()) {
            System.out.println("No stories.\n");
            return;
        }

        Story story = selectFromList(project.getStories(), "Select story:");

        if (story.isInBacklog()) {
            System.out.println("Story is already in backlog.\n");
            return;
        }

        story.unassignFromSprint();

        System.out.println("Story moved to backlog.\n");
    }

    private void updateStoryStatus(Project project) {
        if (project.getStories().isEmpty()) return;

        Story story = selectFromList(project.getStories(), "Select story:");

        printMenu("Update Status",
                "1. BACKLOG",
                "2. IN_PROGRESS",
                "3. DONE");

        int choice = getUserChoice(3);

        WorkItem.Status status = switch (choice) {
            case 1 -> WorkItem.Status.BACKLOG;
            case 2 -> WorkItem.Status.IN_PROGRESS;
            case 3 -> WorkItem.Status.DONE;
            default -> WorkItem.Status.BACKLOG;
        };

        story.setStatus(status);
        System.out.println("Status updated.\n");
    }

    private void printMenu(String title, String... options) {
        System.out.println("\n=== " + title + " ===");
        for (String opt : options) System.out.println(opt);
    }

    private String readText(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private int getUserChoice(int max) {
        while (true) {
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 0 && choice <= max) return choice;
            } catch (Exception ignored) {}
            System.out.print("Invalid choice. Try again: ");
        }
    }

    private <T> T selectFromList(List<T> list, String prompt) {
        System.out.println(prompt);
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + ". " + list.get(i));
        }
        int index = getUserChoice(list.size() - 1);
        return list.get(index);
    }
}