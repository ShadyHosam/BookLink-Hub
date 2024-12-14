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
    private String firstname;
    @NotEmpty(message = "Lastname name is required")
    private String lastname;
    @NotEmpty(message = "Email should be valid")
    private String email;
    @Size(min = 8, message = "Password should be at least 8 characters")
    private String password;

}
