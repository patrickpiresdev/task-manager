package com.ptk.task_manager.controllers.converters;

import com.ptk.task_manager.controllers.dtos.TaskDto;
import com.ptk.task_manager.entities.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskConverter {
    public Task taskFrom(TaskDto taskDto) {
        return new Task(
                taskDto.getId(),
                taskDto.getLabel(),
                taskDto.getDescription(),
                taskDto.isDone());
    }

    public TaskDto dtoFrom(Task task) {
        return new TaskDto(
                task.getId(),
                task.getLabel(),
                task.getDescription(),
                String.valueOf(task.isDone()));
    }
}
