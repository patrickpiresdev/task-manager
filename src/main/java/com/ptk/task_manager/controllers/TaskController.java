package com.ptk.task_manager.controllers;

import com.ptk.task_manager.dtos.TaskDto;
import com.ptk.task_manager.entities.Task;
import com.ptk.task_manager.entities.User;
import com.ptk.task_manager.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
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
    public List<TaskDto> list(@AuthenticationPrincipal UserDetails userDetails) {
        return taskRepository.findAllByOwner_username(userDetails.getUsername())
                .stream()
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
    public TaskDto show(@PathVariable long id, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Task> task = taskRepository.findById(id);

        // todo: handle exception returning the correct answer to the client
        if (!task.isPresent() || !task.get().getOwnerUsername().equals(userDetails.getUsername()))
            throw new TaskNotFoundException();

        return taskDtoFrom(task.get());
    }

    @PutMapping
    public void update(@RequestBody TaskDto taskDto, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Task> task = taskRepository.findById(taskDto.id);

        // todo: handle exception returning the correct answer to the client
        if (!task.isPresent() || !task.get().getOwnerUsername().equals(userDetails.getUsername()))
            throw new TaskNotFoundException();

        // todo: create a mapper
        task.get().setLabel(taskDto.label);
        task.get().setDescription(taskDto.description);
        task.get().setDone(taskDto.done);

        taskRepository.save(task.get());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Task> task = taskRepository.findById(id);

        // todo: handle exception returning the correct answer to the client
        if (!task.isPresent() || !task.get().getOwnerUsername().equals(userDetails.getUsername()))
            throw new TaskNotFoundException();

        taskRepository.deleteById(id);
    }

    @PutMapping("/{id}/complete")
    public void complete(@PathVariable int id) {
        Optional<Task> task = taskRepository.findById(id);

        // todo: handle exception returning the correct answer to the client
        if (!task.isPresent()) throw new TaskNotFoundException();

        task.get().complete();
        taskRepository.save(task.get());
    }
}
