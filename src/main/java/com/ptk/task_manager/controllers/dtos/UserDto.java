package com.ptk.task_manager.controllers.dtos;

import javax.validation.constraints.NotBlank;

public class UserDto {
    @NotBlank private String username;
    @NotBlank private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
