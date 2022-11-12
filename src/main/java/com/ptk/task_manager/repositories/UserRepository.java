package com.ptk.task_manager.repositories;

import com.ptk.task_manager.entities.User;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

@Component
public interface UserRepository extends Repository<User, String> {
    User findByUsername(String username);

    void save(User user);
}
