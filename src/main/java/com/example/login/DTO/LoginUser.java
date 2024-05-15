package com.example.login.DTO;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class LoginUser extends User {

    private static final long serialVersionUID = 1L;
    private int id;
    private String name;

    public LoginUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

}