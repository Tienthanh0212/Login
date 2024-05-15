package com.example.login.service;

import com.example.login.DTO.UserDTO;
import com.example.login.entity.User;
import com.example.login.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

public interface UserService {

    void update (UserDTO userDTO);

    void delete (Integer id);
    void signup (UserDTO userDTO);

    UserDTO signIn(UserDTO userDTO);
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

    @Override
    public UserDTO signIn(UserDTO userDTO) {

        User user = userRepository.findByUsername(userDTO.getUsername());
        if (user != null && new BCryptPasswordEncoder().matches(userDTO.getPassword(), user.getPassword())) {

            return new ModelMapper().map(user, UserDTO.class);
        } else {
            return null;
        }
    }

}
