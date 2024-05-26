package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {
    private UserController userController;

    private final UserRepository userRepository = mock(UserRepository.class) ;

    private final CartRepository cartRepository = mock(CartRepository.class);

    private final BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setup(){
        userController = new UserController();
        TestUtils.injectObjects(userController,"userRepository",userRepository);
        TestUtils.injectObjects(userController,"cartRepository",cartRepository);
        TestUtils.injectObjects(userController,"bCryptPasswordEncoder",encoder);
    }
    @Test
    public void testCreateUser(){
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("1234567");
        request.setConfirmPassword("1234567");

        final ResponseEntity<User> response = userController.createUser(request);

        Assert.assertNotNull(response);
        Assert.assertEquals(200,response.getStatusCodeValue());

        User user = response.getBody();
        Assert.assertNotNull(user);
        Assert.assertEquals(0,user.getId());
        Assert.assertEquals("test",user.getUsername());

    }
    @Test
    public void testCreateUserUnmatchedPassword(){
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("1234567");
        request.setConfirmPassword("1234568");

        final ResponseEntity<User> response = userController.createUser(request);

        Assert.assertNotNull(response);
        Assert.assertEquals(400,response.getStatusCodeValue());

    }
    @Test
    public void testFindByUserName(){
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("1234567");
        request.setConfirmPassword("1234567");

        final ResponseEntity<User> response1 = userController.createUser(request);
        Assert.assertEquals(200,response1.getStatusCodeValue());

        final ResponseEntity<User> response2 = userController.findByUserName("test");
        Assert.assertNotNull(response2);
        Assert.assertEquals(200,response2.getStatusCodeValue());
        Assert.assertEquals("test",response2.getBody().getUsername());

    }
}
