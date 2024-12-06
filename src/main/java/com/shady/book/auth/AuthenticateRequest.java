package com.shady.book.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticateRequest {

    @Email(message = "email should be valid")
    @NotEmpty(message = "email  is required")
    @NotBlank(message = "email cannot be blank")
    private String email;


    @NotEmpty(message = "password  is required")
    @NotBlank(message = "password cannot be blank")
    @Size(min = 8, message = "password should be at least 8 characters")
    private String password;
}
