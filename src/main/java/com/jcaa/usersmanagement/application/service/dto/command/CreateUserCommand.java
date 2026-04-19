package com.jcaa.usersmanagement.application.service.dto.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// VIOLACIÓN Regla 3: se mezcla @Builder de Lombok con un record.
// Los records ya tienen constructor canónico — usar @Builder es redundante e innecesario.
public record CreateUserCommand(
        @NotBlank String id,
        @NotBlank @Size(min = 3) String name,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8) String password,
        @NotBlank String role) {

}
