package com.ptk.task_manager.controllers;

import com.ptk.task_manager.controllers.dtos.TaskDto;
import com.ptk.task_manager.controllers.mappers.TaskConverter;
import com.ptk.task_manager.entities.Task;
import com.ptk.task_manager.entities.User;
import com.ptk.task_manager.exceptions.TaskNotFoundException;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskCRUDManager taskCrudManager;
    private final TaskConverter taskConverter;

    @Autowired
    public TaskController(TaskCRUDManager taskCrudManager, TaskConverter taskConverter) {
        this.taskCrudManager = taskCrudManager;
        this.taskConverter = taskConverter;
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody TaskDto taskDto,
                                         @AuthenticationPrincipal UserDetails userDetails,
                                         UriComponentsBuilder uriComponentsBuilder) {
        try {
            Task task = taskConverter.taskFrom(taskDto);
            task.setOwner(new User(userDetails.getUsername(), userDetails.getPassword()));
            long id = taskCrudManager.create(task);

            URI uri = uriComponentsBuilder.path("/tasks/{id}").build(id);
            return ResponseEntity.created(uri).build();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> list(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                taskCrudManager.list(userDetails.getUsername())
                        .stream()
                        .map(taskConverter::dtoFrom)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> show(@PathVariable long id, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Task task = taskCrudManager.findTask(id, userDetails.getUsername());
            TaskDto taskDto = taskConverter.dtoFrom(task);
            return ResponseEntity.ok(taskDto);
        } catch (TaskNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping
    public ResponseEntity<Object> update(@Valid @RequestBody TaskDto taskDto, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Task task = taskConverter.taskFrom(taskDto);
            task.setOwner(new User(userDetails.getUsername(), userDetails.getPassword()));
            taskCrudManager.update(task);
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
