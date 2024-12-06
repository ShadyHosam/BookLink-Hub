package com.shady.book.auth;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegistrationRequest {
    @NotEmpty(message = "First name is required")
    @NotBlank(message = "First name cannot be blank")
    private String firstname;
    @NotEmpty(message = "lastName name is required")
    @NotBlank(message = "lastName name cannot be blank")
    private String lastname;
    @Email(message = "email should be valid")
    @NotEmpty(message = "email  is required")
    @NotBlank(message = "email cannot be blank")
    private String email;

    @NotEmpty(message = "password  is required")

    @NotBlank(message = "password cannot be blank")
    @Size(min = 8, message = "password should be at least 8 characters")
    private String password;

}
