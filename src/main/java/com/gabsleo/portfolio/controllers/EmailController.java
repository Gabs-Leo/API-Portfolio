package com.gabsleo.portfolio.controllers;

import com.gabsleo.portfolio.dtos.EmailDto;
import com.gabsleo.portfolio.services.EmailService;
import com.gabsleo.portfolio.utils.Response;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/emails")
public class EmailController {
    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<Response<String>> send(@Valid @RequestBody EmailDto emailDto, BindingResult result){
        Response<String> response = new Response<>();
        if(result.hasErrors()){
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }
        emailService.send(emailDto);
        response.setContent("Email sent successfully!");
        return ResponseEntity.ok(response);
    }
}
