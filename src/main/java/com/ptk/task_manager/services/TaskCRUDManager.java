package com.ptk.task_manager.services;

import com.ptk.task_manager.entities.Task;
import com.ptk.task_manager.exceptions.TaskNotFoundException;
import com.ptk.task_manager.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class TaskCRUDManager {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskCRUDManager(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public long create(Task task) {
        taskRepository.save(task);
        return task.getId();
    }

    public Collection<Task> list(String username) {
        return taskRepository.findAllByOwner_username(username);
    }

    public Task findTask(long id, String ownerUsername) {
        Optional<Task> task = taskRepository.findById(id);

        if (!task.isPresent() || !task.get().getOwnerUsername().equals(ownerUsername))
            throw new TaskNotFoundException();

        return task.get();
    }

    public void update(Task task) {
        Optional<Task> taskToUpdate = taskRepository.findById(task.getId());

        if (!taskToUpdate.isPresent() || !taskToUpdate.get().getOwnerUsername().equals(task.getOwnerUsername()))
            throw new TaskNotFoundException();

        taskToUpdate.get().setLabel(task.getLabel());
        taskToUpdate.get().setDescription(task.getDescription());
        taskToUpdate.get().setDone(task.isDone());

        taskRepository.save(taskToUpdate.get());
    }

    public void delete(long id, String ownerUsername) {
        Optional<Task> task = taskRepository.findById(id);

        if (!task.isPresent() || !task.get().getOwnerUsername().equals(ownerUsername))
            throw new TaskNotFoundException();

        taskRepository.deleteById(id);
    }

    public void complete(long id, String ownerUsername) {
        Optional<Task> task = taskRepository.findById(id);

        if (!task.isPresent() || !task.get().getOwnerUsername().equals(ownerUsername))
            throw new TaskNotFoundException();

        task.get().complete();
        taskRepository.save(task.get());
    }
}
