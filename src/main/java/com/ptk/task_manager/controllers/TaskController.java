package com.ptk.task_manager.controllers;

import com.ptk.task_manager.dtos.TaskDto;
import com.ptk.task_manager.entities.Task;
import com.ptk.task_manager.entities.User;
import com.ptk.task_manager.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @PostMapping
    public void create(@RequestBody TaskDto taskDto, @AuthenticationPrincipal UserDetails userDetails) {
        // todo: create mapper to user and userdetails
        taskRepository.save(
            taskFrom(taskDto,
                new User(userDetails.getUsername(), userDetails.getPassword())));
    }

    // todo: create a mapper
    private Task taskFrom(TaskDto taskDto, User owner) {
        return new Task(taskDto.id, taskDto.label, taskDto.description,
                taskDto.done, owner);
    }

    @GetMapping
    public List<TaskDto> list() {
        return taskRepository.findAll().stream()
                .map(this::taskDtoFrom)
                .collect(Collectors.toList());
    }

    // todo: create a mapper
    private TaskDto taskDtoFrom(Task task) {
        return new TaskDto(
                task.getId(),
                task.getLabel(),
                task.getDescription(),
                task.isDone());
    }

    @GetMapping("/{id}")
    public Task show(@PathVariable long id) {
        return taskRepository.findById(id);
    }

    @PutMapping
    public void update(@RequestBody TaskDto taskDto) {
        // todo: create a mapper
        Task taskToUpdate = taskRepository.findById(taskDto.id);
        taskToUpdate.setLabel(taskDto.label);
        taskToUpdate.setDescription(taskDto.description);
        taskToUpdate.setDone(taskDto.done);
        taskRepository.save(taskToUpdate);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        taskRepository.deleteById(id);
    }

    @PutMapping("/{id}/complete")
    public void complete(@PathVariable int id) {
        // todo: handle exception when task == null
        Task task = taskRepository.findById(id);
        task.complete();
        taskRepository.save(task);
    }
}
