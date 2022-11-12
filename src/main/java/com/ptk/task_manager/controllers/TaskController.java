package com.ptk.task_manager.controllers;

import com.ptk.task_manager.entities.Task;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private List<Task> tasks = new ArrayList<>();

    @PostMapping
    public void create(@RequestBody Task task) {
        tasks.add(task);
    }

    @GetMapping
    public List<Task> list() {
        return tasks;
    }

    @GetMapping("/{id}")
    public Task show(@PathVariable int id) {
        return tasks.get(id-1);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable int id, @RequestBody Task task) {
        tasks.add(id-1, task);
        tasks.remove(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        tasks.remove(id-1);
    }

    @PutMapping("/{id}/complete")
    public void complete(@PathVariable int id) {
        tasks.get(id-1).complete();
    }
}
