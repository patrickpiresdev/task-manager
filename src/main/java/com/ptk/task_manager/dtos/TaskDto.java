package com.ptk.task_manager.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TaskDto {
    public long id;

    @NotBlank public String label;
    @NotNull public String description;
    public boolean done;

    public TaskDto(long id, String label, String description, boolean done) {
        this.id = id;
        this.label = label;
        this.description = description;
        this.done = done;
    }
}
