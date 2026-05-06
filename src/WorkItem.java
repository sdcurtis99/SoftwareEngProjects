import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class WorkItem {
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    protected final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    enum Status {
        BACKLOG,
        IN_PROGRESS,
        DONE
    }

    enum Priority {
        LOW,
        MEDIUM,
        HIGH
    }

    public WorkItem(String title, String description, Status status, Priority priority) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
    }

    // Force subclasses to define how they will display their data.
    public abstract void displayDetails();

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        pcs.firePropertyChange(propertyName, oldValue, newValue);
    }

    public String getTitle() { return title; }
    public void setTitle(String title) {
        String old = this.title;
        this.title = title;
        firePropertyChange("title", old, title);
    }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        String old = this.description;
        this.description = description;
        firePropertyChange("description", old, description);
    }

    public Status getStatus() { return status; }
    public void setStatus(Status status) {
        Status old = this.status;
        this.status = status;
        firePropertyChange("status", old, status);
    }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) {
        Priority old = this.priority;
        this.priority = priority;
        firePropertyChange("priority", old, priority);
    }
}