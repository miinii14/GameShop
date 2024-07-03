package com.umcs.finalproject.service;

import com.umcs.finalproject.model.User;

public interface UserService {
    boolean login(User user);
    boolean register(User user);
    User getUserById(Long id);
}
