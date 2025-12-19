package com.hess.fitnessuserservice.dto;

import com.hess.fitnessuserservice.model.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
@Data
public class UserResponse {


    private String id;
    private String name;
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
