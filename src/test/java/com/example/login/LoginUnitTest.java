package com.example.login;


import com.example.login.entity.User;
import com.example.login.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;

@DataJpaTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class LoginUnitTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    AuthenticationManager authenticationManager;

    //testcase:
    @Test
    public void testSignUpUser(){
        User user = new User();
        //input
        user.setUsername("thanh2");
        String rawPassword = "12345";
        String encodedPassword = new BCryptPasswordEncoder().encode(rawPassword);
        user.setPassword(encodedPassword);

        User savedUser= userRepository.save(user);
        User existUser = entityManager.find(User.class, savedUser.getId());

        assert(user.getUsername()).equals(existUser.getUsername());
    }

    @Test
    public void testLoginUser() {
        // Create a user
        User user = new User();
        user.setUsername("thanh");
        String rawPassword = "12346";
        String encodedPassword = new BCryptPasswordEncoder().encode(rawPassword);
        user.setPassword(encodedPassword);
        // Authenticate the user
    }
}

