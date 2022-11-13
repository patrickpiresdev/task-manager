package com.ptk.task_manager.repositories;

import com.ptk.task_manager.entities.Task;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TaskRepository extends Repository<Task, Long> {
    void save(Task task);

    List<Task> findAll();

    Task findById(long id);

    void deleteById(long id);
}
