package com.ptk.task_manager.dtos;

import javax.validation.constraints.NotBlank;

public class UserDto {
    @NotBlank public String username;
    @NotBlank public String password;
}
