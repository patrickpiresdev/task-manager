package com.ptk.task_manager.services;

import com.ptk.task_manager.controllers.TaskNotFoundException;
import com.ptk.task_manager.dtos.TaskDto;
import com.ptk.task_manager.entities.Task;
import com.ptk.task_manager.entities.User;
import com.ptk.task_manager.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskCRUDManager {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskCRUDManager(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void create(TaskDto taskDto, User user) {
        taskRepository.save(taskFrom(taskDto, user));
    }

    public List<TaskDto> list(String username) {
        return taskRepository.findAllByOwner_username(username)
                .stream()
                .map(this::taskDtoFrom)
                .collect(Collectors.toList());
    }

    public TaskDto findTask(long id, String username) {
        Optional<Task> task = taskRepository.findById(id);

        if (!task.isPresent() || !task.get().getOwnerUsername().equals(username))
            throw new TaskNotFoundException();

        return taskDtoFrom(task.get());
    }

    public void update(TaskDto taskDto, String username) {
        Optional<Task> task = taskRepository.findById(taskDto.id);

        if (!task.isPresent() || !task.get().getOwnerUsername().equals(username))
            throw new TaskNotFoundException();

        // todo: create a mapper
        task.get().setLabel(taskDto.label);
        task.get().setDescription(taskDto.description);
        task.get().setDone(taskDto.done);

        taskRepository.save(task.get());
    }

    public void delete(long id, String username) {
        Optional<Task> task = taskRepository.findById(id);

        if (!task.isPresent() || !task.get().getOwnerUsername().equals(username))
            throw new TaskNotFoundException();

        taskRepository.deleteById(id);
    }

    public void complete(long id) {
        Optional<Task> task = taskRepository.findById(id);

        if (!task.isPresent()) throw new TaskNotFoundException();

        task.get().complete();
        taskRepository.save(task.get());
    }

    // todo: create mapper to user and userdetails
    private Task taskFrom(TaskDto taskDto, User owner) {
        return new Task(taskDto.id, taskDto.label, taskDto.description,
                taskDto.done, owner);
    }

    // todo: create a mapper
    private TaskDto taskDtoFrom(Task task) {
        return new TaskDto(
                task.getId(),
                task.getLabel(),
                task.getDescription(),
                task.isDone());
    }
}
