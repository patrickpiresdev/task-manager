package com.ptk.task_manager.repositories;

import com.ptk.task_manager.entities.User;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserRepository extends Repository<User, String> {
    Optional<User> findByUsername(String username);
    void save(User user);
}
