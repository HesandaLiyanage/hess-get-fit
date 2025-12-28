package com.hess.fitnessactivityservice.controller;

import com.hess.fitnessactivityservice.dto.RegisterRequest;
import com.hess.fitnessactivityservice.dto.UserResponse;
import com.hess.fitnessactivityservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable String userId){
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody RegisterRequest request){

        return ResponseEntity.ok(userService.register(request));
    }
}
