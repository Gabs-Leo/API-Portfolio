package com.gabsleo.portfolio.services;

import com.gabsleo.portfolio.entitites.AppUser;
import com.gabsleo.portfolio.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserService {
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.appUserRepository = appUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public AppUser save(AppUser appUser){
        return appUserRepository.save(appUser);
    }

    public AppUser register(AppUser appUser){
        return this.save(
                appUser.setPassword(
                        bCryptPasswordEncoder.encode(appUser.getPassword())
                )
        );
    }

    public Optional<AppUser> findByEmail(String email){
        return appUserRepository.findByEmail(email);
    }
}
