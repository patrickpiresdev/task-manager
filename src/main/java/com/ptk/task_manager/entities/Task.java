package com.ptk.task_manager.entities;

public class Task {

    private String label;
    private String description;
    private boolean done;

    public Task() {}

    public Task(String label) {
        this.label = label;
        description = "";
        done = false;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return done;
    }

    public void complete() {
        done = true;
    }
}
