package com.gabsleo.portfolio.dtos;

import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.Length;

public record EmailDto(
        String name,
        @Email(message = "Invalid email address.") String email,
        @Length(min = 10, message = "Invalid phone number.") String phone,
        @Length(min = 5, max = 200, message = "Invalid character size, message should have from 5 to 200 characters.")
        String message)

{  }
