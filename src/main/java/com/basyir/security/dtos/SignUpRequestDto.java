package com.basyir.security.dtos;

import com.basyir.security.annotations.PasswordChecker;
import com.basyir.security.entities.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class SignUpRequestDto {

    @NotNull(message = "name field cannot be null")
    @NotBlank(message = "Name should not be Empty ")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @PasswordChecker
    private String password;

    @NotEmpty
    private Set<String> roles;
}
