package com.ptk.task_manager.controllers;

import com.ptk.task_manager.controllers.dtos.UserDto;
import com.ptk.task_manager.entities.User;
import com.ptk.task_manager.exceptions.UsernameAlreadyTakenException;
import com.ptk.task_manager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class RegistrationController {
    private final UserRepository userRepository;

    @Autowired
    public RegistrationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<Object> register(@Valid @RequestBody UserDto userDto, BCryptPasswordEncoder bCryptPasswordEncoder) {
        try {
            registerUser(userDto, bCryptPasswordEncoder);
            return ResponseEntity.ok().build();
        } catch (UsernameAlreadyTakenException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    private void registerUser(UserDto userDto, BCryptPasswordEncoder bCryptPasswordEncoder) {
        Optional<User> user = userRepository.findByUsername(userDto.getUsername());
        if (user.isPresent())
            throw new UsernameAlreadyTakenException("Username '" + userDto.getUsername() + "' is already taken!");
        userRepository.save(
                new User(userDto.getUsername(), bCryptPasswordEncoder.encode(userDto.getPassword()))
        );
    }
}
