package com.example.login.service;

import com.example.login.DTO.UserDTO;
import com.example.login.entity.User;
import com.example.login.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public interface UserService {

    void update (UserDTO userDTO);

    void delete (Integer id);
    void signup (UserDTO userDTO);

    User signUp1(String username,  String password);

    boolean login(String username, String password);
}

@Service
class UserServiceImpl implements UserService{
    @Autowired
    UserRepository userRepository;


    @Override
    public void update(UserDTO userDTO) {
        ModelMapper mapper = new ModelMapper();
        mapper.createTypeMap(UserDTO.class, User.class)
                .setProvider(p -> userRepository.findById(userDTO.getId()).orElseThrow());

        User user = mapper.map(userDTO, User.class);
        userRepository.save(user);
    }
    @Override
    public void delete(Integer id) {
        userRepository.deleteById(id);
    }

    //login su dung spring security
    @Override
    public void signup(UserDTO userDTO) {
        User user = new ModelMapper().map(userDTO, User.class);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("Username already exists");
        }
        userRepository.save(user);
        userDTO.setId(user.getId());
    }


    //login khong su dung spring security
    @Override
    public User signUp1(String username, String password) {
        if (userRepository.findByUsername(username) != null) {
            throw new RuntimeException("Username already exists");
        }

        String hashedPassword = hashPassword(password);

        User user = new User();
        user.setUsername(username);
        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            // Handle exception
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null || !passwordMatches(password, user.getPassword())) {
            return false;
        }
        return true;
    }

    private boolean passwordMatches(String rawPassword, String hashedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(rawPassword, hashedPassword);
    }


}
