package com.ptk.task_manager.entities;

import javax.persistence.*;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue
    private long id;
    private String label;
    private String description;
    private boolean done;

    @ManyToOne
    @JoinColumn(name = "owner_username")
    private User owner;

    public Task(long id, String label, String description, boolean done) {
        this.id = id;
        this.label = label;
        this.description = description;
        this.done = done;
    }

    public long getId() {
        return id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isDone() {
        return done;
    }

    public void complete() {
        done = true;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getOwnerUsername() {
        return this.owner.getUsername();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Task)) return false;
        if (o == this) return true;
        Task other = (Task) o;
        return id == other.id &&
                label.equals(other.label) &&
                description.equals(other.description) &&
                done == other.done &&
                owner.equals(other.owner);
    }
}
