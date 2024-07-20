package com.shady.book.auth;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.Registration;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name="Authentication")
public class authenticationController {


    private final AuthenticationService service;
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(@RequestBody @Val id RegistrationRequest request){

         service.register(request);
        return ResponseEntity.accepted().build();

    }
}
