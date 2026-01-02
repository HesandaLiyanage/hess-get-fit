package com.hess.fitnessactivityservice.service;


import com.hess.fitnessactivityservice.dto.RegisterRequest;
import com.hess.fitnessactivityservice.dto.UserResponse;
import com.hess.fitnessactivityservice.model.User;
import com.hess.fitnessactivityservice.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {


    private UserRepository userRepository;
    public UserService (UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public UserResponse register(@Valid RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstname(request.getFirstName());
        user.setLastname(request.getLastName());

        User savedUser = userRepository.save(user);
        UserResponse userResponse = new UserResponse();
        userResponse.setId(savedUser.getId());
        userResponse.setEmail(savedUser.getEmail());
        userResponse.setPassword(savedUser.getPassword());
        userResponse.setFirstname(savedUser.getFirstname());
        userResponse.setLastname(savedUser.getLastname());
        userResponse.setUpdatedAt(savedUser.getUpdatedAt());
        userResponse.setCreatedAt(savedUser.getCreatedAt());

        return userResponse;
    }

    public UserResponse getUserProfile(String userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found for this id :: "+userId));

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setEmail(user.getEmail());
        userResponse.setPassword(user.getPassword());
        userResponse.setFirstname(user.getFirstname());
        userResponse.setLastname(user.getLastname());
        userResponse.setUpdatedAt(user.getUpdatedAt());
        userResponse.setCreatedAt(user.getCreatedAt());

        return userResponse;

    }

    public Boolean existByUserId(String userId) {
        return userRepository.existsById(userId);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
