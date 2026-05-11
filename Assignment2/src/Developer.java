/**
 * This class represents the main action panel of the GUI.
 * It allows users to create stories, assign developers, and update status.
 *
 * @Shannon Curtis
 * @version 2.0
 */

public class Developer {
    private String name;
    private final String username;

    public Developer(String name, String username) {
        this.name = name;
        this.username = username;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUsername() { return username; }

    public void displayDetails() {
        System.out.println("Developer: " + name);
        System.out.println("Username: " + username);
    }

    @Override
    public String toString() { return name + " (" + username + ")"; }
}