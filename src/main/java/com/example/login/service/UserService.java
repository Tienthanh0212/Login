package com.example.login.service;

import com.example.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public interface UserService {

}

@Service
class UserServiceImpl implements UserService{
    @Autowired
    UserRepository userRepository;

}
