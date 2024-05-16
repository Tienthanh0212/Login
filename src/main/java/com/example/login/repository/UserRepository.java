package com.example.login.repository;

import com.example.login.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findByUsername(String username);

    //Lenh SQl de hien thi password trung
    //SELECT password, COUNT(*) FROM users GROUP BY password HAVING COUNT(*) > 1;
}
