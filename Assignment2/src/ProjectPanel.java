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

public class ProjectPanel extends JPanel implements PropertyChangeListener {

    private DefaultListModel<Project> model = new DefaultListModel<>();
    private JList<Project> list = new JList<>(model);

    public ProjectPanel() {
        setLayout(new BorderLayout());

        add(new JLabel("Projects"), BorderLayout.NORTH);
        add(new JScrollPane(list), BorderLayout.CENTER);

        JButton addBtn = new JButton("Add Project");
        addBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Project name:");
            if (name != null && !name.isEmpty()) {
                Blackboard.getInstance().addProject(new Project(name));
            }
        });

        add(addBtn, BorderLayout.SOUTH);

        Blackboard.getInstance().addPropertyChangeListener(this);

        // initial load
        Blackboard.getInstance().getProjects().forEach(model::addElement);
    }

    public Project getSelectedProject() {
        return list.getSelectedValue();
    }

    public JList<Project> getList() {
        return list;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("projects".equals(evt.getPropertyName())) {
            if (evt.getNewValue() != null) {
                model.addElement((Project) evt.getNewValue());
            } else if (evt.getOldValue() != null) {
                model.removeElement((Project) evt.getOldValue());
            }
        }
    }
}