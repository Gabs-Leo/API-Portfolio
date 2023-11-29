package com.gabsleo.portfolio.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabsleo.portfolio.utils.Response;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@Component
public class AuthenticationEntrypoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException
    {
        Response<String> apiResponse = new Response<String>();
        System.out.println(authException.toString());
        if (request.getAttribute("failed") != null) {
            if(request.getAttribute("failed").equals("expired")) {
                apiResponse.getErrors().add("Unauthorized. Token expired.");
                response.setStatus(401);
            } else {
                apiResponse.getErrors().add("Unauthorized. Invalid token.");
                response.setStatus(401);
            }
        } else {
            apiResponse.getErrors().add("Unauthorized. Token needed.");
            response.setStatus(401);
        }

        OutputStream outputStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(outputStream, apiResponse);
        outputStream.flush();
    }
}
