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

    public Task() {}

    public Task(String label) {
        this.label = label;
        description = "";
        done = false;
    }

    public Task(long id, String label, String description, boolean done, User owner) {
        this.id = id;
        this.label = label;
        this.description = description;
        this.done = done;
        this.owner = owner;
    }

    public void setId(long id) {
        this.id = id;
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
}
