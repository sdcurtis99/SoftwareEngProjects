import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SwingUI {
    private final Blackboard blackboard = Blackboard.getInstance();

    private JFrame frame;

    private final DefaultListModel<Project> projectsModel = new DefaultListModel<>();
    private final JList<Project> projectsList = new JList<>(projectsModel);

    private final DefaultListModel<Sprint> sprintsModel = new DefaultListModel<>();
    private final JList<Sprint> sprintsList = new JList<>(sprintsModel);

    private final DefaultListModel<Story> storiesModel = new DefaultListModel<>();
    private final JList<Story> storiesList = new JList<>(storiesModel);

    private final DefaultListModel<Task> tasksModel = new DefaultListModel<>();
    private final JList<Task> tasksList = new JList<>(tasksModel);

    private final DefaultListModel<Developer> developersModel = new DefaultListModel<>();
    private final JList<Developer> developersList = new JList<>(developersModel);

    private Project selectedProject;
    private Story selectedStory;

    private final PropertyChangeListener blackboardListener = this::onBlackboardChanged;
    private final PropertyChangeListener projectListener = this::onProjectChanged;
    private final PropertyChangeListener storyListener = this::onStoryChanged;

    public void start() {
        SwingUtilities.invokeLater(this::createAndShow);
    }

    private void createAndShow() {
        frame = new JFrame("Taigai - Desktop");
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                blackboard.removePropertyChangeListener(blackboardListener);
                if (selectedProject != null) selectedProject.removePropertyChangeListener(projectListener);
                if (selectedStory != null) selectedStory.removePropertyChangeListener(storyListener);
                frame.dispose();
                System.exit(0);
            }
        });

        frame.setContentPane(buildRoot());
        frame.setMinimumSize(new Dimension(1050, 700));

        wireListeners();
        refreshAll();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JComponent buildRoot() {
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel header = new JLabel("Taigai Project Management");
        header.setFont(header.getFont().deriveFont(Font.BOLD, header.getFont().getSize() + 6f));
        root.add(header, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buildLeftPane(),
                buildRightPane());
        split.setResizeWeight(0.28);
        root.add(split, BorderLayout.CENTER);

        return root;
    }

    private JComponent buildLeftPane() {
        JPanel left = new JPanel(new BorderLayout(8, 8));
        left.add(sectionTitle("Projects"), BorderLayout.NORTH);

        projectsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        left.add(new JScrollPane(projectsList), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridLayout(0, 1, 6, 6));
        JButton addProject = new JButton("New Project");
        addProject.addActionListener(e -> actionNewProject());
        JButton editProject = new JButton("Edit Project");
        editProject.addActionListener(e -> actionEditProject());
        JButton deleteProject = new JButton("Delete Project");
        deleteProject.addActionListener(e -> actionDeleteProject());

        buttons.add(addProject);
        buttons.add(editProject);
        buttons.add(deleteProject);
        left.add(buttons, BorderLayout.SOUTH);

        return left;
    }

    private JComponent buildRightPane() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Project", buildProjectTab());
        tabs.addTab("Developers", buildDevelopersTab());
        return tabs;
    }

    private JComponent buildProjectTab() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));

        JSplitPane vertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                buildSprintsAndStoriesPane(),
                buildStoryDetailsPane());
        vertical.setResizeWeight(0.55);

        panel.add(vertical, BorderLayout.CENTER);
        return panel;
    }

    private JComponent buildSprintsAndStoriesPane() {
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buildSprintsPane(),
                buildStoriesPane());
        split.setResizeWeight(0.5);
        return split;
    }

    private JComponent buildSprintsPane() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.add(sectionTitle("Sprints"), BorderLayout.NORTH);

        sprintsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        p.add(new JScrollPane(sprintsList), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridLayout(0, 1, 6, 6));
        JButton add = new JButton("New Sprint");
        add.addActionListener(e -> actionNewSprint());
        JButton edit = new JButton("Edit Sprint");
        edit.addActionListener(e -> actionEditSprint());
        JButton remove = new JButton("Delete Sprint");
        remove.addActionListener(e -> actionDeleteSprint());
        buttons.add(add);
        buttons.add(edit);
        buttons.add(remove);
        p.add(buttons, BorderLayout.SOUTH);

        return p;
    }

    private JComponent buildStoriesPane() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.add(sectionTitle("Stories"), BorderLayout.NORTH);

        storiesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        p.add(new JScrollPane(storiesList), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridLayout(0, 1, 6, 6));
        JButton addStory = new JButton("New Story");
        addStory.addActionListener(e -> actionNewStory());

        JButton editStory = new JButton("Edit Story");
        editStory.addActionListener(e -> actionEditStory());

        JButton deleteStory = new JButton("Delete Story");
        deleteStory.addActionListener(e -> actionDeleteStory());

        JButton changeSprint = new JButton("Change Sprint / Backlog");
        changeSprint.addActionListener(e -> actionChangeStorySprint());

        JButton priority = new JButton("Update Priority");
        priority.addActionListener(e -> actionUpdateStoryPriority());

        JButton status = new JButton("Update Status");
        status.addActionListener(e -> actionUpdateStoryStatus());

        buttons.add(addStory);
        buttons.add(editStory);
        buttons.add(deleteStory);
        buttons.add(changeSprint);
        buttons.add(status);
        buttons.add(priority);
        p.add(buttons, BorderLayout.SOUTH);

        return p;
    }

    private JComponent buildStoryDetailsPane() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.add(sectionTitle("Story Tasks"), BorderLayout.NORTH);

        tasksList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        p.add(new JScrollPane(tasksList), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton addTask = new JButton("New Task");
        addTask.addActionListener(e -> actionNewTask());
        JButton editTask = new JButton("Edit Task");
        editTask.addActionListener(e -> actionEditTask());
        JButton removeTask = new JButton("Remove Task");
        removeTask.addActionListener(e -> actionRemoveTask());
        JButton taskStatus = new JButton("Task Status");
        taskStatus.addActionListener(e -> actionUpdateTaskStatus());
        JButton taskPriority = new JButton("Task Priority");
        taskPriority.addActionListener(e -> actionUpdateTaskPriority());

        JButton manageDevs = new JButton("Manage Story Developers");
        manageDevs.addActionListener(e -> actionManageStoryDevelopers());
        JButton taskAssignee = new JButton("Task Assignee");
        taskAssignee.addActionListener(e -> actionManageTaskAssignee());
        buttons.add(addTask);
        buttons.add(editTask);
        buttons.add(removeTask);
        buttons.add(taskStatus);
        buttons.add(taskPriority);
        buttons.add(manageDevs);
        buttons.add(taskAssignee);

        p.add(buttons, BorderLayout.SOUTH);
        return p;
    }

    private JComponent buildDevelopersTab() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.add(sectionTitle("Developers"), BorderLayout.NORTH);

        developersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        p.add(new JScrollPane(developersList), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton add = new JButton("New Developer");
        add.addActionListener(e -> actionNewDeveloper());
        JButton remove = new JButton("Remove Developer");
        remove.addActionListener(e -> actionRemoveDeveloper());
        buttons.add(add);
        buttons.add(remove);
        p.add(buttons, BorderLayout.SOUTH);

        return p;
    }

    private JLabel sectionTitle(String text) {
        JLabel l = new JLabel(text);
        l.setFont(l.getFont().deriveFont(Font.BOLD));
        return l;
    }

    private void wireListeners() {
        blackboard.addPropertyChangeListener(blackboardListener);

        projectsList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            setSelectedProject(projectsList.getSelectedValue());
        });

        storiesList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            setSelectedStory(storiesList.getSelectedValue());
        });
    }

    private void setSelectedProject(Project project) {
        if (selectedProject == project) return;
        if (selectedProject != null) selectedProject.removePropertyChangeListener(projectListener);
        selectedProject = project;
        if (selectedProject != null) selectedProject.addPropertyChangeListener(projectListener);
        refreshProjectLists();
        setSelectedStory(null);
    }

    private void setSelectedStory(Story story) {
        if (selectedStory == story) return;
        if (selectedStory != null) selectedStory.removePropertyChangeListener(storyListener);
        selectedStory = story;
        if (selectedStory != null) selectedStory.addPropertyChangeListener(storyListener);
        refreshTasks();
    }

    private void onBlackboardChanged(PropertyChangeEvent evt) {
        if ("projects".equals(evt.getPropertyName())) {
            SwingUtilities.invokeLater(this::refreshProjects);
        }
        if ("developers".equals(evt.getPropertyName())) {
            SwingUtilities.invokeLater(this::refreshDevelopers);
        }
    }

    private void onProjectChanged(PropertyChangeEvent evt) {
        if ("sprints".equals(evt.getPropertyName()) || "stories".equals(evt.getPropertyName()) || "name".equals(evt.getPropertyName())) {
            SwingUtilities.invokeLater(this::refreshProjectLists);
        }
    }

    private void onStoryChanged(PropertyChangeEvent evt) {
        SwingUtilities.invokeLater(() -> {
            refreshStories();
            refreshTasks();
        });
    }

    private void refreshAll() {
        refreshProjects();
        refreshDevelopers();
        refreshProjectLists();
        refreshTasks();
    }

    private void refreshProjects() {
        Project keep = projectsList.getSelectedValue();
        projectsModel.clear();
        for (Project p : blackboard.getProjects()) projectsModel.addElement(p);
        if (keep != null) projectsList.setSelectedValue(keep, true);
    }

    private void refreshDevelopers() {
        Developer keep = developersList.getSelectedValue();
        developersModel.clear();
        for (Developer d : blackboard.getDevelopers()) developersModel.addElement(d);
        if (keep != null) developersList.setSelectedValue(keep, true);
    }

    private void refreshProjectLists() {
        refreshSprints();
        refreshStories();
        refreshTasks();
    }

    private void refreshSprints() {
        Sprint keep = sprintsList.getSelectedValue();
        sprintsModel.clear();
        if (selectedProject != null) {
            for (Sprint s : selectedProject.getSprints()) sprintsModel.addElement(s);
        }
        if (keep != null) sprintsList.setSelectedValue(keep, true);
    }

    private void refreshStories() {
        Story keep = storiesList.getSelectedValue();
        storiesModel.clear();
        if (selectedProject != null) {
            for (Story s : selectedProject.getStories()) storiesModel.addElement(s);
        }
        if (keep != null) storiesList.setSelectedValue(keep, true);
    }

    private void refreshTasks() {
        Task keep = tasksList.getSelectedValue();
        tasksModel.clear();
        if (selectedStory != null) {
            for (Task t : selectedStory.getTasks()) tasksModel.addElement(t);
        }
        if (keep != null) tasksList.setSelectedValue(keep, true);
    }

    private void actionNewProject() {
        String name = promptNonEmpty("Project name:");
        if (name == null) return;
        blackboard.addProject(new Project(name));
    }

    private void actionEditProject() {
        Project p = projectsList.getSelectedValue();
        if (p == null) return;
        String name = promptNonEmpty("Project name:");
        if (name == null) return;
        p.setName(name);
        refreshProjects();
    }

    private void actionDeleteProject() {
        Project p = projectsList.getSelectedValue();
        if (p == null) return;
        if (!confirm("Delete project \"" + p.getName() + "\"?")) return;
        blackboard.removeProject(p);
        if (selectedProject == p) setSelectedProject(null);
    }

    private void actionNewSprint() {
        if (selectedProject == null) return;
        String name = promptNonEmpty("Sprint name:");
        if (name == null) return;
        String start = promptNonEmpty("Start date:");
        if (start == null) return;
        String end = promptNonEmpty("End date:");
        if (end == null) return;
        selectedProject.addSprint(new Sprint(name, start, end));
    }

    private void actionEditSprint() {
        if (selectedProject == null) return;
        Sprint sprint = sprintsList.getSelectedValue();
        if (sprint == null) return;
        String name = promptNonEmpty("Sprint name:");
        if (name == null) return;
        String start = promptNonEmpty("Start date:");
        if (start == null) return;
        String end = promptNonEmpty("End date:");
        if (end == null) return;
        sprint.setName(name);
        sprint.setStartDate(start);
        sprint.setEndDate(end);
        refreshSprints();
    }

    private void actionDeleteSprint() {
        if (selectedProject == null) return;
        Sprint sprint = sprintsList.getSelectedValue();
        if (sprint == null) return;
        if (!confirm("Delete sprint \"" + sprint.getName() + "\"?\nStories assigned to it will move to backlog.")) return;
        for (Story s : selectedProject.getStories()) {
            if (s.getSprint() == sprint) s.unassignFromSprint();
        }
        selectedProject.removeSprint(sprint);
        refreshProjectLists();
    }

    private void actionNewStory() {
        if (selectedProject == null) return;
        String title = promptNonEmpty("Story title:");
        if (title == null) return;
        String desc = promptNonEmpty("Description:");
        if (desc == null) return;
        selectedProject.addStory(new Story(title, desc));
    }

    private void actionEditStory() {
        if (selectedProject == null) return;
        Story story = storiesList.getSelectedValue();
        if (story == null) return;
        String title = promptNonEmpty("Story title:");
        if (title == null) return;
        String desc = promptNonEmpty("Description:");
        if (desc == null) return;
        story.setTitle(title);
        story.setDescription(desc);
    }

    private void actionDeleteStory() {
        if (selectedProject == null) return;
        Story story = storiesList.getSelectedValue();
        if (story == null) return;
        if (!confirm("Delete story \"" + story.getTitle() + "\"?")) return;
        story.unassignFromSprint();
        selectedProject.removeStory(story);
        if (selectedStory == story) setSelectedStory(null);
        refreshProjectLists();
    }

    private void actionChangeStorySprint() {
        if (selectedProject == null) return;
        Story story = storiesList.getSelectedValue();
        if (story == null) return;

        Object[] choices = new Object[selectedProject.getSprints().size() + 1];
        choices[0] = "(Backlog)";
        for (int i = 0; i < selectedProject.getSprints().size(); i++) choices[i + 1] = selectedProject.getSprints().get(i);

        Object chosen = JOptionPane.showInputDialog(
                frame,
                "Assign story to:",
                "Change Sprint",
                JOptionPane.PLAIN_MESSAGE,
                null,
                choices,
                story.isInBacklog() ? choices[0] : story.getSprint());
        if (chosen == null) return;

        if ("(Backlog)".equals(chosen)) {
            story.unassignFromSprint();
        } else {
            story.assignToSprint((Sprint) chosen);
        }
    }

    private void actionUpdateStoryPriority() {
        Story story = storiesList.getSelectedValue();
        if (story == null) return;
        WorkItem.Priority[] values = WorkItem.Priority.values();
        WorkItem.Priority chosen = (WorkItem.Priority) JOptionPane.showInputDialog(
                frame,
                "Choose priority:",
                "Update Priority",
                JOptionPane.PLAIN_MESSAGE,
                null,
                values,
                story.getPriority());
        if (chosen == null) return;
        story.setPriority(chosen);
    }

    private void actionUpdateStoryStatus() {
        Story story = storiesList.getSelectedValue();
        if (story == null) return;
        WorkItem.Status[] values = WorkItem.Status.values();
        WorkItem.Status chosen = (WorkItem.Status) JOptionPane.showInputDialog(
                frame,
                "Choose status:",
                "Update Status",
                JOptionPane.PLAIN_MESSAGE,
                null,
                values,
                story.getStatus());
        if (chosen == null) return;
        story.setStatus(chosen);
    }

    private void actionNewTask() {
        if (selectedStory == null) return;
        String title = promptNonEmpty("Task title:");
        if (title == null) return;
        String desc = promptNonEmpty("Description:");
        if (desc == null) return;
        selectedStory.addTask(new Task(title, desc));
    }

    private void actionEditTask() {
        if (selectedStory == null) return;
        Task task = tasksList.getSelectedValue();
        if (task == null) return;
        String title = promptNonEmpty("Task title:");
        if (title == null) return;
        String desc = promptNonEmpty("Description:");
        if (desc == null) return;
        task.setTitle(title);
        task.setDescription(desc);
        refreshTasks();
    }

    private void actionRemoveTask() {
        if (selectedStory == null) return;
        Task task = tasksList.getSelectedValue();
        if (task == null) return;
        selectedStory.removeTask(task);
    }

    private void actionUpdateTaskStatus() {
        if (selectedStory == null) return;
        Task task = tasksList.getSelectedValue();
        if (task == null) return;
        WorkItem.Status[] values = WorkItem.Status.values();
        WorkItem.Status chosen = (WorkItem.Status) JOptionPane.showInputDialog(
                frame,
                "Choose status:",
                "Task Status",
                JOptionPane.PLAIN_MESSAGE,
                null,
                values,
                task.getStatus());
        if (chosen == null) return;
        task.setStatus(chosen);
        refreshTasks();
    }

    private void actionUpdateTaskPriority() {
        if (selectedStory == null) return;
        Task task = tasksList.getSelectedValue();
        if (task == null) return;
        WorkItem.Priority[] values = WorkItem.Priority.values();
        WorkItem.Priority chosen = (WorkItem.Priority) JOptionPane.showInputDialog(
                frame,
                "Choose priority:",
                "Task Priority",
                JOptionPane.PLAIN_MESSAGE,
                null,
                values,
                task.getPriority());
        if (chosen == null) return;
        task.setPriority(chosen);
        refreshTasks();
    }

    private void actionManageTaskAssignee() {
        if (selectedStory == null) return;
        Task task = tasksList.getSelectedValue();
        if (task == null) return;

        String[] options = new String[] { "Assign", "Unassign" };
        int choice = JOptionPane.showOptionDialog(
                frame,
                "Task:\n" + task.getTitle() + "\n\nCurrent: " + (task.getAssignee() == null ? "Unassigned" : task.getAssignee()),
                "Task Assignee",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == 0) {
            if (blackboard.getDevelopers().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No developers exist yet. Create one in the Developers tab.");
                return;
            }
            Developer dev = (Developer) JOptionPane.showInputDialog(
                    frame,
                    "Assign which developer?",
                    "Assign Task",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    blackboard.getDevelopers().toArray(),
                    task.getAssignee());
            if (dev == null) return;
            task.setAssignee(dev);
            refreshTasks();
        } else if (choice == 1) {
            if (task.getAssignee() == null) return;
            task.setAssignee(null);
            refreshTasks();
        }
    }

    private void actionNewDeveloper() {
        String name = promptNonEmpty("Developer name:");
        if (name == null) return;
        String username = promptNonEmpty("Username:");
        if (username == null) return;
        blackboard.addDeveloper(new Developer(name, username));
    }

    private void actionRemoveDeveloper() {
        Developer dev = developersList.getSelectedValue();
        if (dev == null) return;
        if (!confirm("Remove developer \"" + dev + "\"?")) return;
        blackboard.removeDeveloper(dev);
    }

    private void actionManageStoryDevelopers() {
        if (selectedStory == null) return;

        String[] options = new String[] { "Assign developer", "Unassign developer" };
        int choice = JOptionPane.showOptionDialog(
                frame,
                "Manage developers for:\n" + selectedStory.getTitle(),
                "Story Developers",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == 0) {
            if (blackboard.getDevelopers().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No developers exist yet. Create one in the Developers tab.");
                return;
            }
            Developer dev = (Developer) JOptionPane.showInputDialog(
                    frame,
                    "Assign which developer?",
                    "Assign Developer",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    blackboard.getDevelopers().toArray(),
                    null);
            if (dev == null) return;
            selectedStory.addDeveloper(dev);
        } else if (choice == 1) {
            if (selectedStory.getDevelopers().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No developers are assigned to this story.");
                return;
            }
            Developer dev = (Developer) JOptionPane.showInputDialog(
                    frame,
                    "Unassign which developer?",
                    "Unassign Developer",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    selectedStory.getDevelopers().toArray(),
                    null);
            if (dev == null) return;
            selectedStory.removeDeveloper(dev);
        }
    }

    private String promptNonEmpty(String prompt) {
        while (true) {
            String value = JOptionPane.showInputDialog(frame, prompt);
            if (value == null) return null;
            value = value.trim();
            if (!value.isEmpty()) return value;
        }
    }

    private boolean confirm(String prompt) {
        return JOptionPane.showConfirmDialog(frame, prompt, "Confirm", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION;
    }
}

