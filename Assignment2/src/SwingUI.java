/**
 * This class represents the main action panel of the GUI.
 * It allows users to create stories, assign developers, and update status.
 *
 * @Shannon Curtis
 * @version 2.0
 */

import javax.swing.*;
import java.awt.*;

public class SwingUI {

    public void start() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Taigai PM");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 600);
            frame.setLayout(new BorderLayout());

            ProjectPanel projectPanel = new ProjectPanel();
            StoryPanel storyPanel = new StoryPanel(projectPanel);
            ActionPanel actionPanel = new ActionPanel(projectPanel, storyPanel);

            frame.add(projectPanel, BorderLayout.WEST);
            frame.add(storyPanel, BorderLayout.CENTER);
            frame.add(actionPanel, BorderLayout.EAST);

            frame.setVisible(true);
        });
    }
}