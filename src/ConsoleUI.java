import java.util.Scanner;

public class ConsoleUI {
    private final Scanner scanner = new Scanner(System.in);

    public void start() { showMainMenu(); }

    // --- Menus ---

    private void showMainMenu() {
        while (true) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. Manage Projects");
            System.out.println("2. Manage Developers");
            System.out.println("3. Exit");
            switch (getUserChoice(3)) {
                case 1 -> showProjectsMenu();
                case 2 -> showDevelopersMenu();
                case 3 -> { System.out.println("Goodbye!"); return; }
            }
        }
    }

    private void showProjectsMenu() {
        while (true) {
            System.out.println("\n=== Projects ===");
            System.out.println("1. Create Project");
            System.out.println("2. Select Project");
            System.out.println("3. Back");
            switch (getUserChoice(3)) {
                case 1 -> createProject();
                case 2 -> { Project p = selectProject(); if (p != null) showProjectMenu(p); }
                case 3 -> { return; }
            }
        }
    }

    private void showProjectMenu(Project project) {
        while (true) {
            System.out.println("\n=== Project: " + project.getName() + " ===");
            System.out.println("1. View Details");
            System.out.println("2. Manage Sprints");
            System.out.println("3. Manage Stories");
            System.out.println("4. Back");
            switch (getUserChoice(4)) {
                case 1 -> project.displayDetails();
                case 2 -> showSprintsMenu(project);
                case 3 -> showStoriesMenu(project);
                case 4 -> { return; }
            }
        }
    }

    private void showSprintsMenu(Project project) {
        while (true) {
            System.out.println("\n=== Sprints ===");
            System.out.println("1. Create Sprint");
            System.out.println("2. Select Sprint");
            System.out.println("3. Back");
            switch (getUserChoice(3)) {
                case 1 -> createSprint(project);
                case 2 -> { Sprint s = selectSprint(project); if (s != null) showSprintMenu(project, s); }
                case 3 -> { return; }
            }
        }
    }

    private void showSprintMenu(Project project, Sprint sprint) {
        while (true) {
            System.out.println("\n=== Sprint: " + sprint.getName() + " ===");
            System.out.println("1. View Details");
            System.out.println("2. Add Story to Sprint");
            System.out.println("3. Remove Story from Sprint");
            System.out.println("4. Back");
            switch (getUserChoice(4)) {
                case 1 -> sprint.displayDetails();
                case 2 -> addStoryToSprint(project, sprint);
                case 3 -> removeStoryFromSprint(sprint);
                case 4 -> { return; }
            }
        }
    }

    private void showStoriesMenu(Project project) {
        while (true) {
            System.out.println("\n=== Stories ===");
            System.out.println("1. Create Story");
            System.out.println("2. Select Story");
            System.out.println("3. Back");
            switch (getUserChoice(3)) {
                case 1 -> createStory(project);
                case 2 -> { Story s = selectStory(project); if (s != null) showStoryMenu(s); }
                case 3 -> { return; }
            }
        }
    }

    private void showStoryMenu(Story story) {
        while (true) {
            System.out.println("\n=== Story: " + story.getTitle() + " ===");
            System.out.println("1. View Details");
            System.out.println("2. Add Task");
            System.out.println("3. Remove Task");
            System.out.println("4. Assign Developer");
            System.out.println("5. Remove Developer");
            System.out.println("6. Update Status");
            System.out.println("7. Back");
            switch (getUserChoice(7)) {
                case 1 -> story.displayDetails();
                case 2 -> addTask(story);
                case 3 -> removeTask(story);
                case 4 -> assignDeveloper(story);
                case 5 -> removeDeveloper(story);
                case 6 -> updateStatus(story);
                case 7 -> { return; }
            }
        }
    }

    private void showDevelopersMenu() {
        while (true) {
            System.out.println("\n=== Developers ===");
            System.out.println("1. Create Developer");
            System.out.println("2. View All Developers");
            System.out.println("3. Back");
            switch (getUserChoice(3)) {
                case 1 -> createDeveloper();
                case 2 -> viewAllDevelopers();
                case 3 -> { return; }
            }
        }
    }

    // --- Create methods ---

    private void createProject() {
        System.out.print("Enter project name: ");
        Blackboard.getInstance().addProject(new Project(scanner.nextLine()));
        System.out.println("Project created.");
    }

    private void createSprint(Project project) {
        System.out.print("Enter sprint name: ");
        String name = scanner.nextLine();
        System.out.print("Enter start date: ");
        String startDate = scanner.nextLine();
        System.out.print("Enter end date: ");
        String endDate = scanner.nextLine();
        project.addSprint(new Sprint(name, startDate, endDate));
        System.out.println("Sprint created.");
    }

    private void createStory(Project project) {
        System.out.print("Enter story title: ");
        String title = scanner.nextLine();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        project.addStory(new Story(title, description));
        System.out.println("Story created.");
    }

    private void createDeveloper() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        Blackboard.getInstance().addDeveloper(new Developer(name, username));
        System.out.println("Developer created.");
    }

    private void addTask(Story story) {
        System.out.print("Enter task title: ");
        String title = scanner.nextLine();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        story.addTask(new Task(title, description));
        System.out.println("Task added.");
    }

    private void removeTask(Story story) {
        Task task = selectTask(story);
        if (task != null) {
            story.removeTask(task);
            System.out.println("Task removed.");
        }
    }

    private void assignDeveloper(Story story) {
        Developer developer = selectDeveloper();
        if (developer != null) {
            story.addDeveloper(developer);
            System.out.println("Developer assigned.");
        }
    }

    private void removeDeveloper(Story story) {
        if (story.getDevelopers().isEmpty()) {
            System.out.println("No developers assigned.");
            return;
        }
        Developer developer = selectDeveloperFromStory(story);
        if (developer != null) {
            story.removeDeveloper(developer);
            System.out.println("Developer removed.");
        }
    }

    private void addStoryToSprint(Project project, Sprint sprint) {
        Story story = selectStory(project);
        if (story != null) {
            story.assignToSprint(sprint);
            System.out.println("Story added to sprint.");
        }
    }

    private void removeStoryFromSprint(Sprint sprint) {
        if (sprint.getStories().isEmpty()) {
            System.out.println("No stories in sprint.");
            return;
        }
        System.out.println("Select story to remove:");
        for (int i = 0; i < sprint.getStories().size(); i++) {
            System.out.println((i + 1) + ". " + sprint.getStories().get(i).getTitle());
        }
        Story story = sprint.getStories().get(getUserChoice(sprint.getStories().size()) - 1);
        story.unassignFromSprint();
        System.out.println("Story removed from sprint.");
    }

    private void updateStatus(Story story) {
        System.out.println("Select new status:");
        System.out.println("1. BACKLOG");
        System.out.println("2. IN_PROGRESS");
        System.out.println("3. DONE");
        switch (getUserChoice(3)) {
            case 1 -> story.setStatus(WorkItem.Status.BACKLOG);
            case 2 -> story.setStatus(WorkItem.Status.IN_PROGRESS);
            case 3 -> story.setStatus(WorkItem.Status.DONE);
        }
        System.out.println("Status updated.");
    }

    private void viewAllDevelopers() {
        if (Blackboard.getInstance().getDevelopers().isEmpty()) {
            System.out.println("No developers yet.");
            return;
        }
        for (Developer developer : Blackboard.getInstance().getDevelopers()) {
            developer.displayDetails();
        }
    }

    // --- Select helpers ---

    private Project selectProject() {
        if (Blackboard.getInstance().getProjects().isEmpty()) {
            System.out.println("No projects yet.");
            return null;
        }
        System.out.println("Select a project:");
        for (int i = 0; i < Blackboard.getInstance().getProjects().size(); i++) {
            System.out.println((i + 1) + ". " + Blackboard.getInstance().getProjects().get(i).getName());
        }
        return Blackboard.getInstance().getProjects().get(getUserChoice(Blackboard.getInstance().getProjects().size()) - 1);
    }

    private Sprint selectSprint(Project project) {
        if (project.getSprints().isEmpty()) {
            System.out.println("No sprints yet.");
            return null;
        }
        System.out.println("Select a sprint:");
        for (int i = 0; i < project.getSprints().size(); i++) {
            System.out.println((i + 1) + ". " + project.getSprints().get(i).getName());
        }
        return project.getSprints().get(getUserChoice(project.getSprints().size()) - 1);
    }

    private Story selectStory(Project project) {
        if (project.getStories().isEmpty()) {
            System.out.println("No stories yet.");
            return null;
        }
        System.out.println("Select a story:");
        for (int i = 0; i < project.getStories().size(); i++) {
            System.out.println((i + 1) + ". " + project.getStories().get(i).getTitle());
        }
        return project.getStories().get(getUserChoice(project.getStories().size()) - 1);
    }

    private Task selectTask(Story story) {
        if (story.getTasks().isEmpty()) {
            System.out.println("No tasks yet.");
            return null;
        }
        System.out.println("Select a task:");
        for (int i = 0; i < story.getTasks().size(); i++) {
            System.out.println((i + 1) + ". " + story.getTasks().get(i).getTitle());
        }
        return story.getTasks().get(getUserChoice(story.getTasks().size()) - 1);
    }

    private Developer selectDeveloper() {
        if (Blackboard.getInstance().getDevelopers().isEmpty()) {
            System.out.println("No developers yet.");
            return null;
        }
        System.out.println("Select a developer:");
        for (int i = 0; i < Blackboard.getInstance().getDevelopers().size(); i++) {
            System.out.println((i + 1) + ". " + Blackboard.getInstance().getDevelopers().get(i).getName());
        }
        return Blackboard.getInstance().getDevelopers().get(getUserChoice(Blackboard.getInstance().getDevelopers().size()) - 1);
    }

    private Developer selectDeveloperFromStory(Story story) {
        System.out.println("Select a developer:");
        for (int i = 0; i < story.getDevelopers().size(); i++) {
            System.out.println((i + 1) + ". " + story.getDevelopers().get(i).getName());
        }
        return story.getDevelopers().get(getUserChoice(story.getDevelopers().size()) - 1);
    }

    // --- Input validation ---

    private int getUserChoice(int max) {
        while (true) {
            System.out.print("Enter choice (1-" + max + "): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= 1 && choice <= max) return choice;
                System.out.println("Please enter a number between 1 and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a number.");
            }
        }
    }
}