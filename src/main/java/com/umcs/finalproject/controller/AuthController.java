package com.umcs.finalproject.controller;

import com.umcs.finalproject.model.User;
import com.umcs.finalproject.repository.UserRepository;
import com.umcs.finalproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody User user){
        if(userService.login(user)){
            user = userRepository.findByLogin(user.getLogin()).get();
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.badRequest().body("Invalid username or password");
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody User user){
        if(userService.register(user)){
            return ResponseEntity.ok().body("Success");
        }
        return ResponseEntity.badRequest().body("Login already exist");
    }
}
