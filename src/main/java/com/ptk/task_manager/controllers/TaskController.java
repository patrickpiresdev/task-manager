package com.ptk.task_manager.controllers;

import com.ptk.task_manager.dtos.TaskDto;
import com.ptk.task_manager.entities.User;
import com.ptk.task_manager.services.TaskCRUDManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
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
    public ResponseEntity<Object> create(@Valid @RequestBody TaskDto taskDto,
                                         @AuthenticationPrincipal UserDetails userDetails, UriComponentsBuilder uriComponentsBuilder) {
        try {
            long id = taskCrudManager.create(taskDto,
                    new User(userDetails.getUsername(), userDetails.getPassword()));
            URI uri = uriComponentsBuilder.path("/tasks/{id}").build(id);
            return ResponseEntity.created(uri).build();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> list(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(taskCrudManager.list(userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> show(@PathVariable long id, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            TaskDto task = taskCrudManager.findTask(id, userDetails.getUsername());
            return ResponseEntity.ok(task);
        } catch (TaskNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping
    public ResponseEntity<Object> update(@Valid @RequestBody TaskDto taskDto, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            taskCrudManager.update(taskDto, userDetails.getUsername());
            return ResponseEntity.ok().build();
        } catch (TaskNotFoundException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable long id, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            taskCrudManager.delete(id, userDetails.getUsername());
            return ResponseEntity.ok().build();
        } catch (TaskNotFoundException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Object> complete(@PathVariable long id) {
        try {
            taskCrudManager.complete(id);
            return ResponseEntity.ok().build();
        } catch (TaskNotFoundException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
