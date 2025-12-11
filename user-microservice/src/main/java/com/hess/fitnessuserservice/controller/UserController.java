package com.hess.fitnessuserservice.controller;

import com.hess.fitnessuserservice.dto.RegisterRequest;
import com.hess.fitnessuserservice.dto.UserResponse;
import com.hess.fitnessuserservice.service.UserService;
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
    public ResponseEntity<UserResponse> getUserProfile(){
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody RegisterRequest request){

        return ResponseEntity.ok(userService.register(request));
    }
}
