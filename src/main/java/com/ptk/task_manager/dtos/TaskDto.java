package com.ptk.task_manager.dtos;

public class TaskDto {
    public long id;
    public String label;
    public String description;
    public boolean done;

    public TaskDto(long id, String label, String description, boolean done) {
        this.id = id;
        this.label = label;
        this.description = description;
        this.done = done;
    }
}
