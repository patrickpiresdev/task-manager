package com.ptk.task_manager.controllers;

import com.ptk.task_manager.dtos.UserDto;
import com.ptk.task_manager.entities.User;
import com.ptk.task_manager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class RegistrationController {
    private final UserRepository userRepository;

    @Autowired
    public RegistrationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public void register(@RequestBody UserDto userDto, BCryptPasswordEncoder bCryptPasswordEncoder) {
        userRepository.save(
                new User(userDto.username, bCryptPasswordEncoder.encode(userDto.password))
        );
    }
}
