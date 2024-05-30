package com.example.login.repository;

import com.example.login.entity.Role;
import com.example.login.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role,Long> {
    @Query("SELECT r from Role r where r.name = ?1")
    User findByName(String name);
}
