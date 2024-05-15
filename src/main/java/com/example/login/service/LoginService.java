package com.example.login.service;


import com.example.login.DTO.LoginUser;
import com.example.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoginService implements UserDetailsService {

    @Autowired
    UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.example.login.entity.User st =
                userRepo.findByUsername(username);

        if (st == null) {
            throw new UsernameNotFoundException("not found");
        }

        List<SimpleGrantedAuthority> list =
                new ArrayList<SimpleGrantedAuthority>();

        LoginUser currentUser =
                new LoginUser(username, st.getPassword(), list);

        currentUser.setId(st.getId());

        return currentUser;
    }
}
