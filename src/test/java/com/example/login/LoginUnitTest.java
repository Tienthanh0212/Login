package com.example.login;


import com.example.login.entity.User;
import com.example.login.repository.UserRepository;
import com.example.login.service.JwtTokenService;
import com.example.login.service.UserService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.test.web.servlet.MockMvc;

@DataJpaTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LoginUnitTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired(required = false)
    JwtTokenService jwtTokenService;
    @Autowired
    private UserRepository userRepository;

    @MockBean
    UserService userService;

    @Autowired(required = false)
    MockMvc mockMvc;

    @Value("${http://localhost:8200}")
    String baseUrl;

    @MockBean
    AuthenticationManager authenticationManager;
    //testcase:
    @Test
    public void testSignUpUser(){
        User user = new User();
        //input
        user.setUsername("thanh2345");
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
        jwtTokenService.createToken(user.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        // Set the authenticated user in SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Assertions.assertNotNull(authentication, "Authentication object should not be null");

    }

    //Test khong su dung spring Security
    @Test
    public void testLoginUser1() {
        // Create a user
        User user = new User();
        user.setUsername("thanh");
        String rawPassword = "12346";
        String encodedPassword = new BCryptPasswordEncoder().encode(rawPassword);
        user.setPassword(encodedPassword);

        userRepository.save(user);

        Mockito.when(userService.login("thanh", "12346")).thenReturn(true);

        boolean loginSuccessful = userService.login("thanh", "12346");

        Assertions.assertTrue(loginSuccessful);
    }
}

