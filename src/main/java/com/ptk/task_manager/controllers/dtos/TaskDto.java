package com.ptk.task_manager.controllers.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class TaskDto {
    private long id;
    @NotBlank private String label;
    @NotNull private String description;

    @NotNull
    @Pattern(regexp = "^true$|^false$")
    private String done;

    public TaskDto(long id, String label, String description, String done) {
        this.id = id;
        this.label = label;
        this.description = description;
        this.done = done;
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

    public void setDone(String done) {
        this.done = done;
    }

    public boolean isDone() {
        return Boolean.parseBoolean(done);
    }
}
