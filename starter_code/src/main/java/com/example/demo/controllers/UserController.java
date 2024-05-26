package com.example.demo.controllers;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
	Logger log= LogManager.getLogger(UserController.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private  BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {

		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if(user == null) {
			log.error("UserController:findByUserName User {} ",username+" was not found");
			return ResponseEntity.notFound().build();
		}
		return  ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		try {
			User user = new User();
			user.setUsername(createUserRequest.getUsername());
			Cart cart = new Cart();
			cartRepository.save(cart);
			user.setCart(cart);
			if (createUserRequest.getPassword().length() < 7 ||
					!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
				log.error("CreateUserFail Error - Either length is less than 7 or pass and conf pass do not match. Unable to create {}", createUserRequest.getUsername());
				return ResponseEntity.badRequest().build();
			}
			user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
			userRepository.save(user);

			log.info("CreateUserSuccess User {} ", user.getUsername()+" was created successfully");
			return ResponseEntity.ok(user);
		}catch (Exception e){
			log.error("CreateUserFail Error in creating user {}",e.getMessage());
			return ResponseEntity.badRequest().build();
		}
	}
	
}
