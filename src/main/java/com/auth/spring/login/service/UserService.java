package com.auth.spring.login.service;



import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.auth.spring.login.models.User;
import com.auth.spring.login.repository.RoleRepository;
import com.auth.spring.login.repository.UserRepository;
import com.auth.spring.login.security.jwt.JwtUtils;
import com.auth.spring.login.security.services.UserDetailsServiceImpl;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    private UserDetailsServiceImpl userDetails;
    private JwtUtils jwtTokenUtils;


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }
    public UserDetails whoami(HttpServletRequest req) {
        return userDetails.loadUserByUsername(jwtTokenUtils.getUserNameFromJwtToken(jwtTokenUtils.getJwtFromCookies(req)));
    }
}
