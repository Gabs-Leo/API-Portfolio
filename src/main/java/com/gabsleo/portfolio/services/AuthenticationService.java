package com.gabsleo.portfolio.services;

import com.gabsleo.portfolio.dtos.JwtTokenDto;
import com.gabsleo.portfolio.dtos.LoginDto;
import com.gabsleo.portfolio.entitites.AppUser;
import com.gabsleo.portfolio.repositories.AppUserRepository;
import com.gabsleo.portfolio.security.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(AppUserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.appUserRepository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public JwtTokenDto register(LoginDto request) {
        AppUser user = new AppUser()
                .setEmail(request.email())
                .setPassword(passwordEncoder.encode(request.password()))
                .setRole(Roles.USER);

        appUserRepository.save(user);
        String token = jwtService.generateToken(user);
        return new JwtTokenDto(token);
    }

    public JwtTokenDto authenticate(LoginDto request) {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.email(),
                    request.password()
            ));

            AppUser user = appUserRepository.findByEmail(request.email()).get();
            String token = jwtService.generateToken(user);
            return new JwtTokenDto(token);
        } catch(Exception e) {
            System.out.println(e);
            return new JwtTokenDto("");
        }
    }
}
