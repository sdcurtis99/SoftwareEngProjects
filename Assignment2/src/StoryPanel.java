/**
 * This class represents the main action panel of the GUI.
 * It allows users to create stories, assign developers, and update status.
 *
 * @Shannon Curtis
 * @version 2.0
 */

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class StoryPanel extends JPanel implements PropertyChangeListener {

    private DefaultListModel<Story> model = new DefaultListModel<>();
    private JList<Story> list = new JList<>(model);

    private Project currentProject;

    public StoryPanel(ProjectPanel projectPanel) {
        setLayout(new BorderLayout());

        add(new JLabel("Stories"), BorderLayout.NORTH);
        add(new JScrollPane(list), BorderLayout.CENTER);

        projectPanel.getList().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                setProject(projectPanel.getSelectedProject());
            }
        });
    }

    public Story getSelectedStory() {
        return list.getSelectedValue();
    }

    private void setProject(Project project) {
        if (currentProject != null) {
            currentProject.removePropertyChangeListener(this);
        }

        currentProject = project;
        model.clear();

        if (project == null) return;

        for (Story s : project.getStories()) {
            model.addElement(s);
            attachStoryListener(s);
        }

        project.addPropertyChangeListener(this);
    }

    private void attachStoryListener(Story story) {
        story.addPropertyChangeListener(evt -> {
            String prop = evt.getPropertyName();

            if ("status".equals(prop) ||
                    "developers".equals(prop) ||
                    "sprint".equals(prop)) {
                list.repaint();
            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("stories".equals(evt.getPropertyName())) {

            if (evt.getNewValue() != null) {
                Story s = (Story) evt.getNewValue();
                model.addElement(s);
                attachStoryListener(s);
            }

            if (evt.getOldValue() != null) {
                model.removeElement((Story) evt.getOldValue());
            }
        }
    }
}