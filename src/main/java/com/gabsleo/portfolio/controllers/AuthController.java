package com.gabsleo.portfolio.controllers;

import com.gabsleo.portfolio.dtos.LoginDto;
import com.gabsleo.portfolio.repositories.AppUserRepository;
import com.gabsleo.portfolio.services.AuthenticationService;
import com.gabsleo.portfolio.utils.Response;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin("*")
public class AuthController {
    private final AuthenticationService service;
    private final AppUserRepository userRepository;

    @Autowired
    public AuthController(AuthenticationService service, AppUserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<Response<String>> helloWorld(){
        var response = new Response<>("Hello World!");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/auth/hello-adm")
    public ResponseEntity<Response<String>> helloAdm(){
        var response = new Response<>("Hello Adm!");
        return ResponseEntity.ok(response);
    }
/*
    @PostMapping("/auth/register")
    public ResponseEntity<Response<String>> register(@Valid @RequestBody LoginDto request, BindingResult result) {
        Response<String> response = new Response<>();
        if(result.hasErrors()){
            result.getAllErrors().forEach(i -> response.getErrors().add(i.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        } else if(userRepository.findByEmail(request.email()).isPresent()) {
            response.getErrors().add("Email already in use.");
            return ResponseEntity.badRequest().body(response);
        }

        response.setData(service.register(request).token());
        return ResponseEntity.ok(response);
    }
*/
    @PostMapping("/auth/login")
    public ResponseEntity<Response<String>> authenticate(@RequestBody LoginDto request) {
        Response<String> response = new Response<>();
        var authentication = service.authenticate(request);
        if(authentication.token().equals("")) {
            if(userRepository.findByEmail(request.email()).isEmpty())
                response.getErrors().add("Email not found.");
            else
                response.getErrors().add("Incorrect password.");
            return ResponseEntity.badRequest().body(response);
        }
        response.setContent(authentication.token());
        return ResponseEntity.ok(response);
    }

}
