package com.example.login.repository;

import com.example.login.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User,Integer> {
    @Query("SELECT u from User u where u.username = ?1")
    User findByUsername(String username);

    //Lenh SQl de hien thi password trung
    //SELECT password, COUNT(*) FROM users GROUP BY password HAVING COUNT(*) > 1;
}
