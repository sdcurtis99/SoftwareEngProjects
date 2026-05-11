/**
 * This class represents the main action panel of the GUI.
 * It allows users to create stories, assign developers, and update status.
 *
 * @Shannon Curtis
 * @version 2.0
 */

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ActionPanel extends JPanel {

    public ActionPanel(ProjectPanel projectPanel, StoryPanel storyPanel) {
        setLayout(new GridLayout(0, 1));

        // =========================
        // CREATE STORY
        // =========================
        JButton addStory = new JButton("Create Story");
        addStory.addActionListener(e -> {
            Project p = projectPanel.getSelectedProject();
            if (p == null) {
                JOptionPane.showMessageDialog(this, "Select a project first.");
                return;
            }

            String title = JOptionPane.showInputDialog("Title:");
            if (title == null || title.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Title cannot be empty.");
                return;
            }

            String desc = JOptionPane.showInputDialog("Description:");
            if (desc == null) desc = "";

            p.addStory(new Story(title, desc));
        });

        // =========================
        // CREATE SPRINT
        // =========================
        JButton createSprint = new JButton("Create Sprint");
        createSprint.addActionListener(e -> {
            Project p = projectPanel.getSelectedProject();
            if (p == null) {
                JOptionPane.showMessageDialog(this, "Select a project first.");
                return;
            }

            String name = JOptionPane.showInputDialog("Sprint name:");
            if (name == null || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Sprint name cannot be empty.");
                return;
            }

            String start = JOptionPane.showInputDialog("Start date:");
            if (start == null) start = "";

            String end = JOptionPane.showInputDialog("End date:");
            if (end == null) end = "";

            p.addSprint(new Sprint(name, start, end));
        });

        // =========================
        // ASSIGN STORY TO SPRINT
        // =========================
        JButton assignSprint = new JButton("Assign to Sprint");
        assignSprint.addActionListener(e -> {
            Project p = projectPanel.getSelectedProject();
            Story s = storyPanel.getSelectedStory();

            if (p == null || s == null) {
                JOptionPane.showMessageDialog(this, "Select a project and story.");
                return;
            }

            if (p.getSprints().isEmpty()) {
                JOptionPane.showMessageDialog(this, "No sprints available.");
                return;
            }

            Sprint sprint = (Sprint) JOptionPane.showInputDialog(
                    this,
                    "Select Sprint",
                    "Assign",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    p.getSprints().toArray(new Sprint[0]),
                    null
            );

            if (sprint != null) {
                s.assignToSprint(sprint);
            }
        });

        // =========================
        // CREATE DEVELOPER
        // =========================
        JButton addDev = new JButton("Create Developer");
        addDev.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Name:");
            if (name == null || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name cannot be empty.");
                return;
            }

            String user = JOptionPane.showInputDialog("Username:");
            if (user == null || user.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username cannot be empty.");
                return;
            }

            Blackboard.getInstance().addDeveloper(new Developer(name, user));
        });

        // =========================
        // ASSIGN DEVELOPER
        // =========================
        JButton assignDev = new JButton("Assign Developer");
        assignDev.addActionListener(e -> {
            Story s = storyPanel.getSelectedStory();
            if (s == null) {
                JOptionPane.showMessageDialog(this, "Select a story first.");
                return;
            }

            List<Developer> devs = Blackboard.getInstance().getDevelopers();
            if (devs.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No developers exist.");
                return;
            }

            Developer d = (Developer) JOptionPane.showInputDialog(
                    this,
                    "Select Developer",
                    "Assign",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    devs.toArray(new Developer[0]),
                    null
            );

            if (d != null) {
                s.addDeveloper(d);
                storyPanel.repaint(); // ensure UI updates
            }
        });

        // =========================
        // MOVE TO BACKLOG
        // =========================
        JButton backlog = new JButton("Move to Backlog");
        backlog.addActionListener(e -> {
            Story s = storyPanel.getSelectedStory();
            if (s == null) {
                JOptionPane.showMessageDialog(this, "Select a story first.");
                return;
            }

            if (s.isInBacklog()) {
                JOptionPane.showMessageDialog(this, "Already in backlog.");
                return;
            }

            s.unassignFromSprint();
        });

        // =========================
        // UPDATE STATUS
        // =========================
        JButton status = new JButton("Update Status");
        status.addActionListener(e -> {
            Story s = storyPanel.getSelectedStory();
            if (s == null) {
                JOptionPane.showMessageDialog(this, "Select a story first.");
                return;
            }

            WorkItem.Status newStatus = (WorkItem.Status)
                    JOptionPane.showInputDialog(
                            this,
                            "Select status",
                            "Update",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            WorkItem.Status.values(),
                            s.getStatus()
                    );

            if (newStatus != null) {
                s.setStatus(newStatus);
            }
        });

        // =========================
        // ADD ALL BUTTONS
        // =========================
        add(addStory);
        add(createSprint);
        add(assignSprint);
        add(addDev);
        add(assignDev);
        add(backlog);
        add(status);
    }
}