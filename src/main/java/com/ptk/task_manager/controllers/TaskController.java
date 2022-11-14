package com.ptk.task_manager.controllers;

import com.ptk.task_manager.dtos.TaskDto;
import com.ptk.task_manager.entities.User;
import com.ptk.task_manager.services.TaskCRUDManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskCRUDManager taskCrudManager;

    @Autowired
    public TaskController(TaskCRUDManager taskCrudManager) {
        this.taskCrudManager = taskCrudManager;
    }

    @PostMapping
    public void create(@Valid @RequestBody TaskDto taskDto, @AuthenticationPrincipal UserDetails userDetails) {
        taskCrudManager.create(taskDto, new User(userDetails.getUsername(), userDetails.getPassword()));
    }

    @GetMapping
    public List<TaskDto> list(@AuthenticationPrincipal UserDetails userDetails) {
        return taskCrudManager.list(userDetails.getUsername());
    }

    @GetMapping("/{id}")
    public TaskDto show(@PathVariable long id, @AuthenticationPrincipal UserDetails userDetails) {
        // todo: handle exception returning the correct answer to the client
        return taskCrudManager.findTask(id, userDetails.getUsername());
    }

    @PutMapping
    public void update(@Valid @RequestBody TaskDto taskDto, @AuthenticationPrincipal UserDetails userDetails) {
        // todo: handle exception returning the correct answer to the client
        taskCrudManager.update(taskDto, userDetails.getUsername());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id, @AuthenticationPrincipal UserDetails userDetails) {
        // todo: handle exception returning the correct answer to the client
        taskCrudManager.delete(id, userDetails.getUsername());
    }

    @PutMapping("/{id}/complete")
    public void complete(@PathVariable long id) {
        // todo: handle exception returning the correct answer to the client
        taskCrudManager.complete(id);
    }
}
