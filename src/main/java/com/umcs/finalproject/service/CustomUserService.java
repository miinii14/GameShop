package com.umcs.finalproject.service;

import com.umcs.finalproject.model.Role;
import com.umcs.finalproject.model.User;
import com.umcs.finalproject.repository.RoleRepository;
import com.umcs.finalproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CustomUserService implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public boolean login(User request) {
        Optional<User> user = userRepository.findByLogin(request.getLogin());
        System.out.println(user.get().getRoles());
        return user.isPresent() && passwordEncoder.matches(request.getPassword(), user.get().getPassword());
    }

    @Override
    public boolean register(User request) {
        Optional<User> user = userRepository.findByLogin(request.getLogin());
        if(user.isPresent()) {
            return false;
        }
        user = Optional.of(new User());
        user.get().setLogin(request.getLogin());
        user.get().setPassword(passwordEncoder.encode(request.getPassword()));
        user.get().getRoles().add(roleRepository.findByName("USER").get());
        userRepository.save(user.get());
        return true;
    }

    @Override
    public User getUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        return userRepository.findById(id).get();
    }
}
